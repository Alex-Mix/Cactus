package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface Unit {

    default void work(UnitInvokerContext ctx, Object o) {
        ctx.fireWork(o);
    }

    default void unitAdded(UnitInvokerContext ctx) {
    }

    default void unitRemoved(UnitInvokerContext ctx) {
    }

    default void workCompleted(UnitInvokerContext ctx) {
        ctx.fireWorkCompleted();
    }

    default void exceptionCaught(UnitInvokerContext ctx, Throwable throwable) {
        ctx.fireExceptionCaught(throwable);
    }

    default void eventTriggered(UnitInvokerContext ctx, Object evt) {
        ctx.fireEventTriggered(evt);
    }
    
}
