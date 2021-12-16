package com.hjysite.cactus.pipeline;

import java.util.List;
import java.util.Map;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/23
 **/
public interface UnitSequence<T extends Unit> extends Iterable<Map.Entry<String, T>> {

    default UnitSequence<T> addFirst(T unit) {
        return addFirst(null, unit);
    }

    default UnitSequence<T> addFirst(T... units) {
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
            T unit = units[i];
            addFirst(unit);
        }

        return this;
    }

    UnitSequence<T> addFirst(String name, T newUnit);

    default UnitSequence<T> addLast(T unit) {
        return addLast(null, unit);
    }

    default UnitSequence<T> addLast(T... units) {
        for (T u : units) {
            if (u == null) {
                break;
            }
            addLast(u);
        }

        return this;
    }

    UnitSequence<T> addLast(String name, T newUnit);

    UnitSequence<T> addBefore(String baseName, String name, T newUnit);

    UnitSequence<T> addAfter(String baseName, String name, T newUnit);

    UnitSequence<T> remove(T unit);

    Unit remove(String name);

    <U extends Unit> U remove(Class<U> unitType);

    T removeFirst();

    T removeLast();

    T replace(T oldUnit, String newName, T newUnit);

    Unit replace(String oldName, String newName, T newUnit);

    <U extends Unit> U replace(Class<U> oldUnitType, String newName, T newUnit);

    T findUnit(String name);

    T findUnit(T unit);

    <U extends Unit> U findUnit(Class<U> unitType);

    T firstUnit();

    T lastUnit();

    boolean isExist(String name);

    boolean isExist(T unit);

    <U extends Unit> boolean isExist(Class<U> unitType);

    long size();

    List<String> names();

    Map<String, T> toMap();

    static String ifNullGenerateNameAndCheckDuplicate(UnitSequence<?> unitSequence, String name, Unit unit) {
        if (name == null) {
            return generateName(unitSequence, unit);
        }
        checkDuplicateName(unitSequence, name);
        return name;
    }

    static void checkDuplicateName(UnitSequence<?> unitSequence, String name) {
        if (unitSequence.isExist(name)) {
            throw new IllegalArgumentException("this name is exist in this pipeline, name: " + name);
        }
    }

    static String generateName(UnitSequence<?> unitSequence, Unit unit) {
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
