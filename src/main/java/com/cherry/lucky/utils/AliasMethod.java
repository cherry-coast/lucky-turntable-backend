package com.cherry.lucky.utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class AliasMethod {
    /**
     * The random number generator used to sample from the distribution.
     */
    private final Random random;
 
    /**
     * Supplementary index of data tables
     */
    private final int[] complementary;
    /**
     * Probability tables
     */
    private final double[] probability;
 
    /**
     * Constructs a new AliasMethod to sample from a discrete distribution and
     * hand back outcomes based on the probability distribution.
     *
     * @param probabilities The list of probabilities.
     */
    public AliasMethod(List<Double> probabilities) {
        this(probabilities, new Random());
    }
 
    /**
     * Constructs a new AliasMethod to sample from a discrete distribution and
     * hand back outcomes based on the probability distribution.
     * <p/>
     * Given as input a list of probabilities corresponding to outcomes 0, 1,
     * ..., n - 1, along with the random number generator that should be used
     * as the underlying generator, this constructor creates the probability
     * and complementary tables needed to efficiently sample from this distribution.
     *
     * @param probabilities probabilities list.
     * @param random       The random number generator
     */
    public AliasMethod(List<Double> probabilities, Random random) {
        // Basic parameter inspection and verification
        if (probabilities == null || random == null) {
            throw new NullPointerException("probabilities or random is null. ");
        }
        if (probabilities.size() == 0) {
            throw new IllegalArgumentException("probabilities list must be nonempty. ");
        }
 
        // Allocate space for the probability and complementary tables.
        probability = new double[probabilities.size()];
        complementary = new int[probabilities.size()];
 
        // Store the random number generator.
        this.random = random;
 
        // Compute the average probability
        final double average = 1.0 / probabilities.size();

        // Make a copy of the probabilities list, since we will be making changes to it.
        probabilities = new ArrayList<Double>(probabilities);
 
        // Create two stacks to act as work lists as we populate the tables.
        Deque<Integer> small = new ArrayDeque<Integer>();
        Deque<Integer> large = new ArrayDeque<Integer>();
 
        // Traverse the original probability table to fill the created small stack and large stack
        for (int i = 0; i < probabilities.size(); ++i) {
            /*
             * If the probability is below the average probability, then we add
             * it to the small list; otherwise we add it to the large list.
             */
            if (probabilities.get(i) >= average) {
                large.add(i);
            } else {
                small.add(i);
            }
        }
 
        /*
         * As a note: in the mathematical specification of the algorithm, we
         * will always exhaust the small list before the big list.  However,
         * due to floating point inaccuracies, this is not necessarily true.
         * Consequently, this inner loop (which tries to pair small and large
         * elements) will have to check that both lists aren't empty.
         */
        while (!small.isEmpty() && !large.isEmpty()) {
            /* Get the last index of the small and the large probabilities. */
            int less = small.removeLast();
            int more = large.removeLast();
 
            // 将小概率取出来并放大其概率，同时设置其补集为当前的大堆里面的索引
            probability[less] = probabilities.get(less) * probabilities.size();
            complementary[less] = more;



            // 将大概率缩小，最终缩小的值 = 计算的每一列平均概率 - 前面小概率放大的值
            probabilities.set(more,
                    (probabilities.get(more) + probabilities.get(less)) - average);
 
            // 重新计算当前的大概率是否应该继续下一轮的缩小
            if (probabilities.get(more) >= 1.0 / probabilities.size()) {
                large.add(more);
            } else {
                small.add(more);
            }
        }
 
        /* At this point, everything is in one list, which means that the
         * remaining probabilities should all be 1/n.  Based on this, set them
         * appropriately.  Due to numerical issues, we can't be sure which
         * stack will hold the entries, so we empty both.
         */
        // 多余的概率将被设置为100% 同时此概率没有对应的补集
        while (!small.isEmpty()) {
            probability[small.removeLast()] = 1.0;
        }
        while (!large.isEmpty()) {
            probability[large.removeLast()] = 1.0;
        }
    }
 
    /**
     * Samples a value from the underlying distribution.
     *
     * @return A random value sampled from the underlying distribution.
     */
    public int next() {
        /* Generate a fair die roll to determine which column to inspect. */
        int column = random.nextInt(probability.length);
 
        /* Generate a biased coin toss to determine which option to pick. */
        boolean coinToss = random.nextDouble() < probability[column];
 
        /* Based on the outcome, return either the column or its alias. */
       /* Log.i("1234","column="+column);
        Log.i("1234","coinToss="+coinToss);
        Log.i("1234","alias[column]="+coinToss);*/
        return coinToss ? column : complementary[column];
    }
 
    public static void main(String[] args) {
        TreeMap<String, Double> map = new TreeMap<String, Double>();
        map.put("苹果11pro 256G", 0.0);
        map.put("华为P40 pro", 0.0);
        map.put("100元现金", 0.001);
        map.put("100元京东卡", 0.004);

        map.put("50元话费", 0.01);
        map.put("永久翻译包",0.03);
        map.put("7天翻译包", 0.05);
        map.put("230颗蓝砖", 0.05);
        map.put("一个月会员", 0.08);
        map.put("100粉砖", 0.1);
        map.put("20粉砖",0.2);
        map.put("5粉砖",0.475);
        List<Double> list = new ArrayList<Double>(map.values());
        List<String> gifts = new ArrayList<String>(map.keySet());
 
        AliasMethod method = new AliasMethod(list);
 
        Map<String, AtomicInteger> resultMap = new HashMap<String, AtomicInteger>();
 
        for (int i = 0; i < 100000; i++) {
            int index = method.next();
            String key = gifts.get(index);
            if (!resultMap.containsKey(key)) {
                resultMap.put(key, new AtomicInteger());
            }
            resultMap.get(key).incrementAndGet();
        }
        for (String key : resultMap.keySet()) {

            System.out.println(key + "==" + (100000 / Long.parseLong(String.valueOf(resultMap.get(key)))));
        }
 
    }
}