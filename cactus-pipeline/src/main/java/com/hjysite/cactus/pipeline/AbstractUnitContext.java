package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultConcurrentAttributeMap;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public abstract class AbstractUnitContext<T extends Unit> extends DefaultConcurrentAttributeMap implements UnitContext<T> {

    volatile AbstractUnitContext<?> prev;
    volatile AbstractUnitContext<?> next;

    private final UnitEngine engine;
    private final UnitContext<? extends UnitPipeline> holder;
    private final String name;
    private final T unit;

    protected AbstractUnitContext(UnitEngine engine, UnitContext<? extends UnitPipeline> holder, String name, T unit) {
        this.engine = engine;
        this.holder = holder;
        this.name = name;
        this.unit = unit;
    }

    @Override
    public AbstractUnitContext<?> prev() {
        return prev;
    }

    @Override
    public AbstractUnitContext<?> next() {
        return next;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public UnitEngine engine() {
        return engine;
    }

    @Override
    public UnitContext<? extends UnitPipeline> holder() {
        return holder;
    }

    @Override
    public T unit() {
        return unit;
    }

    @Override
    public UnitContext<?> fireWork(Object o) {
        next.unit().work(next, o);
        return this;
    }

    @Override
    public UnitContext<?> fireExceptionCaught(Throwable throwable) {
        next.unit().exceptionCaught(next, throwable);
        return this;
    }

    @Override
    public UnitContext<?> fireEventTriggered(Object evt) {
        next.unit().eventTriggered(next, evt);
        return this;
    }

    @Override
    public UnitContext<?> fireWorkCompleted() {
        next.unit().workCompleted(next);
        return this;
    }
}
