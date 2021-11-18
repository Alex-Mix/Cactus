package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public interface Unit {

    default void work(UnitContext<?> ctx, Object o) {
        ctx.fireWork(o);
    }

    default void unitAdded(UnitContext<?> ctx) {
    }

    default void unitRemoved(UnitContext<?> ctx) {
    }

    default void workCompleted(UnitContext<?> ctx) {
        ctx.fireWorkCompleted();
    }

    default void exceptionCaught(UnitContext<?> ctx, Throwable throwable) {
        ctx.fireExceptionCaught(throwable);
    }

    default void eventTriggered(UnitContext<?> ctx, Object evt) {
        ctx.fireEventTriggered(evt);
    }
}
