package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.Attribute;
import com.hjysite.cactus.common.AttributeKey;
import com.hjysite.cactus.common.AttributeMap;
import com.hjysite.cactus.common.AttributeTag;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/22
 **/
public interface UnitContext extends AttributeMap {
    
    UnitContext prev();

    UnitContext next();

    Unit unit();

    String name();

    UnitEngine engine();

    UnitEnginePipeline pipeline();

    UnitChainContext chainContext();

    default <T> Attribute<T> setChainAttrVal(AttributeKey<T> key, T t) {
        return setChainAttrVal(AttributeTag.DEFAULT, key, t);
    }

    default <T> Attribute<T> setChainAttrVal(AttributeTag tag, AttributeKey<T> key, T t) {
        return chainContext().setAttrVal(tag, key, t);
    }
    default <T> T chainAttrVal(AttributeKey<T> key) {
        return chainAttrVal(AttributeTag.DEFAULT, key);
    }

    default <T> T chainAttrVal(AttributeTag tag, AttributeKey<T> key) {
        return chainContext().attrVal(tag, key);
    }

    default <T> Attribute<T> setPipelineAttrVal(AttributeKey<T> key, T t) {
        return setPipelineAttrVal(AttributeTag.DEFAULT, key, t);
    }

    default <T> Attribute<T> setPipelineAttrVal(AttributeTag tag, AttributeKey<T> key, T t) {
        return pipeline().setAttrVal(tag, key, t);
    }
    default <T> T pipelineAttrVal(AttributeKey<T> key) {
        return pipelineAttrVal(AttributeTag.DEFAULT, key);
    }

    default <T> T pipelineAttrVal(AttributeTag tag, AttributeKey<T> key) {
        return pipeline().attrVal(tag, key);
    }

    default <T> Attribute<T> setEngineAttrVal(AttributeKey<T> key, T t) {
        return setEngineAttrVal(AttributeTag.DEFAULT, key, t);
    }

    default <T> Attribute<T> setEngineAttrVal(AttributeTag tag, AttributeKey<T> key, T t) {
        return engine().setAttrVal(tag, key, t);
    }
    default <T> T engineAttrVal(AttributeKey<T> key) {
        return engineAttrVal(AttributeTag.DEFAULT, key);
    }

    default <T> T engineAttrVal(AttributeTag tag, AttributeKey<T> key) {
        return engine().attrVal(tag, key);
    }
}
