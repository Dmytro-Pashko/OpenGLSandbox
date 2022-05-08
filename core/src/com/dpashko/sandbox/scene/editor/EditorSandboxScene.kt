package com.dpashko.sandbox.scene.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.dpashko.sandbox.scene.SandboxScene

class EditorSandboxScene(skin: Skin) : SandboxScene, ChangeListener() {

    private var stage = Stage()
    private var table = Table(skin)
    private var gridSizeSelectBox = SelectBox<GridSize>(skin)
    private var gridSizeLabel = Label("Grid size: ", skin)
    private var fpsLabel = Label("FPS : ", skin)
    private var fpsValue = Label("", skin)
    private var drawGridCheckBox = CheckBox("Draw grid", skin)
    private var drawAxisCheckBox = CheckBox("Draw axis", skin)
    private var wireFrameModeCheckBox = CheckBox("Wireframe Mode", skin)
    private val controller = EditorSceneController()

    override fun init() {
        stage.apply {
            addActor(initUI(controller.state))
            Gdx.input.inputProcessor = this
        }
        controller.init()
    }

    private fun initUI(state: EditorSceneState) =
            table.apply {
                add(Table().apply {
                    right()
                    add(drawGridCheckBox.apply {
                        isChecked = state.isDrawGrid
                        addListener(this@EditorSandboxScene)
                    }).padLeft(10f)
                    add(gridSizeLabel)
                            .padLeft(10f)
                    add(gridSizeSelectBox.apply {
                        items = Array(GridSize.values())
                        selected = state.gridSize
                        addListener(this@EditorSandboxScene)
                    })
                    add(wireFrameModeCheckBox.apply {
                        isChecked = state.isWireframeMode
                        addListener(this@EditorSandboxScene)
                    }).padLeft(10f)
                    add(drawAxisCheckBox.apply {
                        isChecked = state.isDrawAxis
                        addListener(this@EditorSandboxScene)
                    }).padLeft(10f).padRight(10f)
                }).fillX().align(Align.top).expand()
                row()
                add(Table().apply {
                    right()
                    add(fpsLabel).padRight(10f)
                    add(fpsValue).padRight(10f)
                }).fillX().align(Align.bottom).expand()
                setFillParent(true)
            }

    override fun draw() {
        controller.draw()
        stage.act(Gdx.graphics.deltaTime)
        fpsValue.setText("${Gdx.graphics.framesPerSecond}")
        stage.draw()
    }

    override fun dispose() {
        controller.dispose()
        stage.dispose()
    }

    override fun changed(event: ChangeEvent, actor: Actor) {
        when (actor) {
            gridSizeSelectBox -> controller.onGridSizeChanged(gridSizeSelectBox.selected)
            drawAxisCheckBox -> controller.onDrawAxisChanged(drawAxisCheckBox.isChecked)
            drawGridCheckBox -> {
                gridSizeSelectBox.isDisabled = !drawGridCheckBox.isChecked
                controller.onDrawGridChanged(drawGridCheckBox.isChecked)
            }
            wireFrameModeCheckBox -> controller.onWireframeModeChanged(wireFrameModeCheckBox.isChecked)
        }
    }
}
