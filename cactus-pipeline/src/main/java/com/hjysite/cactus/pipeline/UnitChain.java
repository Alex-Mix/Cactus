package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitChain extends Unit, UnitSequence {

    @Override
    default UnitChain addFirst(Unit newUnit) {
        return addFirst(null, newUnit);
    }

    @Override
    default UnitChain addFirst(Unit... units) {
        UnitSequence.super.addFirst(units);
        return this;
    }

    @Override
    UnitChain addFirst(String name, Unit newUnit);

    @Override
    default UnitChain addLast(Unit newUnit) {
        return addLast(null, newUnit);
    }

    @Override
    default UnitChain addLast(Unit... units) {
        UnitSequence.super.addLast(units);
        return this;
    }

    @Override
    UnitChain addLast(String name, Unit newUnit);

    @Override
    UnitChain addBefore(String baseName, String name, Unit newUnit);

    @Override
    UnitChain addAfter(String baseName, String name, Unit newUnit);

    @Override
    UnitChain remove(Unit unit);
}
