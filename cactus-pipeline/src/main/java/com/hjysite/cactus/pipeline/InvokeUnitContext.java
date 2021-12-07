package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/29
 **/
public interface InvokeUnitContext extends UnitContext, UnitInvoker {

    @Override
    UnitContext prev();

    @Override
    UnitContext next();

    @Override
    InvokeUnitContext fireWork(Object o);

    @Override
    InvokeUnitContext fireExceptionCaught(Throwable throwable);

    @Override
    InvokeUnitContext fireEventTriggered(Object evt);

    @Override
    InvokeUnitContext fireWorkCompleted();
}
