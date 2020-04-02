package com.dpashko.sandbox.scene.test;

import com.dpashko.sandbox.scene.BaseScene;

import javax.inject.Inject;

public class TestScene extends BaseScene<TestSceneController> {

    private TestSceneController controller = new TestSceneController();

    @Inject
    TestScene(final TestSceneController controller) {
        super(controller);
    }

    @Override
    public void dispose() {
        controller.dispose();
    }
}
