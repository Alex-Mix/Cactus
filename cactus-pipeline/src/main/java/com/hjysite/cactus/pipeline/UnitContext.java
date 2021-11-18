package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.AttributeMap;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public interface UnitContext<T extends Unit> extends AttributeMap, UnitInvoker {

    UnitContext<?> prev();

    UnitContext<?> next();

    T unit();

    String name();

    UnitEngine engine();

    UnitContext<? extends UnitPipeline> holder();

    @Override
    UnitContext<?> fireWork(Object o);

    @Override
    UnitContext<?> fireExceptionCaught(Throwable throwable);

    @Override
    UnitContext<?> fireEventTriggered(Object evt);

    @Override
    UnitContext<?> fireWorkCompleted();
}
