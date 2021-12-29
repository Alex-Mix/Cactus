package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultConcurrentAttributeMap;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/12/29
 **/
public class DefaultUnitEngine extends DefaultConcurrentAttributeMap implements UnitEngine<Unit> {

    private final UnitEnginePipeline<Unit> executePipeline;
    private final UnitEnginePipeline<Unit> completePipeline;
    private final UnitEnginePipeline<Unit> successPipeline;
    private final UnitEnginePipeline<Unit> failurePipeline;

    public DefaultUnitEngine(UnitEnginePipeline<Unit> executePipeline, UnitEnginePipeline<Unit> completePipeline, UnitEnginePipeline<Unit> successPipeline, UnitEnginePipeline<Unit> failurePipeline) {
        this.executePipeline = executePipeline;
        this.completePipeline = completePipeline;
        this.successPipeline = successPipeline;
        this.failurePipeline = failurePipeline;
    }


    @Override
    public UnitEnginePipeline<Unit> executePipeline() {
        return executePipeline;
    }

    @Override
    public UnitEnginePipeline<Unit> completePipeline() {
        return completePipeline;
    }

    @Override
    public UnitEnginePipeline<Unit> successPipeline() {
        return successPipeline;
    }

    @Override
    public UnitEnginePipeline<Unit> failurePipeline() {
        return failurePipeline;
    }
}
