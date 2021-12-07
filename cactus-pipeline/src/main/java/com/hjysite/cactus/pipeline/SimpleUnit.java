package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.TypeParameterMatcher;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/26
 **/
public abstract class SimpleUnit<T> implements Unit {

    private final TypeParameterMatcher matcher;

    protected SimpleUnit() {
        matcher = TypeParameterMatcher.find(this, SimpleUnit.class, "T");
    }

    @Override
    public final void work(InvokeUnitContext ctx, Object o) {
        if (acceptInboundMessage(o)) {
            @SuppressWarnings("unchecked")
            T t = (T) o;
            work0(ctx, t);
        } else {
            ctx.fireWork(o);
        }
    }

    public abstract void work0(InvokeUnitContext ctx, T t);

    public boolean acceptInboundMessage(Object msg) {
        return matcher.match(msg);
    }

}
