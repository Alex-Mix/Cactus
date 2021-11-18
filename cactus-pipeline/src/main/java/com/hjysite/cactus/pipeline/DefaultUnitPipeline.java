package com.hjysite.cactus.pipeline;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/16
 **/
public class DefaultUnitPipeline implements UnitPipeline {

    private static final ThreadLocal<Map<Class<?>, String>> nameCaches = ThreadLocal.withInitial(WeakHashMap::new);

    private static final String HEAD_NAME = generateName0(HeadUnitContext.class);
    private static final String TAIL_NAME = generateName0(TailUnitContext.class);

    final AbstractUnitContext<?> head = new HeadUnitContext();
    final AbstractUnitContext<?> tail = new TailUnitContext();

    private final LinkedTransferQueue<Object> results = new LinkedTransferQueue<>();

    private AbstractUnitContext<?> newContext(UnitContext<? extends UnitPipeline> ctx, String name, Unit newUnit) {
        if (newUnit instanceof UnitPipeline) {
            return new DefaultUnitPipelineContext(ctx.engine(), ctx, name, (UnitPipeline) newUnit);
        } else {
            return new DefaultUnitContext(ctx.engine(), ctx, name, newUnit);
        }
    }

    @Override
    public void work(UnitContext<?> ctx, Object o) {
        head.unit().work(head, o);
    }

    @Override
    public UnitPipeline addFirst(UnitContext<? extends UnitPipeline> ctx, String name, Unit newUnit) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(newUnit);

        name = ifNullGenerateNameAndCheckDuplicate(name, newUnit);

        AbstractUnitContext<?> newCtx = newContext(ctx, name, newUnit);
        addFirst0(newCtx);

        callUnitAdded0(newCtx);
        return this;
    }

    private void addFirst0(AbstractUnitContext<?> newCtx) {
        AbstractUnitContext<?> nextCtx = head.next;
        newCtx.prev = head;
        newCtx.next = nextCtx;
        head.next = newCtx;
        nextCtx.prev = newCtx;
    }

    @Override
    public UnitPipeline addLast(UnitContext<? extends UnitPipeline> ctx, String name, Unit newUnit) {
        Objects.requireNonNull(ctx);
        Objects.requireNonNull(newUnit);

        name = ifNullGenerateNameAndCheckDuplicate(name, newUnit);

        AbstractUnitContext<?> newCtx = newContext(ctx, name, newUnit);
        addLast0(newCtx);
        callUnitAdded0(newCtx);
        return this;
    }

    private void addLast0(AbstractUnitContext<?> newCtx) {
        AbstractUnitContext<?> prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }

    @Override
    public UnitPipeline addBefore(UnitContext<? extends UnitPipeline> ctx, String baseName, String name, Unit newUnit) {
        Objects.requireNonNull(newUnit);

        name = ifNullGenerateNameAndCheckDuplicate(name, newUnit);

        AbstractUnitContext<?> baseUnit = getUnitContextOrDie(baseName);
        AbstractUnitContext<?> newCtx = newContext(ctx, name, newUnit);
        addBefore0(baseUnit, newCtx);
        callUnitAdded0(newCtx);
        return this;
    }

    private void addBefore0(AbstractUnitContext<?> baseUnit, AbstractUnitContext<?> newCtx) {
        newCtx.prev = baseUnit.prev;
        newCtx.next = baseUnit;
        baseUnit.prev.next = newCtx;
        baseUnit.prev = newCtx;
    }

    @Override
    public UnitPipeline addAfter(UnitContext<? extends UnitPipeline> ctx, String baseName, String name, Unit newUnit) {
        Objects.requireNonNull(newUnit);

        name = ifNullGenerateNameAndCheckDuplicate(name, newUnit);

        AbstractUnitContext<?> baseUnit = getUnitContextOrDie(baseName);
        AbstractUnitContext<?> newCtx = newContext(ctx, name, newUnit);
        addAfter0(baseUnit, newCtx);
        callUnitAdded0(newCtx);
        return this;
    }

    private void addAfter0(AbstractUnitContext<?> baseCtx, AbstractUnitContext<?> newCtx) {
        newCtx.prev = baseCtx;
        newCtx.next = baseCtx.next;
        baseCtx.next.prev = newCtx;
        baseCtx.next = newCtx;
    }

    @Override
    public Unit remove(Unit unit) {
        return remove(getUnitContextOrDie(unit)).unit();
    }

    @Override
    public Unit remove(String name) {
        return remove(getUnitContextOrDie(name)).unit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Unit> T remove(Class<T> unitType) {
        return (T) remove(getUnitContextOrDie(unitType)).unit();
    }

    private AbstractUnitContext<?> remove(final AbstractUnitContext<?> ctx) {
        assert ctx != head && ctx != tail;

//        synchronized (this) {
//            atomicRemoveFromHandlerList(ctx);
//        }
        callUnitRemoved0(ctx);
        return ctx;
    }

    @Override
    public Unit removeFirst() {
        if (head.next == tail) {
            return null;
        }
        return remove(head.next).unit();
    }

    @Override
    public Unit removeLast() {
        if (head.next == tail) {
            return null;
        }
        return remove(tail.prev).unit();
    }

    @Override
    public Unit replace(UnitContext<? extends UnitPipeline> ctx, Unit oldUnit, String newName, Unit newUnit) {
        return null;
    }

    @Override
    public Unit replace(UnitContext<? extends UnitPipeline> ctx, String oldName, String newName, Unit newUnit) {
        return null;
    }

    @Override
    public <T extends Unit> T replace(UnitContext<? extends UnitPipeline> ctx, Class<T> oldUnitType, String newName, Unit newUnit) {
        return null;
    }

    @Override
    public AbstractUnitContext<?> findUnit(String name) {
        return null;
    }

    @Override
    public AbstractUnitContext<?> findUnit(Unit unit) {
        return null;
    }

    @Override
    public AbstractUnitContext<?> findUnit(Class<? extends Unit> unitType) {
        return null;
    }

    @Override
    public AbstractUnitContext<?> firstContext() {
        AbstractUnitContext<?> first = head.next;
        if (first == tail) {
            return null;
        }
        return head.next;
    }

    @Override
    public UnitContext<?> first() {
        AbstractUnitContext<?> first = firstContext();
        return first;
    }

    @Override
    public AbstractUnitContext<?> lastContext() {
        AbstractUnitContext<?> last = tail.prev;
        if (last == head) {
            return null;
        }
        return last;
    }

    @Override
    public UnitContext<?> last() {
        AbstractUnitContext<?> last = tail.prev;
        if (last == head) {
            return null;
        }
        return last;
    }

    @Override
    public boolean isExist(String name) {
        return false;
    }

    @Override
    public boolean isExist(Unit unit) {
        return false;
    }

    @Override
    public <T extends Unit> boolean isExist(Class<T> unitType) {
        return false;
    }

    @Override
    public UnitPipeline clear() {
        return null;
    }

    @Override
    public List<String> getUnitNames() {
        return null;
    }

    @Override
    public Object resultPoll() {
        return results.poll();
    }

    private String ifNullGenerateNameAndCheckDuplicate(String name, Unit unit) {
        if (name == null) {
            return generateName(unit);
        }
        checkDuplicateName(name);
        return name;
    }

    private void checkDuplicateName(String name) {
        if (isExist(name)) {
            throw new IllegalArgumentException("this name is exist in this pipeline, name: " + name);
        }
    }

    private String generateName(Unit unit) {
        Map<Class<?>, String> cache = nameCaches.get();
        Class<?> handlerType = unit.getClass();
        String name = cache.computeIfAbsent(handlerType, k -> generateName0(handlerType));

        if (findUnit(name) != null) {
            String baseName = name.substring(0, name.length() - 1); // 去除最后一位，比如： '0'.
            for (int i = 1; ; i++) {
                String newName = baseName + i;
                if (findUnit(newName) == null) {
                    name = newName;
                    break;
                }
            }
        }
        return name;
    }

    private static String generateName0(Class<?> unitType) {
        return unitType.getSimpleName() + "#0";
    }

    private AbstractUnitContext<?> getUnitContextOrDie(String name) {
        AbstractUnitContext<?> target = findUnit(name);
        if (target == null) {
            throw new NoSuchElementException(name);
        } else {
            return target;
        }
    }

    private AbstractUnitContext<?> getUnitContextOrDie(Unit unit) {
        AbstractUnitContext<?> target = findUnit(unit);
        if (target == null) {
            throw new NoSuchElementException(unit.getClass().getName());
        } else {
            return target;
        }
    }

    private AbstractUnitContext<?> getUnitContextOrDie(Class<? extends Unit> unitType) {
        AbstractUnitContext<?> target = findUnit(unitType);
        if (target == null) {
            throw new NoSuchElementException(unitType.getName());
        } else {
            return target;
        }
    }

    private AbstractUnitContext<?> findUnit0(String name) {
        AbstractUnitContext<?> cur = head.next;
        while (cur != tail) {
            if (cur.name().equals(name)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    private void callUnitAdded0(final AbstractUnitContext<?> ctx) {

    }

    private void callUnitRemoved0(final AbstractUnitContext<?> ctx) {

    }

    final class HeadUnitContext extends DefaultUnitContext implements Unit {
        HeadUnitContext() {
            super(null, null, HEAD_NAME, null);
        }

        @Override
        public Unit unit() {
            return this;
        }

    }

    final class TailUnitContext extends DefaultUnitContext implements Unit {
        TailUnitContext() {
            super(null, null, TAIL_NAME, null);
        }

        @Override
        public void work(UnitContext<?> ctx, Object o) {
            if (ctx.holder().holder() != null) {
                ctx.holder().holder().fireWork(o);
            } else {
                results.add(o);
            }
        }

        @Override
        public Unit unit() {
            return this;
        }
    }
}
