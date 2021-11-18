package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public interface UnitInvoker {

    UnitInvoker fireWork(Object o);

    UnitInvoker fireExceptionCaught(Throwable throwable);

    UnitInvoker fireEventTriggered(Object evt);

    UnitInvoker fireWorkCompleted();
}
