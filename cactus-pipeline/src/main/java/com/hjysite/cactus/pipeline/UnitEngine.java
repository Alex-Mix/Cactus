package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.AttributeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitEngine<T extends Unit> extends AttributeMap {
    Logger log = LoggerFactory.getLogger(UnitEngine.class);

    default void execute(Object o) {
        Object result = null;
        Throwable throwable = null;
        try {
            try {
                UnitEnginePipeline<T> pipeline = executePipeline();
                log.debug("========= executePipeline开始执行 ============");
                pipeline.exec(o);
                log.debug("========= executePipeline执行结束 ============");
                result = pipeline.resultPoll();
            } catch (Throwable t) {
                throwable = t;
                log.debug("========= 开始执行failurePipeline ============");
                failurePipeline().exec(throwable);
                log.debug("========= 结束执行failurePipeline ============");
                return;
            }
            log.debug("========= 开始执行successPipeline ============");
            successPipeline().exec(result);
            log.debug("========= 结束执行successPipeline ============");
        } finally {
            log.debug("========= 开始执行completePipeline ============");
            completePipeline().exec(throwable == null ? result : throwable);
            log.debug("========= 结束执行completePipeline ============");
        }
    }

    UnitEnginePipeline<T> executePipeline();

    UnitEnginePipeline<Unit> completePipeline();

    UnitEnginePipeline<T> successPipeline();

    UnitEnginePipeline<Unit> failurePipeline();

    default UnitEnginePipeline<T> addSuccessUnit(T unit) {
        return addSuccessUnit(null, unit);
    }

    default UnitEnginePipeline<T> addSuccessUnit(String name, T unit) {
        return successPipeline().addLast(unit);
    }

    default UnitEnginePipeline<Unit> addCompleteUnit(T unit) {
        return addCompleteUnit(null, unit);
    }

    default UnitEnginePipeline<Unit> addCompleteUnit(String name, Unit unit) {
        return completePipeline().addLast(name, unit);
    }

    default UnitEnginePipeline<Unit> addFailureUnit(T unit) {
        return addFailureUnit(null, unit);
    }

    default UnitEnginePipeline<Unit> addFailureUnit(String name, Unit unit) {
        return failurePipeline().addLast(name, unit);
    }
}
