package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleFloatProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import org.opencv.core.Mat

class BrightnessDraggableNode: DraggableNode() {
    val brightnessLevel = SimpleFloatProperty()
    val inputImageView = ImageView()
    val inputImage = inputImageView.imageProperty()
    val outputImageView = ImageView()
    val outputImage = outputImageView.imageProperty()

    val imageInputLayout = HBox()
    val brightnessInputLayout = HBox()

    val inputImagedCircle = DragCircle(inputImage, true, this, ObjectProperty::class.java)
    val outputImagedCircle = DragCircle(outputImage, false, this, ObjectProperty::class.java)
    val brightnessLeveldCircle = DragCircle(brightnessLevel, true, this, SimpleFloatProperty::class.java)
    val button = Button("Selfdestruct")

    init {
        children.addAll(outputImageView, imageInputLayout, brightnessInputLayout, button, outputImagedCircle)
    }

    fun getResultingImage(inputImage: Image?, brightnessLevel: Float) {
        if (inputImage != null) {
            val sourceImage = OpenCVUtils.image2Mat(inputImage)
            val newImage = Mat(sourceImage.rows(), sourceImage.cols(), sourceImage.type())
            sourceImage.convertTo(newImage, sourceImage.type(), brightnessLevel.toDouble() )

            outputImage.value = OpenCVUtils.mat2Image(newImage)
        }
    }

    init {
        name.value = "Яркость"
        outputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        outputImageView.isPreserveRatio = true

        brightnessLevel.addListener { _, _, newValue ->
            if ((newValue as Float) > 1.0f) {
                brightnessLevel.value = 1.0f
            } else if (newValue < 0.0f) {
                brightnessLevel.value = 0.0f
            }

            getResultingImage(inputImage.value, brightnessLevel.value as Float)
        }
        val Label = Label("  Уровень яркости")
        brightnessInputLayout.children.addAll(brightnessLeveldCircle, Label)
    }

    init  {
        inputImage.addListener { _, _, newValue ->
            getResultingImage(newValue, brightnessLevel.value)
        }

        val Label = Label("  Входное изображение")
        imageInputLayout.children.addAll(inputImagedCircle, Label)
    }

    fun selfDestroy(it: MouseEvent) {
        LinerSinglton.deleteLine(inputImagedCircle)
        LinerSinglton.deleteLine(outputImagedCircle)
        LinerSinglton.deleteLine(brightnessLeveldCircle)
        (outputImagedCircle.parent.parent as Pane).children.remove(this)
    }

    init {
        button.onMouseClicked = EventHandler{
            selfDestroy(it)
        }
    }
}