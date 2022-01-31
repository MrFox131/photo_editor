package com.example.photoeditor

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.util.converter.NumberStringConverter

class FloatDraggableNode: DraggableNode() {
    var value: SimpleFloatProperty = SimpleFloatProperty()
    var editableValueLayout = HBox()
    init {
        this.name.value = "Float"
        editableValueLayout.prefWidthProperty().bind(widthProperty())
    }

    val dCircle = DragCircle(value, false, this, SimpleFloatProperty::class.java)
    val button = Button("Selfdestroy")


    init {
        val textField = TextField()
        textField.textProperty().addListener { _, _, newValue ->
            if (!newValue.matches(Regex("[\\d,]*"))) {
                textField.text = newValue.replace(Regex("[^\\d,]"), "");
            }
        }



        Bindings.bindBidirectional(textField.textProperty(), value, NumberStringConverter())

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