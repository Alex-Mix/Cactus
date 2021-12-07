package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultAttributeMap;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public abstract class AbstractInvokeUnitContext extends DefaultAttributeMap implements InvokeUnitContext {
    volatile AbstractInvokeUnitContext prev;
    volatile AbstractInvokeUnitContext next;

    private final UnitEngine engine;
    private final UnitEnginePipeline pipeline;
    private final UnitChainContext chainContext;
    private final String name;

    protected AbstractInvokeUnitContext(UnitEngine engine, UnitEnginePipeline pipeline, UnitChainContext chainContext, String name) {
        this.engine = engine;
        this.pipeline = pipeline;
        this.chainContext = chainContext;
        this.name = name;
    }

    @Override
    public AbstractInvokeUnitContext prev() {
        return prev;
    }

    @Override
    public AbstractInvokeUnitContext next() {
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
    public UnitEnginePipeline pipeline() {
        return pipeline;
    }

    @Override
    public UnitChainContext chainContext() {
        return chainContext;
    }

    @Override
    public AbstractInvokeUnitContext fireWork(Object o) {
        next.unit().work(next, o);
        return this;
    }

    @Override
    public AbstractInvokeUnitContext fireExceptionCaught(Throwable throwable) {
        next.unit().exceptionCaught(next, throwable);
        return this;
    }

    @Override
    public AbstractInvokeUnitContext fireEventTriggered(Object evt) {
        next.unit().eventTriggered(next, evt);
        return this;
    }

    @Override
    public AbstractInvokeUnitContext fireWorkCompleted() {
        next.unit().workCompleted(next);
        return this;
    }
}
