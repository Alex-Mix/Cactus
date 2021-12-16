package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.AttributeMap;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitEnginePipeline<T extends Unit> extends AttributeMap, UnitSequence<T> {

    void exec(Object o);

    @Override
    default UnitEnginePipeline<T> addFirst(T unit) {
        return addFirst(null, unit);
    }

    @Override
    default UnitEnginePipeline<T> addFirst(T... units) {
        UnitSequence.super.addFirst(units);
        return this;
    }

    @Override
    UnitEnginePipeline<T> addFirst(String name, T newUnit);

    @Override
    default UnitEnginePipeline<T> addLast(T unit) {
        return addLast(null, unit);
    }

    @Override
    default UnitEnginePipeline<T> addLast(T... units) {
        UnitSequence.super.addLast(units);
        return this;
    }

    @Override
    UnitEnginePipeline<T> addLast(String name, T newUnit);

    @Override
    UnitEnginePipeline<T> addBefore(String baseName, String name, T newUnit);

    @Override
    UnitEnginePipeline<T> addAfter(String baseName, String name, T newUnit);

    @Override
    UnitEnginePipeline<T> remove(T unit);

    AbstractInvokeUnitContext findUnitContext(String name);

    AbstractInvokeUnitContext findUnitContext(Unit unit);

    <U extends Unit> AbstractInvokeUnitContext findUnitContext(Class<U> unitType);

    AbstractInvokeUnitContext firstContext();

    AbstractInvokeUnitContext lastContext();

    UnitEnginePipeline<T> clear();

    Object resultPoll();
}
