package com.hjysite.cactus.pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/26
 **/
public class ParallelUnitEnginePipeline extends NoInputUnitEnginePipeline {

    private final boolean joinFlag;
    private final Executor workers;

    public ParallelUnitEnginePipeline(UnitEngine engine, boolean joinFlag, Executor workers) {
        super(engine);
        this.joinFlag = joinFlag;
        this.workers = workers;
    }

    @Override
    public void exec() {
        Map<String, CompletableFuture<?>> unitFutureMap = new HashMap<>();
        for (AbstractUnitInvokerContext cur = firstContext(); cur != tail; cur = cur.next()) {
            AbstractUnitInvokerContext ctx = cur;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> ((NoInputUnit) ctx.unit()).noInputWork(ctx), workers);
            unitFutureMap.put(ctx.name(), future);
        }
        CompletableFuture<Void> allResultFuture = CompletableFuture.allOf(unitFutureMap.values().toArray(new CompletableFuture[0]));
        if (joinFlag) {
            allResultFuture.join();
        }
    }
}
