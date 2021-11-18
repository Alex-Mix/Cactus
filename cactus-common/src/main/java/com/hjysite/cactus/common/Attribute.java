package com.hjysite.cactus.common;

import java.util.Optional;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/3/2
 **/
public interface Attribute<T> {

    /**
     * Returns the key of this attribute.
     */
    AttributeKey<T> key();

    /**
     * Returns the current value, which may be {@code null}
     */
    T get();

    default Optional<T> getNullable() {
        return Optional.ofNullable(get());
    }

    /**
     * Sets the value
     */
    void set(T value);

    /**
     *  Atomically sets to the given value and returns the old value which may be {@code null} if non was set before.
     */
    T getAndSet(T value);

    /**
     *  Atomically sets to the given value if this {@link com.hjysite.cactus.common.Attribute}'s value is {@code null}.
     *  If it was not possible to set the value as it contains a value it will just return the current value.
     */
    T setIfAbsent(T value);

    @Deprecated
    T getAndRemove();

    /**
     * Atomically sets the value to the given updated value if the current value == the expected value.
     * If it the set was successful it returns {@code true} otherwise {@code false}.
     */
    boolean compareAndSet(T oldValue, T newValue);

    @Deprecated
    void remove();

    /**
     * @author hjy
     * @description 该Attribute中的value的引用是否是不可变的（对象内部属性的引用仍然可变）
     * @date 2021/9/1
     * @return {@code true} 该Attribute中的值的引用不可变，此时调用任何可能修改value引用的方法都会抛出异常{@link IllegalStateException}, 但是仍然可以调用{@link #remove}/{@link #getAndRemove}
     *         否则 {@code false}.
     **/
    boolean immutable();

    /**
     * @author hjy
     * @description 该Attribute中的值的引用变为不可变的
     * @date 2021/9/1
     **/
    Attribute<T> toImmutable();

    default void assertImmutable() {
        if (immutable()) {
            throw new IllegalStateException("this attribute is immutable");
        }
    }
}
