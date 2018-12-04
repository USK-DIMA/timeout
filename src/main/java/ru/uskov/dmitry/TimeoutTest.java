package ru.uskov.dmitry;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(time = 10)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@Fork(value = 1, jvmArgs = "-Xmx6G")
@Threads(Threads.MAX)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class TimeoutTest {

    private Set<Long> concurrentSkipListSet;
    private ScheduledExecutorService service;
    private Random random;

    @Setup(Level.Iteration)
    public void beforeIteration() {
        random = new Random();
        concurrentSkipListSet = new ConcurrentSkipListSet<>();
        service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @TearDown(Level.Iteration)
    public void afterIteration() {
        service.shutdown();
        try{
            System.out.println("Wait service");
            service.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException: " + e.getMessage());
        }
        System.out.println("CPU CORE: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Usage memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        System.out.println("Set size: " + concurrentSkipListSet.size());
    }


    @Benchmark
    public long base() {
        return getDelayTime();
    }

    @Benchmark
    public void concurrentSkipListSetPutX() {
        concurrentSkipListSet.add(getDelayTime() + System.currentTimeMillis());
    }

    @Benchmark
    public void executorServiceTest() {
        service.schedule(()->{}, getDelayTime(), TimeUnit.MINUTES);
    }


    private long getDelayTime() {
        return 60L + random.nextLong()%100_000L;
    }


}
