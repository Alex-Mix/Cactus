package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.AttributeMap;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitEnginePipeline extends AttributeMap, UnitSequence {

    void exec(Object o);

    @Override
    default UnitEnginePipeline addFirst(Unit unit) {
        return addFirst(null, unit);
    }

    @Override
    default UnitEnginePipeline addFirst(Unit... units) {
        UnitSequence.super.addFirst(units);
        return this;
    }

    @Override
    UnitEnginePipeline addFirst(String name, Unit newUnit);

    @Override
    default UnitEnginePipeline addLast(Unit unit) {
        return addLast(null, unit);
    }

    @Override
    default UnitEnginePipeline addLast(Unit... units) {
        UnitSequence.super.addLast(units);
        return this;
    }

    @Override
    UnitEnginePipeline addLast(String name, Unit newUnit);

    @Override
    UnitEnginePipeline addBefore(String baseName, String name, Unit newUnit);

    @Override
    UnitEnginePipeline addAfter(String baseName, String name, Unit newUnit);

    @Override
    UnitEnginePipeline remove(Unit unit);

    AbstractUnitInvokerContext findUnitContext(String name);

    AbstractUnitInvokerContext findUnitContext(Unit unit);

    AbstractUnitInvokerContext findUnitContext(Class<? extends Unit> unitType);

    AbstractUnitInvokerContext firstContext();

    AbstractUnitInvokerContext lastContext();

    UnitEnginePipeline clear();

    Object resultPoll();
}
