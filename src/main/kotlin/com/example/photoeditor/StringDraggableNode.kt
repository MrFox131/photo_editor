package com.example.photoeditor

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.util.converter.NumberStringConverter

class StringDraggableNode: DraggableNode() {
    var value: SimpleStringProperty = SimpleStringProperty()
    var editableValueLayout = HBox()
    init {
        this.name.value = "String"
        editableValueLayout.prefWidthProperty().bind(widthProperty())


    }

    val dCircle = DragCircle(value, false, this, SimpleStringProperty::class.java)
    val button = Button("Selfdestroy")

    init {
        val textField = TextField()

        Bindings.bindBidirectional(textField.textProperty(), value)

        editableValueLayout.children.addAll(textField, dCircle)
        children.addAll(editableValueLayout, button)
    }



    fun selfDestroy(it: MouseEvent) {
        LinerSinglton.deleteLine(dCircle)
        (parent as Pane).children.remove(this)
    }

    init {
        button.onMouseClicked = EventHandler{
            selfDestroy(it)
        }
    }
}