package com.hjysite.cactus.common;

/**
 * @description: 线程不安全
 * @author: hjy
 * @date: 2021/3/8
 **/
public class DefaultAttributeMap implements AttributeMap {

    private static final int BUCKET_SIZE = 4;
    private static final int MASK = BUCKET_SIZE - 1;

    private volatile DefaultAttribute<?>[] attributes;

    @SuppressWarnings("unchecked")
    @Override
    public <T> Attribute<T> attr(AttributeTag tag, AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }

        if (tag == null) {
            throw new NullPointerException("tag");
        }

        if (this.attributes == null) {
            this.attributes = new DefaultAttribute<?>[BUCKET_SIZE];
        }

        int i = index(key);
        DefaultAttribute<?> head = attributes[i];
        if (head == null) {
            // 还没有头节点，这意味着我们可以在不同步的情况下添加属性，而只需使用比较和设置即可。
            head = new DefaultAttribute<>();
            DefaultAttribute<T> attr = new DefaultAttribute<>(head, key);
            head.next = attr;
            attr.prev = head;
            attributes[i] = head;
            return attr;
        }

        DefaultAttribute<?> curr = head;
        for (; ; ) {
            DefaultAttribute<?> next = curr.next;
            if (next == null) {
                DefaultAttribute<T> attr = new DefaultAttribute<>(head, key, tag);
                curr.next = attr;
                attr.prev = curr;
                return attr;
            }

            // 根据AttributeKey和AttributeTag确定一个Attribute
            if (next.key == key && next.tag == tag && !next.removed) {
                return (Attribute<T>) next;
            }
            curr = next;
        }
    }

    @Override
    public <T> boolean hasAttr(AttributeTag tag, AttributeKey<T> key) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        DefaultAttribute<?>[] attributes = this.attributes;
        if (attributes == null) {
            return false;
        }

        int i = index(key);
        DefaultAttribute<?> head = attributes[i];
        if (head == null) {
            return false;
        }

        DefaultAttribute<?> curr = head.next;
        while (curr != null) {
            if (curr.key == key && curr.tag == tag && !curr.removed) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private static int index(AttributeKey<?> key) {
        return key.id() & MASK;
    }

    @SuppressWarnings("serial")
    private static final class DefaultAttribute<T> implements Attribute<T> {

        private boolean immutable = false;

        private static final long serialVersionUID = -2661411462200283011L;

        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;

        private DefaultAttribute<?> prev;
        private DefaultAttribute<?> next;

        private AttributeTag tag;

        // 调用 {@link DefaultAttribute#remove}/{@link #getAndRemove} 时设置为true
        private volatile boolean removed;

        private T value;

        DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key) {
            this(head, key, AttributeTag.DEFAULT);
        }

        public DefaultAttribute(DefaultAttribute<?> head, AttributeKey<T> key, AttributeTag tag) {
            this.head = head;
            this.key = key;
            this.tag = tag;
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
        public T get() {
            return this.value;
        }

        @Override
        public void set(T value) {
            assertImmutable();
            this.value = value;
        }

        @Override
        public T getAndSet(T value) {
            assertImmutable();
            T old = this.value;
            this.value = value;
            return old;
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
        public boolean compareAndSet(T oldValue, T newValue) {
            assertImmutable();
            if (oldValue == this.value) {
                this.value = newValue;
                return true;
            }
            return false;
        }

        @Override
        public void remove() {
            removed = true;
            set(null);
            remove0();
        }

        @Override
        public boolean immutable() {
            return immutable;
        }

        @Override
        public Attribute<T> toImmutable() {
            immutable = true;
            return this;
        }

        private void remove0() {
            if (prev == null) {
                // 之前移除了
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
