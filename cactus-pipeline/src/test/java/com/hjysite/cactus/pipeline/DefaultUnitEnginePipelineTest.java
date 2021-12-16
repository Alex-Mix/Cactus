package com.hjysite.cactus.pipeline;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DefaultUnitEnginePipelineTest {

    @Test
    void exec() {
    }

    @Test
    void findUnitContext() {
    }

    @Test
    void testFindUnitContext() {
    }

    @Test
    void testFindUnitContext1() {
    }

    @Test
    void firstContext() {
    }

    @Test
    void lastContext() {
    }

    @Test
    void clear() {
    }

    @Test
    void resultPoll() {
    }

    @Test
    void addFirst() {
        UnitEngine engine = mock(UnitEngine.class);
        DefaultUnitEnginePipeline testPipeline = new DefaultUnitEnginePipeline(engine);
        NoOpUnit first = new NoOpUnit();
        testPipeline.addFirst(first);
        assertNotNull(testPipeline.firstUnit());
        assertNotNull(testPipeline.lastUnit());
        assertEquals(testPipeline.firstUnit(), testPipeline.lastUnit());
        assertEquals(testPipeline.firstUnit(), first);

        NoOpUnitChain second = new NoOpUnitChain();
        testPipeline.addFirst(second);
        assertEquals(testPipeline.firstUnit(), second);
        assertEquals(testPipeline.lastUnit(), first);
    }

    @Test
    void addLast() {
        UnitEngine engine = mock(UnitEngine.class);
        DefaultUnitEnginePipeline testPipeline = new DefaultUnitEnginePipeline(engine);
        NoOpUnit first = new NoOpUnit();
        testPipeline.addLast(first);
        assertNotNull(testPipeline.firstUnit());
        assertNotNull(testPipeline.lastUnit());
        assertEquals(testPipeline.firstUnit(), testPipeline.lastUnit());
        assertEquals(testPipeline.firstUnit(), first);

        NoOpUnitChain second = new NoOpUnitChain();
        testPipeline.addLast(second);
        assertEquals(testPipeline.firstUnit(), first);
        assertEquals(testPipeline.lastUnit(), second);
    }

    @Test
    void addBefore() {
        UnitEngine engine = mock(UnitEngine.class);
        DefaultUnitEnginePipeline testPipeline = new DefaultUnitEnginePipeline(engine);
        NoOpUnitChain zero = new NoOpUnitChain();
        NoOpUnit first = new NoOpUnit();
        NoOpUnitChain second = new NoOpUnitChain();
        NoOpUnit third = new NoOpUnit();

        testPipeline.addLast("first", first);
        testPipeline.addLast("third", third);

        testPipeline.addBefore("third", "second", second);
        List<String> names = List.of("first", "second", "third");
        assertIterableEquals(testPipeline.names(), names);

        testPipeline.addBefore("first", "zero", zero);
        names = List.of("zero", "first", "second", "third");
        assertIterableEquals(testPipeline.names(), names);
    }

    @Test
    void addAfter() {
    }

    @Test
    void remove() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void testRemove1() {
    }

    @Test
    void removeFirst() {
    }

    @Test
    void removeLast() {
    }

    @Test
    void replace() {
    }

    @Test
    void testReplace() {
    }

    @Test
    void testReplace1() {
    }

    @Test
    void findUnit() {
    }

    @Test
    void testFindUnit() {
    }

    @Test
    void testFindUnit1() {
    }

    @Test
    void firstUnit() {
    }

    @Test
    void lastUnit() {
    }

    @Test
    void isExist() {
    }

    @Test
    void testIsExist() {
    }

    @Test
    void testIsExist1() {
    }

    @Test
    void names() {
        UnitEngine engine = mock(UnitEngine.class);
        DefaultUnitEnginePipeline testPipeline = new DefaultUnitEnginePipeline(engine);
        NoOpUnitChain zero = new NoOpUnitChain();
        NoOpUnit first = new NoOpUnit();
        NoOpUnitChain second = new NoOpUnitChain();
        NoOpUnit third = new NoOpUnit();
        testPipeline.addLast(zero, first, second, third);

        List<String> names = List.of("com.hjysite.cactus.pipeline.DefaultUnitEnginePipelineTest$NoOpUnitChain#0",
                "com.hjysite.cactus.pipeline.DefaultUnitEnginePipelineTest$NoOpUnit#0",
                "com.hjysite.cactus.pipeline.DefaultUnitEnginePipelineTest$NoOpUnitChain#1",
                "com.hjysite.cactus.pipeline.DefaultUnitEnginePipelineTest$NoOpUnit#1");
        assertIterableEquals(testPipeline.names(), names);

        DefaultUnitEnginePipeline testPipeline1 = new DefaultUnitEnginePipeline(engine);
        testPipeline1.addLast("first", first)
                .addLast("second", second)
                .addLast("third", third);

        names = List.of("first", "second", "third");
        assertIterableEquals(testPipeline1.names(), names);
    }

    @Test
    public void testIntIncrementEngine() {
        UnitEngine engine = mock(UnitEngine.class);
        DefaultUnitEnginePipeline testPipeline = new DefaultUnitEnginePipeline(engine);
        testPipeline.addLast(new IntIncrementUnit())
                .addLast(new IntIncrementUnit())
                .addLast(new IntIncrementUnit())
                .addLast(new DefaultUnitChain()
                        .addLast(new IntIncrementUnit())
                        .addLast(new IntIncrementUnit()))
                .addLast(new IntIncrementUnit());
        testPipeline.exec(0);
        Object result = testPipeline.resultPoll();
        assertEquals(result, 6);
    }

    public final class IntIncrementUnit extends GenericsUnit<Integer> {
        @Override
        public void work0(InvokeUnitContext ctx, Integer origin) {
            ctx.fireWork(origin + 1);
        }
    }


    public final class NoOpUnit implements Unit {
        @Override
        public void work(InvokeUnitContext ctx, Object o) {
            ctx.fireWork(o);
        }
    }

    public final class NoOpUnitChain extends DefaultUnitChain {
        @Override
        public void work(InvokeUnitContext ctx, Object o) {
            ctx.fireWork(o);
        }
    }
}