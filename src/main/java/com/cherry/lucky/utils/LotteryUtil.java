package com.cherry.lucky.utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * @author cherry
 * @version 1.0.0
 * @ClassName LotteryUtil
 * @Description
 * @createTime 2023年03月23日 17:02:00
 */
public class LotteryUtil {

    private final List<Double> averageProbability;

    private final Random random;

    public LotteryUtil(List<Double> probability) {
        this(probability, new Random());
    }

    private LotteryUtil(List<Double> probability, Random random) {
        // Basic parameter inspection and verification
        if (probability == null || random == null) {
            throw new NullPointerException("probabilities or random is null. ");
        }
        if (probability.size() == 0) {
            throw new IllegalArgumentException("probabilities list must be nonempty. ");
        }

        // Store the random number generator.
        this.random = random;

        // Sum the data in the probability table
        Double totalProbability = probability.stream().reduce(0.00, Double::sum);

        // Calculate the average probability of each probability under the total probability
        AtomicReference<Double> tempProbability = new AtomicReference<>(0.00);
        averageProbability = probability.stream().map(x -> {
            tempProbability.updateAndGet(v -> v + x);
            return tempProbability.get() / totalProbability;
        }).collect(Collectors.toList());
    }

    /**
     * Obtain the extracted item index based on the block value
     *
     * @return prize index
     */
    public Integer getPrizeIndex() {
        double nextDouble = random.nextDouble();
        averageProbability.add(nextDouble);
        Collections.sort(averageProbability);
        int index = averageProbability.indexOf(nextDouble);
        averageProbability.remove(index);
        return index;
    }

}
