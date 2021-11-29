package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/25
 **/
public class NoInputUnitChain extends DefaultUnitChain {

    private void checkUnitIsNoInput(Unit[] units) {
        for (Unit unit : units) {
            if (!(unit instanceof NoInputUnit)) {
                throw new IllegalArgumentException("units are not all NoInputUnit or NoInputUnitChain");
            }
        }
    }

    private void checkUnitIsNoInput(Unit unit) {
        if (!(unit instanceof NoInputUnit) && !(unit instanceof NoInputUnitChain)) {
            throw new IllegalArgumentException("this unit are not all NoInputUnit or NoInputUnitChain");
        }
    }

    @Override
    public synchronized DefaultUnitChain addFirst(Unit... units) {
        checkUnitIsNoInput(units);
        return super.addFirst(units);
    }

    @Override
    public synchronized DefaultUnitChain addFirst(Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.addFirst(newUnit);
    }

    @Override
    public synchronized DefaultUnitChain addFirst(String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.addFirst(name, newUnit);
    }

    @Override
    public synchronized DefaultUnitChain addLast(Unit... units) {
        checkUnitIsNoInput(units);
        return super.addLast(units);
    }

    @Override
    public synchronized DefaultUnitChain addLast(String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.addLast(name, newUnit);
    }

    @Override
    public synchronized DefaultUnitChain addBefore(String baseName, String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.addBefore(baseName, name, newUnit);
    }

    @Override
    public synchronized DefaultUnitChain addAfter(String baseName, String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.addAfter(baseName, name, newUnit);
    }

    @Override
    public synchronized NoInputUnit replace(Unit oldUnit, String newName, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return (NoInputUnit) super.replace(oldUnit, newName, newUnit);
    }

    @Override
    public synchronized NoInputUnit replace(String oldName, String newName, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return (NoInputUnit) super.replace(oldName, newName, newUnit);
    }

    @Override
    public synchronized <T extends Unit> T replace(Class<T> oldUnitType, String newName, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.replace(oldUnitType, newName, newUnit);
    }

}
