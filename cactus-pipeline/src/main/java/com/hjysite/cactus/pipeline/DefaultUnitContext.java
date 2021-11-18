package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public class DefaultUnitContext extends AbstractUnitContext<Unit> {
    protected DefaultUnitContext(UnitEngine engine, UnitContext<? extends UnitPipeline> holder, String name, Unit unit) {
        super(engine, holder, name, unit);
    }
}
