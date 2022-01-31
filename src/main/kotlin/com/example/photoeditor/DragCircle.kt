package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.input.ClipboardContent
import javafx.scene.input.TransferMode
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import java.lang.reflect.Type


class DragCircle<T>(var value_: T, var inputMode: Boolean, var parent: VBox, var dataType: Type): Circle() { // TODO:Implement line connections

    init {
        radius = 10.0
        fill = javafx.scene.paint.Color.AQUA
        onMouseDragged = EventHandler {
            it.consume()
        }
        onDragDetected = EventHandler {
            it.consume()
            if (!inputMode) {
                val ddb = startDragAndDrop(TransferMode.LINK)
                val cc = ClipboardContent()
                cc.putString("Hate this")
                ddb.setContent(cc)
            }
        }
        onDragEntered = EventHandler {
            if (inputMode && (it.gestureSource is DragCircle<*>) && ((it.gestureSource as DragCircle<*>).parent != parent)) {
                fill = javafx.scene.paint.Color.RED
            }
        }
        onDragExited = EventHandler {
            if (inputMode) {
                fill = javafx.scene.paint.Color.AQUA
            }
        }
        onDragOver = EventHandler {
            if (inputMode && (it.gestureSource is DragCircle<*>))
                it.acceptTransferModes(TransferMode.LINK)
        }
        onDragDropped = EventHandler {
            if (inputMode && (it.gestureSource as DragCircle<*>).parent != parent && (it.gestureSource as DragCircle<*>).dataType == dataType) {
                val prop = (it.gestureSource as DragCircle<*>).value_
                LinerSinglton.deleteLine(this)
                if (prop is SimpleIntegerProperty) {
                    (value_ as SimpleIntegerProperty).unbind()
                    (value_ as SimpleIntegerProperty).bind(prop)
                }
                else if (prop is SimpleFloatProperty) {
                    (value_ as SimpleFloatProperty).unbind()
                    (value_ as SimpleFloatProperty).bind(prop)
                }
                else if (prop is SimpleStringProperty) {
                    (value_ as SimpleStringProperty).unbind()
                    (value_ as SimpleStringProperty).bind(prop)
                }
                else if (prop is ObjectProperty<*>) {
                    (value_ as ObjectProperty<Image>).unbind()
                    (value_ as ObjectProperty<Image>).bind(prop as ObjectProperty<Image>)
                }
                LinerSinglton.addPair(this, it.gestureSource as DragCircle<*>)
            }
        }

    }

    fun unbindAll() {
        if (value_ is SimpleIntegerProperty) {
            (value_ as SimpleIntegerProperty).unbind()
        }
        else if (value_ is SimpleFloatProperty) {
            (value_ as SimpleFloatProperty).unbind()
        }
        else if (value_ is SimpleStringProperty) {
            (value_ as SimpleStringProperty).unbind()
        }
        else if (value_ is ObjectProperty<*>) {
            (value_ as ObjectProperty<Image>).unbind()
        }
    }
}