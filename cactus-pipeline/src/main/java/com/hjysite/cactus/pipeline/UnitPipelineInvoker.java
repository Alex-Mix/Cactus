package com.hjysite.cactus.pipeline;

import java.util.List;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/17
 **/
public interface UnitPipelineInvoker {
    UnitPipelineInvoker invokeAddFirst(Unit newUnit);

    UnitPipelineInvoker invokeAddFirst(String name, Unit newUnit);

    UnitPipelineInvoker invokeAddLast(Unit newUnit);

    UnitPipelineInvoker invokeAddLast(String name, Unit newUnit);

    UnitPipelineInvoker invokeAddBefore(String baseName, String name, Unit newUnit);

    UnitPipelineInvoker invokeAddAfter(String baseName, String name, Unit newUnit);

    Unit invokeRemove(Unit unit);

    Unit invokeRemove(String name);

    <T extends Unit> T invokeRemove(Class<T> unitType);

    Unit invokeRemoveFirst();

    Unit invokeRemoveLast();

    Unit invokeReplace(Unit oldUnit, String newName, Unit newUnit);

    Unit invokeReplace(String oldName, String newName, Unit newUnit);

    <T extends Unit> T invokeReplace(Class<T> oldUnitType, String newName, Unit newUnit);

    AbstractUnitContext<?> invokeFindUnit(String name);

    AbstractUnitContext<?> invokeFindUnit(Unit unit);

    AbstractUnitContext<?> invokeFindUnit(Class<? extends Unit> unitType);

    Unit first();

    Unit last();

    boolean isExist(String name);

    boolean isExist(Unit unit);

    <T extends Unit> boolean isExist(Class<T> unitType);

    UnitPipelineInvoker clear();

    List<String> getUnitNames();

    Object invokeResultPoll();
}
