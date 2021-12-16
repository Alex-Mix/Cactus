package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/25
 **/
public interface NoInputUnit extends Unit {
    @Override
    default void work(InvokeUnitContext ctx, Object o) {
        noInputWork(ctx);
        ctx.fireWork(o);
    }

    void noInputWork(UnitContext ctx);
}
