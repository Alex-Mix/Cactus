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
    private final AbstractUnitEnginePipeline<Unit> pipeline;
    private final UnitChainContext chainContext;
    private final String name;

    protected AbstractInvokeUnitContext(UnitEngine engine, AbstractUnitEnginePipeline<Unit> pipeline, UnitChainContext chainContext, String name) {
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
    public UnitEnginePipeline<Unit> pipeline() {
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
}
