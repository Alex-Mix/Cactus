package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.AttributeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public interface UnitEngine extends AttributeMap {

    Logger log = LoggerFactory.getLogger(UnitEngine.class);

    default void execute(Object o) {
        Object result = null;
        try {
            try {
                AbstractUnitPipelineContext ctx = executePipeline();
                log.debug("========= 开始执行executePipeline ============");
                invokeUnitPipelineWork(ctx, o);
                log.debug("========= 开始执行executePipeline ============");
                result = ctx.invokeResultPoll();
            } catch (Throwable t) {
                whenFail(t);
                return;
            }
            whenSuccess(result);
        } finally {
            whenComplete(result);
        }
    }

    AbstractUnitPipelineContext executePipeline();

    AbstractUnitPipelineContext completePipeline();

    AbstractUnitPipelineContext successPipeline();

    AbstractUnitPipelineContext failurePipeline();

    default AbstractUnitPipelineContext addSuccessUnit(Unit unit) {
        return addSuccessUnit(null, unit);
    }

    default AbstractUnitPipelineContext addSuccessUnit(String name, Unit unit) {
        return successPipeline().invokeAddLast(name, unit);
    }

    default AbstractUnitPipelineContext addCompleteUnit(Unit unit) {
        return addCompleteUnit(null, unit);
    }

    default AbstractUnitPipelineContext addCompleteUnit(String name, Unit unit) {
        return completePipeline().invokeAddLast(name, unit);
    }

    default AbstractUnitPipelineContext addFailureUnit(Unit unit) {
        return addFailureUnit(null, unit);
    }

    default AbstractUnitPipelineContext addFailureUnit(String name, Unit unit) {
        return failurePipeline().invokeAddLast(name, unit);
    }

    default void whenComplete(Object result) {
        log.debug("========= 开始执行completePipeline ============");
        AbstractUnitPipelineContext ctx = completePipeline();
        invokeUnitPipelineWork(ctx, result);
        log.debug("========= 结束执行completePipeline ============");
    }

    default void whenSuccess(Object result) {
        log.debug("========= 开始执行successPipeline ============");
        AbstractUnitPipelineContext ctx = successPipeline();
        invokeUnitPipelineWork(ctx, result);
        log.debug("========= 结束执行successPipeline ============");
    }

    default void whenFail(Throwable throwable) {
        log.debug("========= 开始执行failurePipeline ============");
        AbstractUnitPipelineContext ctx = failurePipeline();
        invokeUnitPipelineWork(ctx, throwable);
        log.debug("========= 结束执行failurePipeline ============");
    }

    static void invokeUnitPipelineWork(AbstractUnitPipelineContext ctx, Object o) {
        ctx.unit().work(ctx, o);
    }

}
