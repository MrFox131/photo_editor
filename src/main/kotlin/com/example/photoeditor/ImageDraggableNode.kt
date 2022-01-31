package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import java.io.FileInputStream

class ImageDraggableNode: DraggableNode() {
    var imageView = ImageView()
    var imageProperty = imageView.imageProperty()
    val dCircle = DragCircle(imageProperty, false, this, ObjectProperty::class.java)
    val editableValueLayout = HBox()
    val label = Label("Перетащите картинку сюда")

    val button = Button("Selfdestroy")

    init {
        name.value = "Картинка"

        imageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        imageView.isPreserveRatio = true

        children.addAll(imageView, editableValueLayout, button)
    }

    init {

        label.onDragOver = EventHandler {
            it.acceptTransferModes(*TransferMode.ANY)
        }
        label.onDragDropped = EventHandler {
            if (it.dragboard.hasFiles()) {
                imageView.image = Image(FileInputStream(it.dragboard.files[0]))
            }
        }
    }

    init {
        editableValueLayout.children.addAll(label, dCircle)
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