package com.hjysite.cactus.pipeline;

/**
 * @description: TODO
 * @author: hjy
 * @date: 2021/11/26
 **/
public class NoInputUnitEnginePipeline extends AbstractUnitEnginePipeline<NoInputUnit> {
    public NoInputUnitEnginePipeline(UnitEngine engine) {
        super(engine);
    }

    @Override
    public final void exec(Object o) {
        exec();
    }

    public void exec() {
        super.exec(null);
    }
}
