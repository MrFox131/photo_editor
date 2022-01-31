package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class GaussianBlurDraggableNode: DraggableNode() {
    val imageView = ImageView()
    val imageValue = imageView.imageProperty()
    val imagedCircle = DragCircle(imageValue, true, this, ObjectProperty::class.java)
    val imageValueLayout = HBox()

    val outputImageView = ImageView()
    val outputImageValue = outputImageView.imageProperty()
    val outputImagedCircle = DragCircle(outputImageValue, false, this, ObjectProperty::class.java)

    val integerValue = SimpleIntegerProperty()
    val integerdCircle = DragCircle(integerValue, true, this, SimpleIntegerProperty::class.java)
    val integerValueLayout = HBox()



    fun getResultingImage(num: Number, image: Image?) {
        var num = num
        if (image != null) {
            val sourceImage = OpenCVUtils.image2Mat(image)
            val outputImage = Mat(sourceImage.rows(), sourceImage.cols(), sourceImage.type())
            if ((num as Int) % 2==0) {
                num += 1
            }
            Imgproc.GaussianBlur(
                sourceImage,
                outputImage,
                Size(num.toDouble(), num.toDouble()),
                0.0
            )
            outputImageValue.value = OpenCVUtils.mat2Image(outputImage)
        }
    }

    val button = Button("Selfdestroy")

    init {
        name.value = "Gaussian blur"

        outputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        outputImageView.isPreserveRatio = true

        children.addAll(outputImageView, integerValueLayout, imageValueLayout, button)
    }

    init {
        imageValue.addListener { _, _, newValue ->
            getResultingImage(integerValue.value, newValue)
        }
        integerValue.addListener { _, _, newValue ->
            getResultingImage(newValue, imageValue.value)
        }
    }

    init {
        val TextField = Label("  Размер ядра")
        integerValueLayout.children.addAll(integerdCircle, TextField)
    }

    init {
        val TextField = Label("  входное изображение")
        imageValueLayout.children.addAll(imagedCircle, TextField, outputImagedCircle)
    }



    fun selfDestroy(it: MouseEvent) {
        LinerSinglton.deleteLine(imagedCircle)
        LinerSinglton.deleteLine(outputImagedCircle)
        (parent as Pane).children.remove(this)
    }

    init {
        button.onMouseClicked = EventHandler{
            selfDestroy(it)
        }
    }

}