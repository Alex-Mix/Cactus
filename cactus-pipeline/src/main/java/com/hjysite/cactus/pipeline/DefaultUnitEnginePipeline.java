package com.hjysite.cactus.pipeline;

import com.hjysite.cactus.common.DefaultAttributeMap;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.BiPredicate;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/24
 **/
public class DefaultUnitEnginePipeline extends AbstractUnitEnginePipeline<Unit> {
    public DefaultUnitEnginePipeline(UnitEngine engine) {
        super(engine);
    }
}
