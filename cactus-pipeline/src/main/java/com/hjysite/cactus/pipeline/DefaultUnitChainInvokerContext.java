package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.Attribute;
import com.hjysite.cactus.common.AttributeKey;
import com.hjysite.cactus.common.AttributeTag;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public class DefaultUnitChainInvokerContext extends AbstractUnitInvokerContext implements UnitChainContext {

    private final UnitChain unitChain;

    protected DefaultUnitChainInvokerContext(UnitEngine engine, UnitEnginePipeline pipeline, String name, UnitChain unitChain) {
        super(engine, pipeline, null, name);
        this.unitChain = unitChain;
    }

    @Override
    public UnitChain unit() {
        return unitChain;
    }

    @Override
    public DefaultUnitChainInvokerContext fireWork(Object o) {
        super.fireWork(o);
        return this;
    }

    @Override
    public DefaultUnitChainInvokerContext fireExceptionCaught(Throwable throwable) {
        super.fireExceptionCaught(throwable);
        return this;
    }

    @Override
    public DefaultUnitChainInvokerContext fireEventTriggered(Object evt) {
        super.fireEventTriggered(evt);
        return this;
    }

    @Override
    public DefaultUnitChainInvokerContext fireWorkCompleted() {
        super.fireWorkCompleted();
        return this;
    }

    @Override
    public DefaultUnitChainInnerContext getInnerContext(DefaultUnitChain.UnitNode unitNode) {
        return new DefaultUnitChainInnerContext(this, unitNode);
    }


    @SuppressWarnings("InnerClassMayBeStatic")
    public class DefaultUnitChainInnerContext implements UnitChainInnerContext {

        final DefaultUnitChainInvokerContext outCtx;
        final DefaultUnitChain.UnitNode unitNode;

        public DefaultUnitChainInnerContext(DefaultUnitChainInvokerContext outCtx, DefaultUnitChain.UnitNode unitNode) {
            this.outCtx = outCtx;
            this.unitNode = unitNode;
        }

        @Override
        public DefaultUnitChainInnerContext prev() {
            return outCtx.getInnerContext(unitNode.prev);
        }

        @Override
        public DefaultUnitChainInnerContext next() {
            return outCtx.getInnerContext(unitNode.next);
        }

        @Override
        public Unit unit() {
            return unitNode.unit;
        }

        @Override
        public String name() {
            return unitNode.name;
        }

        @Override
        public UnitEngine engine() {
            return outCtx.engine();
        }

        @Override
        public UnitEnginePipeline pipeline() {
            return outCtx.pipeline();
        }

        @Override
        public UnitChainContext chainContext() {
            return outCtx;
        }

        @Override
        public DefaultUnitChainInnerContext fireWork(Object o) {
            DefaultUnitChainInnerContext nextCtx = next();
            nextCtx.unit().work(nextCtx, o);
            return this;
        }

        @Override
        public DefaultUnitChainInnerContext fireExceptionCaught(Throwable throwable) {
            DefaultUnitChainInnerContext nextCtx = next();
            nextCtx.unit().exceptionCaught(nextCtx, throwable);
            return this;
        }

        @Override
        public DefaultUnitChainInnerContext fireEventTriggered(Object evt) {
            DefaultUnitChainInnerContext nextCtx = next();
            nextCtx.unit().eventTriggered(nextCtx, evt);
            return this;
        }

        @Override
        public DefaultUnitChainInnerContext fireWorkCompleted() {
            DefaultUnitChainInnerContext nextCtx = next();
            nextCtx.unit().workCompleted(nextCtx);
            return this;
        }

        @Override
        public <T> Attribute<T> attr(AttributeTag tag, AttributeKey<T> key) {
            return outCtx.attr(tag, key);
        }

        @Override
        public <T> boolean hasAttr(AttributeTag tag, AttributeKey<T> key) {
            return outCtx.hasAttr(tag, key);
        }
    }
}
