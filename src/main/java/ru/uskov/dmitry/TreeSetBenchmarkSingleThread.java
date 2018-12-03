package ru.uskov.dmitry;

import org.openjdk.jmh.annotations.*;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(time = 10)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@Fork(value = 1)
@Threads(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class TreeSetBenchmarkSingleThread {

    private Set<UUID> treeSet;
    private Set<UUID> concurrentSkipListSet;

    @Setup(Level.Iteration)
    public void beforeIteration() {
        treeSet = new TreeSet<>();
        concurrentSkipListSet = new ConcurrentSkipListSet<>();
    }

    @Benchmark
    public UUID line() {
        return UUID.randomUUID();
    }

    @Benchmark
    public void treeSetPutX() {
        treeSet.add(UUID.randomUUID());
    }

    @Benchmark
    public void concurrentSkipListSetPutX() {
        concurrentSkipListSet.add(UUID.randomUUID());
    }


}
