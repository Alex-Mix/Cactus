package com.hjysite.cactus.common;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/3/8
 **/
public class DefaultConcurrentAttributeMap implements AttributeMap {
    @SuppressWarnings("rawtypes")
    private static final AtomicReferenceFieldUpdater<DefaultConcurrentAttributeMap, AtomicReferenceArray> updater =
            AtomicReferenceFieldUpdater.newUpdater(DefaultConcurrentAttributeMap.class, AtomicReferenceArray.class, "attributes");

    private static final int BUCKET_SIZE = 4;
    private static final int MASK = BUCKET_SIZE - 1;

    // 延迟初始化减少内存占用
    @SuppressWarnings("UnusedDeclaration")
    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;

    @SuppressWarnings("unchecked")
    @Override
    public <T> Attribute<T> attr(AttributeTag tag, AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            // 由于占用大量内存，因此不使用ConcurrentHashMap。
            attributes = new AtomicReferenceArray<>(BUCKET_SIZE);

            if (!updater.compareAndSet(this, null, attributes)) {
                attributes = this.attributes;
            }
        }

        int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            // 还没有头节点，这意味着我们可以在不同步的情况下添加属性，而只需使用比较和设置即可。最差的情况，我们需要回退到同步并浪费两个分配。
            head = new DefaultAttribute<>();
            DefaultAttribute<T> attr = new DefaultAttribute<>(head, key);
            head.next = attr;
            attr.prev = head;
            if (attributes.compareAndSet(i, null, head)) {
                return attr;
            } else {
                head = attributes.get(i);
            }
        }

        synchronized (head) {
            DefaultAttribute<?> curr = head;
            for (; ; ) {
                DefaultAttribute<?> next = curr.next;
                if (next == null) {
                    DefaultAttribute<T> attr = new DefaultAttribute<>(head, key);
                    curr.next = attr;
                    attr.prev = curr;
                    return attr;
                }

                if (next.key == key && next.tag == tag && !next.removed) {
                    return (Attribute<T>) next;
                }
                curr = next;
            }
        }
    }

    @Override
    public <T> boolean hasAttr(AttributeTag tag, AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray<DefaultAttribute<?>> attributes = this.attributes;
        if (attributes == null) {
            return false;
        }

        int i = index(key);
        DefaultAttribute<?> head = attributes.get(i);
        if (head == null) {
            return false;
        }

        // 通过头节点同步，从头节点开始查找
        synchronized (head) {
            DefaultAttribute<?> curr = head.next;
            while (curr != null) {
                if (curr.key == key && curr.tag == tag && !curr.removed) {
                    return true;
                }
                curr = curr.next;
            }
            return false;
        }
    }

    private static int index(AttributeKey<?> key) {
        return key.id() & MASK;
    }

    @SuppressWarnings("serial")
    private static final class DefaultAttribute<T> extends AtomicReference<T> implements Attribute<T> {

        private static final long serialVersionUID = -2661411462200283011L;

        private AtomicBoolean immutable = new AtomicBoolean(false);

        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;

        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;

        private AttributeTag tag;
        // 调用 {@link DefaultAttribute#remove}/{@link #getAndRemove} 时设置为true
        private volatile boolean removed;

        DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key) {
            this.head = head;
            this.key = key;
        }

        // 主要用于头节点
        DefaultAttribute() {
            head = this;
            key = null;
        }

        @Override
        public AttributeKey<T> key() {
            return key;
        }

        @Override
        public T setIfAbsent(T value) {
            assertImmutable();
            while (!compareAndSet(null, value)) {
                T old = get();
                if (old != null) {
                    return old;
                }
            }
            return null;
        }

        @Override
        public T getAndRemove() {
            removed = true;
            T oldValue = getAndSet(null);
            remove0();
            return oldValue;
        }

        @Override
        public void remove() {
            removed = true;
            set(null);
            remove0();
        }

        @Override
        public boolean immutable() {
            return immutable.get();
        }

        @Override
        public Attribute<T> toImmutable() {
            if (immutable()) {
                immutable.compareAndSet(false, true);
            }
            return this;
        }

        private void remove0() {
            synchronized (head) {
                if (prev == null) {
                    // 之前已经被移除
                    return;
                }

                prev.next = next;

                if (next != null) {
                    next.prev = prev;
                }

                // 设置为null，防止多次调用remove0破坏链表结构
                prev = null;
                next = null;
            }
        }
    }
}
