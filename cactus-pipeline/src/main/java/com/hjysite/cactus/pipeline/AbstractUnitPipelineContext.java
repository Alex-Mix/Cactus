package com.hjysite.cactus.pipeline;

import java.util.List;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/17
 **/
public abstract class AbstractUnitPipelineContext extends AbstractUnitContext<UnitPipeline> implements UnitPipelineInvoker {

    protected AbstractUnitPipelineContext(UnitEngine engine, UnitContext<? extends UnitPipeline> holder, String name, UnitPipeline unit) {
        super(engine, holder, name, unit);
    }

    @Override
    public AbstractUnitPipelineContext invokeAddFirst(Unit newUnit) {
        unit().addFirst(this, newUnit);
        return this;
    }

    @Override
    public AbstractUnitPipelineContext invokeAddFirst(String name, Unit newUnit) {
        unit().addFirst(this, newUnit);
        return this;
    }

    @Override
    public AbstractUnitPipelineContext invokeAddLast(Unit newUnit) {
        unit().addLast(this, newUnit);
        return this;
    }

    @Override
    public AbstractUnitPipelineContext invokeAddLast(String name, Unit newUnit) {
        unit().addLast(this, name, newUnit);
        return this;
    }

    @Override
    public AbstractUnitPipelineContext invokeAddBefore(String baseName, String name, Unit newUnit) {
        unit().addBefore(this, baseName, name, newUnit);
        return this;
    }

    @Override
    public AbstractUnitPipelineContext invokeAddAfter(String baseName, String name, Unit newUnit) {
        unit().addAfter(this, baseName, name, newUnit);
        return this;
    }

    @Override
    public Unit invokeRemove(Unit unit) {
        return unit().remove(unit);
    }

    @Override
    public Unit invokeRemove(String name) {
        return unit().remove(name);
    }

    @Override
    public <T extends Unit> T invokeRemove(Class<T> unitType) {
        return unit().remove(unitType);
    }

    @Override
    public Unit invokeRemoveFirst() {
        return unit().removeFirst();
    }

    @Override
    public Unit invokeRemoveLast() {
        return unit().removeLast();
    }

    @Override
    public Unit invokeReplace(Unit oldUnit, String newName, Unit newUnit) {
        return unit().replace(this, oldUnit, newName, newUnit);
    }

    @Override
    public Unit invokeReplace(String oldName, String newName, Unit newUnit) {
        return unit().replace(this, oldName, newName, newUnit);
    }

    @Override
    public <T extends Unit> T invokeReplace(Class<T> oldUnitType, String newName, Unit newUnit) {
        return unit().replace(this, oldUnitType, newName, newUnit);
    }

    @Override
    public AbstractUnitContext<?> invokeFindUnit(String name) {
        return unit().findUnit(name);
    }

    @Override
    public AbstractUnitContext<?> invokeFindUnit(Unit unit) {
        return unit().findUnit(unit);
    }

    @Override
    public AbstractUnitContext<?> invokeFindUnit(Class<? extends Unit> unitType) {
        return unit().findUnit(unitType);
    }

    @Override
    public Unit first() {
//        return unit().first();
        return null;
    }

    @Override
    public Unit last() {
//        return unit().last();
        return null;
    }

    @Override
    public boolean isExist(String name) {
        return unit().isExist(name);
    }

    @Override
    public boolean isExist(Unit unit) {
        return unit().isExist(unit);
    }

    @Override
    public <T extends Unit> boolean isExist(Class<T> unitType) {
        return unit().isExist(unitType);
    }

    @Override
    public AbstractUnitPipelineContext clear() {
        unit().clear();
        return this;
    }

    @Override
    public List<String> getUnitNames() {
        return unit().getUnitNames();
    }

    @Override
    public Object invokeResultPoll() {
        return unit().resultPoll();
    }
}
