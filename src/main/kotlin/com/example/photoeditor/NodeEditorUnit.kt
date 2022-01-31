package com.example.photoeditor

import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.layout.VBox
import javafx.scene.text.Text

abstract class DraggableNode : VBox() {
    var x = 0.0
    var y = 0.0
    private val EPSILON = 1.0E-14 //util constant value for comparison

    private var mousex = 0.0 //for drag_and_drop
    private var mousey = 0.0 //for drag_and_drop
    private var dragging = false //for drag_and_drop
    protected var name: SimpleStringProperty = SimpleStringProperty()

    init {
        val title = Text("")
        title.textProperty().bind(name)
        children.addAll(title)
        style = "-fx-border-color: #555"
        initialize()
    }

    private fun initialize() {
        onMousePressed = EventHandler {
            mousex = it.sceneX
            mousey = it.sceneY
        }

        onMouseDragged = EventHandler {
            val offsetX = it.sceneX - mousex
            val offsetY = it.sceneY - mousey

            x += offsetX
            y += offsetY

            layoutX = x
            layoutY = y

            dragging = true
            mousex = it.sceneX
            mousey = it.sceneY

            it.consume()
        }

        onMouseClicked = EventHandler {
            dragging = false
        }
    }
}

fun main(){

}