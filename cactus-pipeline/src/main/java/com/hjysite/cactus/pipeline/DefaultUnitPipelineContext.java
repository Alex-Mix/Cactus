package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/17
 **/
public class DefaultUnitPipelineContext extends AbstractUnitPipelineContext {
    protected DefaultUnitPipelineContext(UnitEngine engine, UnitContext<? extends UnitPipeline> holder, String name, UnitPipeline unit) {
        super(engine, holder, name, unit);
    }
}
