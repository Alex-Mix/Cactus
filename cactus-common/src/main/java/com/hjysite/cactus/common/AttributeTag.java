package com.hjysite.cactus.common;

/**
 * @description: 和 {@link AttributeKey} 共同确定一个 {@link Attribute},
 * 可以通过{@link AttributeTag}在同一个{@link AttributeMap} 中实现逻辑分区，而不用实例化多个{@link AttributeMap}
 * @author: hjy
 * @date: 2021/9/1
 **/
public final class AttributeTag extends AbstractConstant<AttributeTag> {

    private static final ConstantPool<AttributeTag> pool = new ConstantPool<AttributeTag>() {
        @Override
        protected AttributeTag newConstant(int id, String name) {
            return new AttributeTag(id, name);
        }
    };

    public static final AttributeTag DEFAULT = AttributeTag.valueOf("DEFAULT");

    public static AttributeTag valueOf(String name) {
        return pool.valueOf(name);
    }

    public static boolean exists(String name) {
        return pool.exists(name);
    }

    public static AttributeTag newInstance(String name) {
        return pool.newInstance(name);
    }

    public static AttributeTag valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return pool.valueOf(firstNameComponent, secondNameComponent);
    }

    protected AttributeTag(int id, String name) {
        super(id, name);
    }
}
