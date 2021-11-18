package com.hjysite.cactus.common;


/**
 * @description: 持有 {@link Attribute}，且可通过{@link AttributeKey}访问
 *              实现类必须线程安全
 *              see {@link com.hjysite.cactus.common.AttributeMap}
 * @author: hjy
 * @date: 2021/3/2
 **/
public interface AttributeMap {

    /**
     * @author hjy
     * @description 直接设置Attribute持有的对象
     * @date 2021/11/2
     * @param key:
     * @param t:
     * @return 被设置的Attribute
     **/
    default <T> Attribute<T> setAttrVal(AttributeKey<T> key, T t) {
        return setAttrVal(AttributeTag.DEFAULT, key, t);
    }

    /**
     * @author hjy
     * @description 直接设置Attribute持有的对象
     * @date 2021/11/2
     * @param tag:
     * @param key:
     * @param t:
     * @return 被设置的Attribute
     **/
    default <T> Attribute<T> setAttrVal(AttributeTag tag, AttributeKey<T> key, T t) {
        Attribute<T> attr = attr(tag, key);
        attr.set(t);
        return attr;
    }
    /**
     * @author hjy
     * @description 通过 {@link AttributeKey} 获取 {@link Attribute} 中存储的对象, {@link AttributeTag} 默认传入 {@link AttributeTag#DEFAULT}
     * 但是无法通过对返回的对象重新赋值，实现对{@link Attribute}中存储对象的修改
     * @date 2021/11/2
     * @param key:
     * @return AttributeKey对应的Attribute中的储存的对象
     **/
    default <T> T attrVal(AttributeKey<T> key) {
        return attrVal(AttributeTag.DEFAULT, key);
    }

    /**
     * @author hjy
     * @description 通过 {@link AttributeKey} 和 {@link AttributeTag}获取 {@link Attribute}中存储的对象
     * 但是无法通过对返回的对象重新赋值，实现对{@link Attribute}中存储对象的修改
     * @date 2021/11/2
     * @param tag:
     * @param key:
     * @return AttributeKey对应的Attribute中的储存的对象
     **/
    default <T> T attrVal(AttributeTag tag, AttributeKey<T> key) {
        return attr(tag, key).get();
    }
    /**
     * @author hjy
     * @description 通过 {@link AttributeKey} 获取 {@link Attribute}, {@link AttributeTag} 默认传入 {@link AttributeTag#DEFAULT}
     * @date 2021/3/2
     * @param key:
     * @return: Attribute<T>
     **/
    default <T> Attribute<T> attr(AttributeKey<T> key) {
        return attr(AttributeTag.DEFAULT, key);
    }

    /**
     * @author hjy
     * @description 通过 {@link AttributeKey} 和 {@link AttributeTag}获取 {@link Attribute}
     * @date 2021/3/2
     * @param key:
     * @return: Attribute<T>
     **/
    <T> Attribute<T> attr(AttributeTag tag, AttributeKey<T> key);


    /**
     * @author hjy
     * @description 判断 {@link AttributeMap}是否存在{@link AttributeKey}, {@link AttributeTag} 默认传入 {@link AttributeTag#DEFAULT}
     * @date 2021/3/2
     * @param key:
     * @return: boolean {@code true} 如果 {@link AttributeMap}中存在{@link Attribute}。
     **/
    default <T> boolean hasAttr(AttributeKey<T> key) {
        return hasAttr(AttributeTag.DEFAULT, key);
    }

    /**
     * @author hjy
     * @description 判断 {@link AttributeMap}是否存在{@link AttributeKey}和 {@link AttributeTag}
     * @date 2021/3/2
     * @param key:
     * @return: boolean {@code true} 如果 {@link AttributeMap}中存在{@link Attribute}。
     **/
    <T> boolean hasAttr(AttributeTag tag, AttributeKey<T> key);
}
