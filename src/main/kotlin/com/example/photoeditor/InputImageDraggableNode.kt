package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.TransferMode
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import java.io.FileInputStream

class InputImageDraggableNode: DraggableNode() {
    var imageView = ImageView()
    var imageProperty = imageView.imageProperty()
    val dCircle = DragCircle(imageProperty, false, this, ObjectProperty::class.java)
    val editableValueLayout = HBox()
    val label = Label("Перетащите сюда")

    init {
        name.value = "Input Image"
        imageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        imageView.isPreserveRatio = true
        children.addAll(imageView, editableValueLayout)
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
}