package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitChain<T extends Unit> extends Unit, UnitSequence<T> {

    @Override
    default UnitChain<T> addFirst(T newUnit) {
        return addFirst(null, newUnit);
    }

    @Override
    default UnitChain<T> addFirst(T[] units) {
        UnitSequence.super.addFirst(units);
        return this;
    }

    @Override
    UnitChain<T> addFirst(String name, T newUnit);

    @Override
    default UnitChain<T> addLast(T newUnit) {
        return addLast(null, newUnit);
    }

    @Override
    default UnitChain<T> addLast(T[] units) {
        UnitSequence.super.addLast(units);
        return this;
    }

    @Override
    UnitChain<T> addLast(String name, T newUnit);

    @Override
    UnitChain<T> addBefore(String baseName, String name, T newUnit);

    @Override
    UnitChain<T> addAfter(String baseName, String name, T newUnit);

    @Override
    UnitChain<T> remove(T unit);
}
