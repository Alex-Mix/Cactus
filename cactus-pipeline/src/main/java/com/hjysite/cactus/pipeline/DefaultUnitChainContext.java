package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.Attribute;
import com.hjysite.cactus.common.AttributeKey;
import com.hjysite.cactus.common.AttributeTag;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public class DefaultUnitChainContext extends AbstractInvokeUnitContext implements UnitChainContext {

    private final UnitChain<Unit> unitChain;

    protected DefaultUnitChainContext(UnitEngine engine, AbstractUnitEnginePipeline<Unit> pipeline, String name, UnitChain<Unit> unitChain) {
        super(engine, pipeline, null, name);
        this.unitChain = unitChain;
    }

    @Override
    public UnitChain<Unit> unit() {
        return unitChain;
    }

    @Override
    public DefaultUnitChainContext fireWork(Object o) {
        super.fireWork(o);
        return this;
    }

    @Override
    public DefaultUnitChainInnerContext getInnerContext(DefaultUnitChain.UnitNode unitNode) {
        return new DefaultUnitChainInnerContext(this, unitNode);
    }


    @SuppressWarnings("InnerClassMayBeStatic")
    public class DefaultUnitChainInnerContext implements UnitChainInnerContext {

        final DefaultUnitChainContext outCtx;
        final DefaultUnitChain.UnitNode unitNode;

        public DefaultUnitChainInnerContext(DefaultUnitChainContext outCtx, DefaultUnitChain.UnitNode unitNode) {
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
        public UnitEnginePipeline<Unit> pipeline() {
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
        public <T> Attribute<T> attr(AttributeTag tag, AttributeKey<T> key) {
            return outCtx.attr(tag, key);
        }

        @Override
        public <T> boolean hasAttr(AttributeTag tag, AttributeKey<T> key) {
            return outCtx.hasAttr(tag, key);
        }
    }
}
