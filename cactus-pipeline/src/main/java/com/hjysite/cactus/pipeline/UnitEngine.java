package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.AttributeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitEngine extends AttributeMap {
    Logger log = LoggerFactory.getLogger(UnitEngine.class);

    default void execute(Object o) {
        Object result = null;
        Throwable throwable = null;
        try {
            try {
                UnitEnginePipeline pipeline = executePipeline();
                log.debug("========= executePipeline开始执行 ============");
                pipeline.exec(o);
                log.debug("========= executePipeline执行结束 ============");
                result = pipeline.resultPoll();
            } catch (Throwable t) {
                throwable = t;
                whenFail(t);
                return;
            }
            whenSuccess(result);
        } finally {
            whenComplete(throwable == null ? result : throwable);
        }
    }

    UnitEnginePipeline executePipeline();

    UnitEnginePipeline completePipeline();

    UnitEnginePipeline successPipeline();

    UnitEnginePipeline failurePipeline();

    default UnitEnginePipeline addSuccessUnit(Unit unit) {
        return addSuccessUnit(null, unit);
    }

    default UnitEnginePipeline addSuccessUnit(String name, Unit unit) {
        return successPipeline().addLast(unit);
    }

    default UnitEnginePipeline addCompleteUnit(Unit unit) {
        return addCompleteUnit(null, unit);
    }

    default UnitEnginePipeline addCompleteUnit(String name, Unit unit) {
        return completePipeline().addLast(name, unit);
    }

    default UnitEnginePipeline addFailureUnit(Unit unit) {
        return addFailureUnit(null, unit);
    }

    default UnitEnginePipeline addFailureUnit(String name, Unit unit) {
        return failurePipeline().addLast(name, unit);
    }

    default void whenComplete(Object result) {
        log.debug("========= 开始执行completePipeline ============");
        completePipeline().exec(result);
        log.debug("========= 结束执行completePipeline ============");
    }

    default void whenSuccess(Object result) {
        log.debug("========= 开始执行successPipeline ============");
        successPipeline().exec(result);
        log.debug("========= 结束执行successPipeline ============");
    }

    default void whenFail(Throwable throwable) {
        log.debug("========= 开始执行failurePipeline ============");
        failurePipeline().exec(throwable);
        log.debug("========= 结束执行failurePipeline ============");
    }
}
