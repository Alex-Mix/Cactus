package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultAttributeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.BiPredicate;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/24
 **/
public class DefaultUnitEnginePipeline extends DefaultAttributeMap implements UnitEnginePipeline {

    private final static String HEAD_NAME = UnitSequence.generateName0(HeadInvokerContext.class);
    private final static String TAIL_NAME = UnitSequence.generateName0(TailInvokerContext.class);

    private final UnitEngine engine;

    private final LinkedTransferQueue<Object> resultQueue = new LinkedTransferQueue<>();

    final AbstractUnitInvokerContext head = new HeadInvokerContext(HEAD_NAME);
    final AbstractUnitInvokerContext tail = new TailInvokerContext(TAIL_NAME);

    private long size = 0;

    public DefaultUnitEnginePipeline(UnitEngine engine) {
        this.engine = engine;
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void exec(Object o) {
        head.fireWork(o);
    }

    private AbstractUnitInvokerContext newContext(String name, Unit unit) {
        if (unit instanceof UnitChain) {
            return new DefaultUnitChainInvokerContext(engine, this, name, (UnitChain) unit);
        }
        return new DefaultUnitInvokerContext(engine, this, name, unit);
    }

    @Override
    public AbstractUnitInvokerContext findUnitContext(String name) {
        return findUnitContext0((cur, arg) -> cur.name().equals(arg), name);
    }

    @Override
    public AbstractUnitInvokerContext findUnitContext(Unit unit) {
        return findUnitContext0((cur, arg) -> cur.unit() == arg, unit);
    }

    @Override
    public AbstractUnitInvokerContext findUnitContext(Class<? extends Unit> unitType) {
        return findUnitContext0((cur, arg) -> unitType.isAssignableFrom(cur.unit().getClass()), unitType);
    }

    private AbstractUnitInvokerContext findUnitContext0(BiPredicate<AbstractUnitInvokerContext, Object> predicate, Object arg) {
        AbstractUnitInvokerContext cur = head.next;
        while (cur != tail) {
            if (predicate.test(cur, arg)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    private AbstractUnitInvokerContext getContextOrDie(String name) {
        AbstractUnitInvokerContext ctx = findUnitContext(name);
        if (ctx == null) {
            throw new NoSuchElementException(name);
        } else {
            return ctx;
        }
    }

    private AbstractUnitInvokerContext getContextOrDie(Unit unit) {
        AbstractUnitInvokerContext ctx = findUnitContext(unit);
        if (ctx == null) {
            throw new NoSuchElementException(unit.getClass().getName());
        } else {
            return ctx;
        }
    }

    private AbstractUnitInvokerContext getContextOrDie(Class<? extends Unit> unitType) {
        AbstractUnitInvokerContext ctx = findUnitContext(unitType);
        if (ctx == null) {
            throw new NoSuchElementException(unitType.getName());
        } else {
            return ctx;
        }
    }

    @Override
    public AbstractUnitInvokerContext firstContext() {
        if (head.next != tail) {
            return head.next;
        }
        return null;
    }

    @Override
    public AbstractUnitInvokerContext lastContext() {
        if (head.next != tail) {
            return tail.prev;
        }
        return null;
    }

    @Override
    public DefaultUnitEnginePipeline clear() {
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
    public synchronized DefaultUnitEnginePipeline addFirst(String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractUnitInvokerContext newCtx = newContext(name, newUnit);
        addFirst0(newCtx);
        size++;
        return this;
    }

    private void addFirst0(AbstractUnitInvokerContext newCtx) {
        AbstractUnitInvokerContext next = head.next;
        newCtx.prev = head;
        newCtx.next = next;
        head.next = newCtx;
        next.prev = newCtx;
    }

    @Override
    public synchronized DefaultUnitEnginePipeline addLast(String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractUnitInvokerContext newCtx = newContext(name, newUnit);
        addLast0(newCtx);
        size++;
        return this;
    }

    private void addLast0(AbstractUnitInvokerContext newCtx) {
        AbstractUnitInvokerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }

    @Override
    public synchronized DefaultUnitEnginePipeline addBefore(String baseName, String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractUnitInvokerContext ctx = getContextOrDie(baseName);
        AbstractUnitInvokerContext newCtx = newContext(name, newUnit);
        addBefore0(ctx, newCtx);
        size++;
        return this;
    }

    private static void addBefore0(AbstractUnitInvokerContext ctx, AbstractUnitInvokerContext newCtx) {
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
    }

    @Override
    public synchronized DefaultUnitEnginePipeline addAfter(String baseName, String name, Unit newUnit) {
        name = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, name, newUnit);
        AbstractUnitInvokerContext ctx = getContextOrDie(baseName);
        AbstractUnitInvokerContext newCtx = newContext(name, newUnit);
        addAfter0(ctx, newCtx);
        size++;
        return this;
    }

    private static void addAfter0(AbstractUnitInvokerContext ctx, AbstractUnitInvokerContext newCtx) {
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
    }

    @Override
    public synchronized DefaultUnitEnginePipeline remove(Unit unit) {
        remove0(getContextOrDie(unit));
        return this;
    }

    @Override
    public synchronized Unit remove(String name) {
        AbstractUnitInvokerContext ctx = getContextOrDie(name);
        remove0(ctx);
        size--;
        return ctx.unit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T extends Unit> T remove(Class<T> unitType) {
        AbstractUnitInvokerContext ctx = getContextOrDie(unitType);
        remove0(ctx);
        size--;
        return (T) ctx.unit();
    }

    @Override
    public synchronized Unit removeFirst() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        Unit unit = head.next.unit();
        remove0(head.next);
        size--;
        return unit;
    }

    @Override
    public synchronized Unit removeLast() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        Unit unit = tail.prev.unit();
        remove0(tail.prev);
        size--;
        return unit;
    }

    private void remove0(AbstractUnitInvokerContext ctx) {
        AbstractUnitInvokerContext prev = ctx.prev;
        AbstractUnitInvokerContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
    }

    @Override
    public synchronized Unit replace(Unit oldUnit, String newName, Unit newUnit) {
        AbstractUnitInvokerContext oldCtx = getContextOrDie(oldUnit);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        AbstractUnitInvokerContext newCtx = newContext(newName, newUnit);
        replace0(oldCtx, newCtx);
        return oldUnit;
    }

    @Override
    public synchronized Unit replace(String oldName, String newName, Unit newUnit) {
        AbstractUnitInvokerContext oldCtx = getContextOrDie(oldName);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        AbstractUnitInvokerContext newCtx = newContext(newName, newUnit);
        replace0(oldCtx, newCtx);
        return oldCtx.unit();
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T extends Unit> T replace(Class<T> oldUnitType, String newName, Unit newUnit) {
        AbstractUnitInvokerContext oldCtx = getContextOrDie(oldUnitType);
        newName = UnitSequence.ifNullGenerateNameAndCheckDuplicate(this, newName, newUnit);
        AbstractUnitInvokerContext newCtx = newContext(newName, newUnit);
        replace0(oldCtx, newCtx);
        return (T) oldCtx.unit();
    }

    private void replace0(AbstractUnitInvokerContext oldCtx, AbstractUnitInvokerContext newCtx) {
        AbstractUnitInvokerContext prev = oldCtx.prev;
        AbstractUnitInvokerContext next = oldCtx.next;
        newCtx.prev = prev;
        newCtx.next = next;
        prev.next = newCtx;
        next.prev = newCtx;

        // update the reference to the replacement so forward of buffered content will work correctly
        oldCtx.prev = newCtx;
        oldCtx.next = newCtx;

    }

    @Override
    public Unit findUnit(String name) {
        return Optional.ofNullable(findUnitContext(name)).map(UnitContext::unit).orElse(null);
    }

    @Override
    public Unit findUnit(Unit unit) {
        return Optional.ofNullable(findUnitContext(unit)).map(UnitContext::unit).orElse(null);
    }

    @Override
    public Unit findUnit(Class<? extends Unit> unitType) {
        return Optional.ofNullable(findUnitContext(unitType)).map(UnitContext::unit).orElse(null);
    }

    @Override
    public Unit firstUnit() {
        return Optional.ofNullable(firstContext()).map(UnitContext::unit).orElse(null);
    }

    @Override
    public Unit lastUnit() {
        return Optional.ofNullable(lastContext()).map(UnitContext::unit).orElse(null);
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
    public boolean isExist(Class<? extends Unit> unitType) {
        return findUnitContext(unitType) != null;
    }

    @Override
    public List<String> names() {
        List<String> list = new ArrayList<String>();
        AbstractUnitInvokerContext ctx = head.next;
        for (;;) {
            if (ctx == null || ctx == tail) {
                return list;
            }
            list.add(ctx.name());
            ctx = ctx.next;
        }
    }

    class HeadInvokerContext extends AbstractUnitInvokerContext implements Unit {

        protected HeadInvokerContext(String name) {
            super(engine, DefaultUnitEnginePipeline.this, null, name);
        }

        @Override
        public Unit unit() {
            return this;
        }
    }

    class TailInvokerContext extends AbstractUnitInvokerContext implements Unit {

        protected TailInvokerContext(String name) {
            super(engine, DefaultUnitEnginePipeline.this, null, name);
        }

        @Override
        public void work(UnitInvokerContext ctx, Object o) {
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
