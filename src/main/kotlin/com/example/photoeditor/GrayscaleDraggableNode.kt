package com.example.photoeditor

import javafx.scene.image.Image
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ObjectPropertyBase
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import org.opencv.imgproc.Imgproc

class GrayscaleDraggableNode: DraggableNode() {
    val InputImageView = ImageView()
    val OutputImageView = ImageView()
    val inputImage: ObjectProperty<Image> = InputImageView.imageProperty()
    val outputImage: ObjectProperty<Image> = OutputImageView.imageProperty()
    val inputdCircle = DragCircle(inputImage, true, this, ObjectProperty::class.java)
    val outputdCircle = DragCircle(outputImage, false, this, ObjectProperty::class.java)
    val editableValueLayout = HBox()
    val button = Button("Selfdestroy")

    init {
        name.value = "Grayscale filter"
        children.addAll(editableValueLayout)
    }

    init {
        inputImage.addListener { _, _, newValue ->
            var ocvMat = OpenCVUtils.image2Mat(newValue)
            Imgproc.cvtColor(ocvMat, ocvMat, Imgproc.COLOR_RGB2GRAY)
            Imgproc.cvtColor(ocvMat, ocvMat, Imgproc.COLOR_GRAY2RGB)
            outputImage.value = OpenCVUtils.mat2Image(ocvMat)
        }

        OutputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        OutputImageView.isPreserveRatio = true

        editableValueLayout.children.addAll(OutputImageView, inputdCircle, outputdCircle, button)
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