package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/26
 **/
public class NoInputUnitEnginePipeline extends DefaultUnitEnginePipeline {
    public NoInputUnitEnginePipeline(UnitEngine engine) {
        super(engine);
    }

    @Override
    public final void exec(Object o) {
        exec();
    }

    public void exec() {
        super.exec(null);
    }

    @Override
    public synchronized NoInputUnitEnginePipeline addFirst(String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        super.addFirst(name, newUnit);
        return this;
    }

    @Override
    public synchronized NoInputUnitEnginePipeline addLast(String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        super.addLast(name, newUnit);
        return this;
    }

    @Override
    public synchronized NoInputUnitEnginePipeline addBefore(String baseName, String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        super.addBefore(baseName, name, newUnit);
        return this;
    }

    @Override
    public synchronized NoInputUnitEnginePipeline addAfter(String baseName, String name, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        super.addAfter(baseName, name, newUnit);
        return this;
    }

    @Override
    public synchronized Unit replace(Unit oldUnit, String newName, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.replace(oldUnit, newName, newUnit);
    }

    @Override
    public synchronized Unit replace(String oldName, String newName, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.replace(oldName, newName, newUnit);
    }

    @Override
    public synchronized <T extends Unit> T replace(Class<T> oldUnitType, String newName, Unit newUnit) {
        checkUnitIsNoInput(newUnit);
        return super.replace(oldUnitType, newName, newUnit);
    }

    @Override
    public synchronized UnitEnginePipeline addFirst(Unit unit) {
        checkUnitIsNoInput(unit);
        return super.addFirst(unit);
    }

    @Override
    public synchronized UnitEnginePipeline addFirst(Unit... units) {
        checkUnitIsNoInput(units);
        return super.addFirst(units);
    }

    @Override
    public synchronized UnitEnginePipeline addLast(Unit unit) {
        checkUnitIsNoInput(unit);
        return super.addLast(unit);
    }

    @Override
    public synchronized UnitEnginePipeline addLast(Unit... units) {
        checkUnitIsNoInput(units);
        return super.addLast(units);
    }

    private void checkUnitIsNoInput(Unit[] units) {
        for (Unit unit : units) {
            if (!(unit instanceof NoInputUnit) && !(unit instanceof NoInputUnitChain)) {
                throw new IllegalArgumentException("units are not all NoInputUnit");
            }
        }
    }

    private void checkUnitIsNoInput(Unit unit) {
        if (!(unit instanceof NoInputUnit)) {
            throw new IllegalArgumentException("this unit are not all NoInputUnit");
        }
    }
}
