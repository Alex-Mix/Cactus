package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/25
 **/
public class DefaultUnitInvokerContext extends AbstractUnitInvokerContext {
    private final Unit unit;

    public DefaultUnitInvokerContext(UnitEngine engine, UnitEnginePipeline pipeline, String name, Unit unit) {
        super(engine, pipeline, null, name);
        this.unit = unit;
    }

    @Override
    public Unit unit() {
        return unit;
    }
}
