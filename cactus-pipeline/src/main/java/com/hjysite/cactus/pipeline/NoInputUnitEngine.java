package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultConcurrentAttributeMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/12/29
 **/
@Slf4j
public class NoInputUnitEngine extends DefaultConcurrentAttributeMap implements UnitEngine<NoInputUnit> {
    private final NoInputUnitEnginePipeline executePipeline;
    private final UnitEnginePipeline<Unit> completePipeline;
    private final NoInputUnitEnginePipeline successPipeline;
    private final UnitEnginePipeline<Unit> failurePipeline;

    public NoInputUnitEngine(NoInputUnitEnginePipeline executePipeline, UnitEnginePipeline<Unit> completePipeline, NoInputUnitEnginePipeline successPipeline, UnitEnginePipeline<Unit> failurePipeline) {
        this.executePipeline = executePipeline;
        this.completePipeline = completePipeline;
        this.successPipeline = successPipeline;
        this.failurePipeline = failurePipeline;
    }

    @Override
    public final void execute(Object o) {
        log.warn("this input object will be ignore: {}", o);
        execute();
    }

    public void execute() {
        Throwable throwable = null;
        try {
            try {
                NoInputUnitEnginePipeline pipeline = executePipeline();
                log.debug("========= executePipeline开始执行 ============");
                pipeline.exec();
                log.debug("========= executePipeline执行结束 ============");
            } catch (Throwable t) {
                throwable = t;
                log.debug("========= 开始执行failurePipeline ============");
                failurePipeline().exec(throwable);
                log.debug("========= 结束执行failurePipeline ============");
                return;
            }
            log.debug("========= 开始执行successPipeline ============");
            successPipeline().exec();
            log.debug("========= 结束执行successPipeline ============");
        } finally {
            log.debug("========= 开始执行completePipeline ============");
            completePipeline().exec(throwable);
            log.debug("========= 结束执行completePipeline ============");
        }
    }

    @Override
    public NoInputUnitEnginePipeline executePipeline() {
        return executePipeline;
    }

    @Override
    public UnitEnginePipeline<Unit> completePipeline() {
        return completePipeline;
    }

    @Override
    public NoInputUnitEnginePipeline successPipeline() {
        return successPipeline;
    }

    @Override
    public UnitEnginePipeline<Unit> failurePipeline() {
        return failurePipeline;
    }
}
