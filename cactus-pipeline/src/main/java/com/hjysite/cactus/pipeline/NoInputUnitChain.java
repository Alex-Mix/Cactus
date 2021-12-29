package com.hjysite.cactus.pipeline;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/12/16
 **/
@Slf4j
public class NoInputUnitChain extends AbstractUnitChain<NoInputUnit> implements NoInputUnit {

    @Override
    public void noInputWork(UnitContext ctx) {
        // work() will call AbstractUnitChain::work
        // noop
    }
}
