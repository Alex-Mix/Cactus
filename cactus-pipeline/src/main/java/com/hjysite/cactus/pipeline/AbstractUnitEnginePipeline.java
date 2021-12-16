package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultAttributeMap;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.BiPredicate;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/12/16
 **/
public abstract class AbstractUnitEnginePipeline<T extends Unit> extends DefaultAttributeMap implements UnitEnginePipeline<T> {
    private final String HEAD_NAME = UnitSequence.generateName0(HeadContext.class);
    private final String TAIL_NAME = UnitSequence.generateName0(TailContext.class);

    private final UnitEngine engine;

    private final LinkedTransferQueue<Object> resultQueue = new LinkedTransferQueue<>();

    final AbstractInvokeUnitContext head = new HeadContext(HEAD_NAME);
    final AbstractInvokeUnitContext tail = new TailContext(TAIL_NAME);

    private long size = 0;

    public AbstractUnitEnginePipeline(UnitEngine engine) {
        this.engine = engine;
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void exec(Object o) {
        head.fireWork(o);
    }

    @SuppressWarnings("unchecked")
    private AbstractInvokeUnitContext newContext(String name, T unit) {
        if (unit instanceof UnitChain) {
            return new DefaultUnitChainContext(engine, (AbstractUnitEnginePipeline<Unit>) this, name, (UnitChain<Unit>) unit);
        }
        return new DefaultInvokeUnitContext(engine, (AbstractUnitEnginePipeline<Unit>) this, name, unit);
    }

    @Override
    public AbstractInvokeUnitContext findUnitContext(String name) {
        return findUnitContext0((cur, arg) -> cur.name().equals(arg), name);
    }

    @Override
    public AbstractInvokeUnitContext findUnitContext(Unit unit) {
        return findUnitContext0((cur, arg) -> cur.unit() == arg, unit);
    }

    @Override
    public <U extends Unit> AbstractInvokeUnitContext findUnitContext(Class<U> unitType) {
        return findUnitContext0((cur, arg) -> unitType.isAssignableFrom(cur.unit().getClass()), unitType);
    }

    private AbstractInvokeUnitContext findUnitContext0(BiPredicate<AbstractInvokeUnitContext, Object> predicate, Object arg) {
        AbstractInvokeUnitContext cur = head.next;
        while (cur != tail) {
            if (predicate.test(cur, arg)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    private AbstractInvokeUnitContext getContextOrDie(String name) {
        AbstractInvokeUnitContext ctx = findUnitContext(name);
        if (ctx == null) {
            throw new NoSuchElementException(name);
        } else {
            return ctx;
        }
    }

    private AbstractInvokeUnitContext getContextOrDie(Unit unit) {
        AbstractInvokeUnitContext ctx = findUnitContext(unit);
        if (ctx == null) {
            throw new NoSuchElementException(unit.getClass().getName());
        } else {
            return ctx;
        }
    }

    private <U extends Unit> AbstractInvokeUnitContext getContextOrDie(Class<U> unitType) {
        AbstractInvokeUnitContext ctx = findUnitContext(unitType);
        if (ctx == null) {
            throw new NoSuchElementException(unitType.getName());
        } else {
            return ctx;
        }
    }

    @Override
    public AbstractInvokeUnitContext firstContext() {
        if (head.next != tail) {
            return head.next;
        }
        return null;
    }

    @Override
    public AbstractInvokeUnitContext lastContext() {
        if (head.next != tail) {
            return tail.prev;
        }
        return null;
    }

    @Override
    public AbstractUnitEnginePipeline<T> clear() {
        return null;
    }

    @Override
    public Object resultPoll() {
        return resultQueue.poll();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public synchronized AbstractUnitEnginePipeline<T> addFirst(String name, T newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractInvokeUnitContext newCtx = newContext(name, newUnit);
        addFirst0(newCtx);
        size++;
        return this;
    }

    private void addFirst0(AbstractInvokeUnitContext newCtx) {
        AbstractInvokeUnitContext next = head.next;
        newCtx.prev = head;
        newCtx.next = next;
        head.next = newCtx;
        next.prev = newCtx;
    }

    @Override
    public synchronized AbstractUnitEnginePipeline<T> addLast(String name, T newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractInvokeUnitContext newCtx = newContext(name, newUnit);
        addLast0(newCtx);
        size++;
        return this;
    }

    private void addLast0(AbstractInvokeUnitContext newCtx) {
        AbstractInvokeUnitContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }

    @Override
    public synchronized AbstractUnitEnginePipeline<T> addBefore(String baseName, String name, T newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractInvokeUnitContext ctx = getContextOrDie(baseName);
        AbstractInvokeUnitContext newCtx = newContext(name, newUnit);
        addBefore0(ctx, newCtx);
        size++;
        return this;
    }

    private static void addBefore0(AbstractInvokeUnitContext ctx, AbstractInvokeUnitContext newCtx) {
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
    }

    @Override
    public synchronized AbstractUnitEnginePipeline<T> addAfter(String baseName, String name, T newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractInvokeUnitContext ctx = getContextOrDie(baseName);
        AbstractInvokeUnitContext newCtx = newContext(name, newUnit);
        addAfter0(ctx, newCtx);
        size++;
        return this;
    }

    private static void addAfter0(AbstractInvokeUnitContext ctx, AbstractInvokeUnitContext newCtx) {
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
    }

    @Override
    public synchronized AbstractUnitEnginePipeline<T> remove(T unit) {
        remove0(getContextOrDie(unit));
        return this;
    }

    @Override
    public synchronized Unit remove(String name) {
        AbstractInvokeUnitContext ctx = getContextOrDie(name);
        remove0(ctx);
        size--;
        return ctx.unit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <U extends Unit> U remove(Class<U> unitType) {
        AbstractInvokeUnitContext ctx = getContextOrDie(unitType);
        remove0(ctx);
        size--;
        return (U) ctx.unit();
    }

    @Override
    public synchronized T removeFirst() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        Unit unit = head.next.unit();
        remove0(head.next);
        size--;
        //noinspection unchecked
        return (T) unit;
    }

    @Override
    public synchronized T removeLast() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        Unit unit = tail.prev.unit();
        remove0(tail.prev);
        size--;
        //noinspection unchecked
        return (T) unit;
    }

    private void remove0(AbstractInvokeUnitContext ctx) {
        AbstractInvokeUnitContext prev = ctx.prev;
        AbstractInvokeUnitContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
    }

    @Override
    public synchronized T replace(T oldUnit, String newName, T newUnit) {
        AbstractInvokeUnitContext oldCtx = getContextOrDie(oldUnit);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        AbstractInvokeUnitContext newCtx = newContext(newName, newUnit);
        replace0(oldCtx, newCtx);
        return oldUnit;
    }

    @Override
    public synchronized T replace(String oldName, String newName, T newUnit) {
        AbstractInvokeUnitContext oldCtx = getContextOrDie(oldName);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        AbstractInvokeUnitContext newCtx = newContext(newName, newUnit);
        replace0(oldCtx, newCtx);
        //noinspection unchecked
        return (T) oldCtx.unit();
    }

    @SuppressWarnings("unchecked")
    public synchronized <U extends Unit> U replace(Class<U> oldUnitType, String newName, T newUnit) {
        AbstractInvokeUnitContext oldCtx = getContextOrDie(oldUnitType);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        AbstractInvokeUnitContext newCtx = newContext(newName, newUnit);
        replace0(oldCtx, newCtx);
        return (U) oldCtx.unit();
    }

    private void replace0(AbstractInvokeUnitContext oldCtx, AbstractInvokeUnitContext newCtx) {
        AbstractInvokeUnitContext prev = oldCtx.prev;
        AbstractInvokeUnitContext next = oldCtx.next;
        newCtx.prev = prev;
        newCtx.next = next;
        prev.next = newCtx;
        next.prev = newCtx;

        // update the reference to the replacement so forward of buffered content will work correctly
        oldCtx.prev = newCtx;
        oldCtx.next = newCtx;

    }

    @SuppressWarnings("unchecked")
    @Override
    public T findUnit(String name) {
        return (T) Optional.ofNullable(findUnitContext(name)).map(UnitContext::unit).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findUnit(Unit unit) {
        return (T) Optional.ofNullable(findUnitContext(unit)).map(UnitContext::unit).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U extends Unit> U findUnit(Class<U> unitType) {
        return (U) Optional.ofNullable(findUnitContext(unitType)).map(UnitContext::unit).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T firstUnit() {
        return (T) Optional.ofNullable(firstContext()).map(UnitContext::unit).orElse(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T lastUnit() {
        return (T) Optional.ofNullable(lastContext()).map(UnitContext::unit).orElse(null);
    }

    @Override
    public boolean isExist(String name) {
        return findUnitContext(name) != null;
    }

    @Override
    public boolean isExist(Unit unit) {
        return findUnitContext(unit) != null;
    }

    @Override
    public <U extends Unit> boolean isExist(Class<U> unitType) {
        return findUnitContext(unitType) != null;
    }

    @Override
    public List<String> names() {
        List<String> list = new ArrayList<>();
        AbstractInvokeUnitContext ctx = head.next;
        for (; ; ) {
            if (ctx == null || ctx == tail) {
                return list;
            }
            list.add(ctx.name());
            ctx = ctx.next;
        }
    }

    @Override
    public Map<String, T> toMap() {
        Map<String, T> map = new LinkedHashMap<>();
        AbstractInvokeUnitContext cur = head.next;
        for (; ; ) {
            if (cur == tail) {
                return map;
            }
            //noinspection unchecked
            map.put(cur.name(), (T) cur.unit());
            cur = cur.next;
        }
    }

    @Override
    public Iterator<Map.Entry<String, T>> iterator() {
        return toMap().entrySet().iterator();
    }

    class HeadContext extends AbstractInvokeUnitContext implements Unit {

        @SuppressWarnings("unchecked")
        protected HeadContext(String name) {
            super(engine, (AbstractUnitEnginePipeline<Unit>) AbstractUnitEnginePipeline.this, null, name);
        }

        @Override
        public Unit unit() {
            return this;
        }
    }

    class TailContext extends AbstractInvokeUnitContext implements Unit {

        @SuppressWarnings("unchecked")
        protected TailContext(String name) {
            super(engine, (AbstractUnitEnginePipeline<Unit>) AbstractUnitEnginePipeline.this, null, name);
        }

        @Override
        public void work(InvokeUnitContext ctx, Object o) {
            if (o != null) {
                resultQueue.add(o);
            }
        }

        @Override
        public Unit unit() {
            return this;
        }
    }
}
