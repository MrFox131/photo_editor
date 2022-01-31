package com.example.photoeditor

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.util.converter.NumberStringConverter
import java.util.*
import kotlin.concurrent.schedule

class IntegerDraggableNode() : DraggableNode() {
    var value: SimpleIntegerProperty = SimpleIntegerProperty()
    var editableValueLayout = HBox()
    init {
        this.name.value = "Integer"
        editableValueLayout.prefWidthProperty().bind(widthProperty())


    }

    val dCircle = DragCircle(value, false, this, SimpleIntegerProperty::class.java)
    val button = Button("Selfdestroy")

    init {
        val textField = TextField()
        textField.textProperty().addListener { _, _, newValue ->
            if (!newValue.matches(Regex("\\d*"))) {
                textField.text = newValue.replace(Regex("[^\\d]"), "");
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
