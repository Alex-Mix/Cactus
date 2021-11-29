package com.hjysite.cactus.pipeline;

import java.util.List;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/23
 **/
public interface UnitSequence {

    default UnitSequence addFirst(Unit unit) {
        return addFirst(null, unit);
    }

    default UnitSequence addFirst(Unit... units) {
        if (units.length == 0 || units[0] == null) {
            return this;
        }

        int size;
        for (size = 1; size < units.length; size++) {
            if (units[size] == null) {
                break;
            }
        }

        for (int i = size - 1; i >= 0; i--) {
            Unit unit = units[i];
            addFirst(unit);
        }

        return this;
    }

    UnitSequence addFirst(String name, Unit newUnit);

    default UnitSequence addLast(Unit unit) {
        return addLast(null, unit);
    }

    default UnitSequence addLast(Unit... units) {
        for (Unit u : units) {
            if (u == null) {
                break;
            }
            addLast(u);
        }

        return this;
    }

    UnitSequence addLast(String name, Unit newUnit);

    UnitSequence addBefore(String baseName, String name, Unit newUnit);

    UnitSequence addAfter(String baseName, String name, Unit newUnit);

    UnitSequence remove(Unit unit);

    Unit remove(String name);

    <T extends Unit> T remove(Class<T> unitType);

    Unit removeFirst();

    Unit removeLast();

    Unit replace(Unit oldUnit, String newName, Unit newUnit);

    Unit replace(String oldName, String newName, Unit newUnit);

    <T extends Unit> T replace(Class<T> oldUnitType, String newName, Unit newUnit);

    Unit findUnit(String name);

    Unit findUnit(Unit unit);

    Unit findUnit(Class<? extends Unit> unitType);

    Unit firstUnit();

    Unit lastUnit();

    boolean isExist(String name);

    boolean isExist(Unit unit);

    boolean isExist(Class<? extends Unit> unitType);

    long size();

    List<String> names();

    static String ifNullGenerateNameAndCheckDuplicate(UnitSequence unitSequence, String name, Unit unit) {
        if (name == null) {
            return generateName(unitSequence, unit);
        }
        checkDuplicateName(unitSequence, name);
        return name;
    }

    static void checkDuplicateName(UnitSequence unitSequence, String name) {
        if (unitSequence.isExist(name)) {
            throw new IllegalArgumentException("this name is exist in this pipeline, name: " + name);
        }
    }

    static String generateName(UnitSequence unitSequence, Unit unit) {
        Class<? extends Unit> handlerType = unit.getClass();
        String name = generateName0(handlerType);

        if (unitSequence.findUnit(name) != null) {
            String baseName = name.substring(0, name.length() - 1);// 去除最后一位，比如： '0'.
            for (int i = 1; ; i++) {
                String newName = baseName + i;
                if (unitSequence.findUnit(newName) == null) {
                    name = newName;
                    break;
                }
            }
        }
        return name;
    }

    static String generateName0(Class<?> unitType) {
        return unitType.getName() + "#0";
    }
}
