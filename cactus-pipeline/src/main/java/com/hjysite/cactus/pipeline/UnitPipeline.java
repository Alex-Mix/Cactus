package com.hjysite.cactus.pipeline;

import java.util.List;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public interface UnitPipeline extends Unit {

    default UnitPipeline addFirst(UnitContext<? extends UnitPipeline> ctx, Unit newUnit) {
        return addFirst(ctx, null, newUnit);
    }

    UnitPipeline addFirst(UnitContext<? extends UnitPipeline> ctx, String name, Unit newUnit);

    default UnitPipeline addLast(UnitContext<? extends UnitPipeline> ctx, Unit newUnit) {
        return addLast(ctx, null, newUnit);
    }

    UnitPipeline addLast(UnitContext<? extends UnitPipeline> ctx, String name, Unit newUnit);

    UnitPipeline addBefore(UnitContext<? extends UnitPipeline> ctx, String baseName, String name, Unit newUnit);

    UnitPipeline addAfter(UnitContext<? extends UnitPipeline> ctx, String baseName, String name, Unit newUnit);

    Unit remove(Unit unit);

    Unit remove(String name);

    <T extends Unit> T remove(Class<T> unitType);

    Unit removeFirst();

    Unit removeLast();

    Unit replace(UnitContext<? extends UnitPipeline> ctx, Unit oldUnit, String newName, Unit newUnit);

    Unit replace(UnitContext<? extends UnitPipeline> ctx, String oldName, String newName, Unit newUnit);

    <T extends Unit> T replace(UnitContext<? extends UnitPipeline> ctx, Class<T> oldUnitType, String newName, Unit newUnit);

    AbstractUnitContext<?> findUnit(String name);

    AbstractUnitContext<?> findUnit(Unit unit);

    AbstractUnitContext<?> findUnit(Class<? extends Unit> unitType);

    AbstractUnitContext<?> firstContext();

    UnitContext<?> first();

    AbstractUnitContext<?> lastContext();

    UnitContext<?> last();

    boolean isExist(String name);

    boolean isExist(Unit unit);

    <T extends Unit> boolean isExist(Class<T> unitType);

    UnitPipeline clear();

    List<String> getUnitNames();

    Object resultPoll();

}
