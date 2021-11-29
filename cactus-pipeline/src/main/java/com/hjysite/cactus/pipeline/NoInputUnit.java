package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/25
 **/
public abstract class NoInputUnit implements Unit {
    @Override
    public final void work(UnitInvokerContext ctx, Object o) {
        noInputWork(ctx);
        ctx.fireWork(o);
    }

    abstract void noInputWork(UnitContext ctx);
}
