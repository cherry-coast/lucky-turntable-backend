package com.cherry.lucky.service.impl;

import com.cherry.lucky.constant.StringConstant;
import com.cherry.lucky.domain.CherryResponseEntity;
import com.cherry.lucky.service.MonitorService;
import com.cherry.lucky.utils.CherryDateUtil;
import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.util.GlobalConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author cherry
 * @version 1.0.0
 * @ClassName MonitorServiceImpl
 * @Description
 * @createTime 2023年05月19日 10:54:00
 */
@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

    private static final int SLEEP_TIME = 1;


    @Override
    public CherryResponseEntity<BigDecimal> cpu() {
        GlobalConfig.set(GlobalConfig.OSHI_OS_WINDOWS_CPU_UTILITY, true);
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.SECONDS.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        System.out.println(Arrays.toString(prevTicks));
        System.out.println(Arrays.toString(ticks));
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        System.err.println("cpu核数:" + processor.getLogicalProcessorCount());
        System.err.println("cpu系统使用率:" + new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));
        System.err.println("cpu用户使用率:" + new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));
        System.err.println("cpu当前等待率:" + new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));
        System.err.println("cpu当前空闲率:" + new DecimalFormat("#.##%").format(idle * 1.0 / totalCpu));


        long cpuUtilization = user + nice + cSys  + iowait + irq + softirq + steal;
        System.err.println("cpu利用率：" + new DecimalFormat("#.##%").format(cpuUtilization * 1.0 / totalCpu));

        System.err.println("cpu系统使用率:" + cSys * 1.0 / totalCpu);



        return CherryResponseEntity.success(
                new BigDecimal(cSys)
                        .multiply(new BigDecimal("1.0"))
                        .divide(new BigDecimal(totalCpu), 2, RoundingMode.HALF_UP)
        );
    }


    @Override
    public CherryResponseEntity<BigDecimal> networkRate() {
        Properties props = System.getProperties();
        String os = props.getProperty("os.name").toLowerCase();
        os = os.startsWith("win") ? StringConstant.WINDOWS_SYSTEM : StringConstant.LINUX_SYSTEM;
        Map<String, BigDecimal> result = new HashMap<>(16);
        Process pro = null;
        Runtime runtime = Runtime.getRuntime();
        BufferedReader input = null;
        BigDecimal rxPercent = BigDecimal.ZERO;
        BigDecimal txPercent = BigDecimal.ZERO;
        try {
            String command = StringConstant.WINDOWS_SYSTEM.equals(os) ? "netstat -e" : "ifconfig";
            pro = runtime.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            long[] packetResultAtFirst = readInLine(input, os);
            TimeUnit.SECONDS.sleep(SLEEP_TIME);
            pro.destroy();
            input.close();
            pro = runtime.exec(command);
            input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            long[] packetResultAtSecond = readInLine(input, os);

            BigDecimal unit = new BigDecimal(SLEEP_TIME)
                    .divide(new BigDecimal("1000"), 6, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("1024.0"));
            rxPercent = new BigDecimal(packetResultAtSecond[0]).subtract(new BigDecimal(packetResultAtFirst[0]));
            txPercent = new BigDecimal(packetResultAtSecond[1]).subtract(new BigDecimal(packetResultAtFirst[1]));
            // 下行速率(kB/s)
            rxPercent = rxPercent.divide(unit, 2, RoundingMode.HALF_UP);
            // 上行速率(kB/s)
            txPercent = txPercent.divide(unit, 2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Optional.ofNullable(pro).ifPresent(p -> p.destroy());
        }
        // 下行速率
        result.put("rxPercent", rxPercent);
        // 上行速率
        result.put("txPercent", txPercent);
        System.out.println(result);
        return null;
    }


    private static long[] readInLine(BufferedReader input, String osType) {
        long[] percent = new long[2];
        StringTokenizer tokenStat;
        try {
            if (osType.equals(StringConstant.LINUX_SYSTEM)) {
                // 获取linux环境下的网口上下行速率
                long rx = 0, tx = 0;
                String line = null;
                //RX packets:4171603 errors:0 dropped:0 overruns:0 frame:0
                //TX packets:4171603 errors:0 dropped:0 overruns:0 carrier:0
                while ((line = input.readLine()) != null) {
                    if (line.contains(StringConstant.RX_PACKETS)) {
                        rx += Long.parseLong(line.substring(line.indexOf(StringConstant.RX_PACKETS) + 11, line.indexOf(" ", line.indexOf(StringConstant.RX_PACKETS) + 11)));
                    } else if (line.contains(StringConstant.TX_PACKETS)) {
                        tx += Long.parseLong(line.substring(line.indexOf(StringConstant.TX_PACKETS) + 11, line.indexOf(" ", line.indexOf(StringConstant.TX_PACKETS) + 11)));
                    }
                }
                percent[0] = rx;
                percent[1] = tx;
            } else {
                // 获取windows环境下的网口上下行速率
                input.readLine();
                input.readLine();
                input.readLine();
                input.readLine();
                tokenStat = new StringTokenizer(input.readLine());
                tokenStat.nextToken();
                percent[0] = Long.parseLong(tokenStat.nextToken());
                percent[1] = Long.parseLong(tokenStat.nextToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return percent;
    }


    @Override
    public CherryResponseEntity<Map<String, Object>> memory() {

        Map<String, Object> data = new HashMap<>(16);

        SystemInfo systemInfo = new SystemInfo();
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 椎内存使用情况
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();

        // 初始的总内存
        long initTotalMemorySize = memoryUsage.getInit();
        // 最大可用内存
        long maxMemorySize = memoryUsage.getMax();
        // 已使用的内存
        long usedMemorySize = memoryUsage.getUsed();

        // 操作系统
        String osName = System.getProperty("os.name");
        // 总的物理内存
        String totalMemorySize = new DecimalFormat("#.##").format(osmxb.getTotalMemorySize() / 1024.0 / 1024 / 1024);
        // 剩余的物理内存
        String freePhysicalMemorySize = new DecimalFormat("#.##").format(osmxb.getFreeMemorySize() / 1024.0 / 1024 / 1024);
        // 已使用的物理内存
        BigDecimal usedMemory = new BigDecimal("1").subtract(new BigDecimal(freePhysicalMemorySize).divide(new BigDecimal(totalMemorySize), 4, RoundingMode.HALF_UP));

        System.out.println(usedMemory);
        data.put("date", CherryDateUtil.getNowDate(CherryDateUtil.HH_MM_SS));
        data.put("usedMemory", usedMemory.multiply(new BigDecimal(100)));
        return CherryResponseEntity.success(data);


//        // 获得线程总数
//        ThreadGroup parentThread;
//        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
//                .getParent() != null; parentThread = parentThread.getParent()) {
//
//        }
//
//        int totalThread = parentThread.activeCount();
//
//        // 磁盘使用情况
//        File[] files = File.listRoots();
//        for (File file : files) {
//            String total = new DecimalFormat("#.#").format(file.getTotalSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
//            String free = new DecimalFormat("#.#").format(file.getFreeSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
//            String un = new DecimalFormat("#.#").format(file.getUsableSpace() * 1.0 / 1024 / 1024 / 1024) + "G";
//            String path = file.getPath();
//            System.err.println(path + "总:" + total + ",可用空间:" + un + ",空闲空间:" + free);
//            System.err.println("=============================================");
//        }
//
//        System.err.println("操作系统:" + osName);
//        System.err.println("程序启动时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime())));
//        System.err.println("cpu核数:" + Runtime.getRuntime().availableProcessors());
//        System.err.println("JAVA_HOME:" + System.getProperty("java.home"));
//        System.err.println("JAVA_VERSION:" + System.getProperty("java.version"));
//        System.err.println("USER_HOME:" + System.getProperty("user.home"));
//        System.err.println("USER_NAME:" + System.getProperty("user.name"));
//        System.err.println("初始的总内存(JVM):" + new DecimalFormat("#.#").format(initTotalMemorySize * 1.0 / 1024 / 1024) + "M");
//        System.err.println("最大可用内存(JVM):" + new DecimalFormat("#.#").format(maxMemorySize * 1.0 / 1024 / 1024) + "M");
//        System.err.println("已使用的内存(JVM):" + new DecimalFormat("#.#").format(usedMemorySize * 1.0 / 1024 / 1024) + "M");
//        System.err.println("总的物理内存:" + totalMemorySize);
//        System.err.println("总的物理内存:" + new DecimalFormat("#.##").format(systemInfo.getHardware().getMemory().getTotal() * 1.0 / 1024 / 1024 / 1024) + "M");
//        System.err.println("剩余的物理内存:" + freePhysicalMemorySize);
//        System.err.println("剩余的物理内存:" + new DecimalFormat("#.##").format(systemInfo.getHardware().getMemory().getAvailable() * 1.0 / 1024 / 1024 / 1024) + "M");
//        System.err.println("已使用的物理内存:" + usedMemory);
//        System.err.println("已使用的物理内存:" + new DecimalFormat("#.##").format((systemInfo.getHardware().getMemory().getTotal() - systemInfo.getHardware().getMemory().getAvailable()) * 1.0 / 1024 / 1024 / 1024) + "M");
//        System.err.println("总线程数:" + totalThread);
//        System.err.println("===========================");


//        return null;
    }


    @Override
    public CherryResponseEntity<BigDecimal> disk() {
        BigDecimal totalCount = BigDecimal.ZERO;
        BigDecimal freeCount = BigDecimal.ZERO;
        File[] files = File.listRoots();
        for (File file : files) {
            String total = new DecimalFormat("#.#").format(file.getTotalSpace() * 1.0 / 1024 / 1024 / 1024);
            String free = new DecimalFormat("#.#").format(file.getFreeSpace() * 1.0 / 1024 / 1024 / 1024);
            totalCount = totalCount.add(new BigDecimal(total));
            freeCount = freeCount.add(new BigDecimal(free));
        }
        BigDecimal ratio = freeCount.divide(totalCount, 2, RoundingMode.HALF_UP);
        return CherryResponseEntity.success(new BigDecimal("1").subtract(ratio));
    }
}
