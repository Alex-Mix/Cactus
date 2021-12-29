package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface Unit {

    default void work(InvokeUnitContext ctx, Object o) {
        ctx.fireWork(o);
    }

    default void unitAdded(InvokeUnitContext ctx) {
    }

    default void unitRemoved(InvokeUnitContext ctx) {
    }
    
}
