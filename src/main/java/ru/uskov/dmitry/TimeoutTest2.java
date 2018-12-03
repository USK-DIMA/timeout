package ru.uskov.dmitry;

import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(time = 3)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@Fork(value = 1)
@Threads(4)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class TimeoutTest2 {

    private Set<String> concurrentSkipListSet;
    private ScheduledExecutorService service;

    @Setup(Level.Iteration)
    public void beforeIteration() {
        concurrentSkipListSet = new ConcurrentSkipListSet<>();
        service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @TearDown(Level.Iteration)
    public void afterIteration() {
        service.shutdown();
        System.out.println("CPU CORE: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Usage memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        System.out.println("Set size: " + concurrentSkipListSet.size());
    }

    @Benchmark
    public void concurrentSkipListSetPutX() {
        concurrentSkipListSet.add("x");
    }

    @Benchmark
    public void executorServiceTest() {
        service.schedule(()->{}, 100, TimeUnit.MILLISECONDS);
    }


}
