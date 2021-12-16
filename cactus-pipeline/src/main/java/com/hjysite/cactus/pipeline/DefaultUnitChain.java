package com.hjysite.cactus.pipeline;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public class DefaultUnitChain implements UnitChain {

    private static final String HEAD_NAME = UnitSequence.generateName0(HeadNode.class);
    private static final String TAIL_NAME = UnitSequence.generateName0(TailNode.class);

    private final UnitNode head = new UnitNode(HEAD_NAME, new HeadNode());
    private final UnitNode tail = new UnitNode(TAIL_NAME, new TailNode());
    private long size;

    public DefaultUnitChain() {
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void work(InvokeUnitContext ctx, Object o) {
        UnitChainContext.UnitChainInnerContext innerCtx = ((UnitChainContext) ctx).getInnerContext(head);
        head.unit.work(innerCtx, o);
    }

    @Override
    public synchronized DefaultUnitChain addFirst(Unit... units) {
        UnitChain.super.addFirst(units);
        return this;
    }

    @Override
    public synchronized DefaultUnitChain addFirst(Unit newUnit) {
        UnitChain.super.addFirst(newUnit);
        return this;
    }

    @Override
    public synchronized DefaultUnitChain addFirst(String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        UnitNode newNode = new UnitNode(name, newUnit);
        addFirst0(newNode);
        size++;
        return this;
    }

    private void addFirst0(UnitNode newNode) {
        UnitNode next = head.next;
        newNode.prev = head;
        newNode.next = next;
        head.next = newNode;
        next.prev = newNode;
    }

    @Override
    public synchronized DefaultUnitChain addLast(Unit... units) {
        UnitChain.super.addLast(units);
        return this;
    }

    @Override
    public synchronized DefaultUnitChain addLast(String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        UnitNode newNode = new UnitNode(name, newUnit);
        addLast0(newNode);
        size++;
        return this;
    }

    private void addLast0(UnitNode newNode) {
        UnitNode prev = tail.prev;
        newNode.prev = prev;
        newNode.next = tail;
        prev.next = newNode;
        tail.prev = newNode;
    }

    @Override
    public synchronized DefaultUnitChain addBefore(String baseName, String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        UnitNode node = getNodeOrDie(baseName);
        UnitNode newNode = new UnitNode(name, newUnit);
        addBefore0(node, newNode);
        size++;
        return this;
    }

    private static void addBefore0(UnitNode node, UnitNode newNode) {
        newNode.prev = node.prev;
        newNode.next = node;
        node.prev.next = newNode;
        node.prev = newNode;
    }

    @Override
    public synchronized DefaultUnitChain addAfter(String baseName, String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        UnitNode node = getNodeOrDie(baseName);
        UnitNode newNode = new UnitNode(name, newUnit);
        addAfter0(node, newNode);
        size++;
        return this;
    }

    private static void addAfter0(UnitNode node, UnitNode newNode) {
        newNode.prev = node;
        newNode.next = node.next;
        node.next.prev = newNode;
        node.next = newNode;
    }

    @Override
    public synchronized DefaultUnitChain remove(Unit unit) {
        remove0(getNodeOrDie(unit));
        size--;
        return this;
    }

    @Override
    public synchronized Unit remove(String name) {
        UnitNode node = getNodeOrDie(name);
        remove0(node);
        size--;
        return node.unit;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T extends Unit> T remove(Class<T> unitType) {
        UnitNode node = getNodeOrDie(unitType);
        remove0(node);
        size--;
        return (T) node.unit;
    }

    @Override
    public synchronized Unit removeFirst() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        Unit unit = head.next.unit;
        remove0(head.next);
        size--;
        return unit;
    }

    @Override
    public synchronized Unit removeLast() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        Unit unit = tail.prev.unit;
        remove0(tail.prev);
        size--;
        return unit;
    }

    private void remove0(UnitNode node) {
        UnitNode prev = node.prev;
        UnitNode next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    @Override
    public synchronized Unit replace(Unit oldUnit, String newName, Unit newUnit) {
        UnitNode oldNode = getNodeOrDie(oldUnit);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        UnitNode newNode = new UnitNode(newName, newUnit);
        replace0(oldNode, newNode);
        return oldUnit;
    }

    @Override
    public synchronized Unit replace(String oldName, String newName, Unit newUnit) {
        UnitNode oldNode = getNodeOrDie(oldName);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        UnitNode newNode = new UnitNode(newName, newUnit);
        replace0(oldNode, newNode);
        return oldNode.unit;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T extends Unit> T replace(Class<T> oldUnitType, String newName, Unit newUnit) {
        UnitNode oldNode = getNodeOrDie(oldUnitType);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        UnitNode newNode = new UnitNode(newName, newUnit);
        replace0(oldNode, newNode);
        return (T) oldNode.unit;
    }

    private void replace0(UnitNode node, UnitNode newNode) {
        UnitNode prev = node.prev;
        UnitNode next = node.next;
        newNode.prev = prev;
        newNode.next = next;
        prev.next = newNode;
        next.prev = newNode;

        // update the reference to the replacement so forward of buffered content will work correctly
        node.prev = newNode;
        node.next = newNode;

    }

    @Override
    public Unit findUnit(String name) {
        return Optional.ofNullable(findNode(name)).map(node -> node.unit).orElse(null);
    }

    @Override
    public Unit findUnit(Unit unit) {
        return Optional.ofNullable(findNode(unit)).map(node -> node.unit).orElse(null);
    }

    @Override
    public Unit findUnit(Class<? extends Unit> unitType) {
        return Optional.ofNullable(findNode(unitType)).map(node -> node.unit).orElse(null);
    }

    @Override
    public Unit firstUnit() {
        return head.next != tail ? head.next.unit : null;
    }

    @Override
    public Unit lastUnit() {
        return head.next != tail ? tail.prev.unit : null;
    }

    @Override
    public boolean isExist(String name) {
        return findNode(name) != null;
    }

    @Override
    public boolean isExist(Unit unit) {
        return findNode(unit) != null;
    }

    @Override
    public boolean isExist(Class<? extends Unit> unitType) {
        return findNode(unitType) != null;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public List<String> names() {
        List<String> list = new ArrayList<>();
        UnitNode node = head.next;
        for (;;) {
            if (node == null || node == tail) {
                return list;
            }
            list.add(node.name);
            node = node.next;
        }
    }

    @Override
    public Map<String, Unit> toMap() {
        Map<String, Unit> map = new LinkedHashMap<>();
        UnitNode cur = head.next;
        while (cur != tail) {
            map.put(cur.name, cur.unit);
            cur = cur.next;
        }
        return map;
    }

    public UnitNode findNode(String name) {
        return findNode0((cur, arg) -> cur.name.equals(arg), name);
    }

    public UnitNode findNode(Unit unit) {
        return findNode0((cur, arg) -> cur.unit == arg, unit);
    }

    public UnitNode findNode(Class<? extends Unit> unitType) {
        return findNode0((cur, arg) -> unitType.isAssignableFrom(cur.unit.getClass()), unitType);
    }

    private UnitNode findNode0(BiPredicate<UnitNode, Object> predicate, Object arg) {
        UnitNode cur = head.next;
        while (cur != tail) {
            if (predicate.test(cur, arg)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }


    private UnitNode getNodeOrDie(String name) {
        UnitNode node = findNode(name);
        if (node == null) {
            throw new NoSuchElementException(name);
        } else {
            return node;
        }
    }

    private UnitNode getNodeOrDie(Unit unit) {
        UnitNode node = findNode(unit);
        if (node == null) {
            throw new NoSuchElementException(unit.getClass().getName());
        } else {
            return node;
        }
    }

    private UnitNode getNodeOrDie(Class<? extends Unit> unitType) {
        UnitNode node = findNode(unitType);
        if (node == null) {
            throw new NoSuchElementException(unitType.getName());
        } else {
            return node;
        }
    }

    @Override
    public Iterator<Map.Entry<String, Unit>> iterator() {
        return toMap().entrySet().iterator();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    final class UnitNode {
        volatile UnitNode prev;
        volatile UnitNode next;
        final String name;
        final Unit unit;

        UnitNode(String name, Unit unit) {
            this.name = name;
            this.unit = unit;
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    final class HeadNode implements Unit {
        @Override
        public void work(InvokeUnitContext ctx, Object o) {
            ctx.fireWork(o);
        }
    }


    @SuppressWarnings("InnerClassMayBeStatic")
    final class TailNode implements Unit {
        @Override
        public void work(InvokeUnitContext ctx, Object o) {
            ctx.chainContext().fireWork(o);
        }
    }
}
