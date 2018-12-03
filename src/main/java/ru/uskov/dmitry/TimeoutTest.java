package ru.uskov.dmitry;

import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(time = 10)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@Fork(value = 1)
@Threads(4)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class TimeoutTest {

    private Set<UUID> concurrentSkipListSet;
    private ScheduledExecutorService service;

    @Setup(Level.Iteration)
    public void beforeIteration() {
        concurrentSkipListSet = new ConcurrentSkipListSet<>();
        service = Executors.newScheduledThreadPool(4);
    }

    @TearDown(Level.Iteration)
    public void afterIteration() {
        List<Runnable> list = service.shutdownNow();
        System.out.println("CPU CORE: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory());
        System.out.println("Usage memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        System.out.println("Runnable size:" + list.size());
        System.out.println("Set size:" + list.size());
    }

    @Benchmark
    public UUID line() {
        return UUID.randomUUID();
    }

    @Benchmark
    public boolean concurrentSkipListSetPutX() {
        return concurrentSkipListSet.add(UUID.randomUUID());
    }

    @Benchmark
    public ScheduledFuture<?> executorServiceTest() {
        UUID randomUUID = UUID.randomUUID();
        return service.schedule(() -> System.out.println(randomUUID), 1, TimeUnit.MINUTES);
    }


}
