package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat

class InvertDraggableNode: DraggableNode() {
    val InputImageView = ImageView()
    val OutputImageView = ImageView()
    val inputImage: ObjectProperty<Image> = InputImageView.imageProperty()
    val outputImage: ObjectProperty<Image> = OutputImageView.imageProperty()
    val inputdCircle = DragCircle(inputImage, true, this, ObjectProperty::class.java)
    val outputdCircle = DragCircle(outputImage, false, this, ObjectProperty::class.java)
    val editableValueLayout = HBox()

    init {
        name.value = "Negative filter"

        OutputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        OutputImageView.isPreserveRatio = true

        children.addAll(OutputImageView, editableValueLayout)
    }

    val button = Button("Selfdestroy")

    init {
        inputImage.addListener { _, _, newValue ->
            val ocvImage = OpenCVUtils.image2Mat(newValue)
            Core.bitwise_not(ocvImage, ocvImage)
            outputImage.value = OpenCVUtils.mat2Image(ocvImage)
        }

        editableValueLayout.children.addAll(inputdCircle, outputdCircle, button)
    }



    fun selfDestroy(it: MouseEvent) {
        LinerSinglton.deleteLine(inputdCircle)
        LinerSinglton.deleteLine(outputdCircle)
        (parent as Pane).children.remove(this)
    }

    init {
        button.onMouseClicked = EventHandler{
            selfDestroy(it)
        }
    }
}