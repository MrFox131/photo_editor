package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import org.w3c.dom.Node
import java.io.IOException
import javax.imageio.ImageIO

class OutputImageNode: DraggableNode() {
    val ImageView = ImageView()
    val editableValueLayout = HBox()
    val value = ImageView.imageProperty()
    val dCircle = DragCircle(value, true, this, ObjectProperty::class.java)
    val button = Button("Сохранить изображение")

    init {
        name.value = "Выходное изображкение"
        ImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        ImageView.isPreserveRatio = true
        children.addAll(ImageView, editableValueLayout, button)
    }

    init {
        editableValueLayout.children.addAll(dCircle)
    }

    init {
        button.onMouseClicked = EventHandler {
            val fc = FileChooser()
            val stage = button.scene.window
            fc.title = "Сохранить изображение"
            val file = fc.showSaveDialog(stage)
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(value.value, null), "png", file)
                } catch (_: IOException) {
                    println("Something goes wrong")
                }
            }
        }
    }
}