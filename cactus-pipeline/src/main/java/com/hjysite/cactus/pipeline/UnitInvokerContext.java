package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/29
 **/
public interface UnitInvokerContext extends UnitContext, UnitInvoker {

    @Override
    UnitContext prev();

    @Override
    UnitContext next();

    @Override
    UnitInvokerContext fireWork(Object o);

    @Override
    UnitInvokerContext fireExceptionCaught(Throwable throwable);

    @Override
    UnitInvokerContext fireEventTriggered(Object evt);

    @Override
    UnitInvokerContext fireWorkCompleted();
}
