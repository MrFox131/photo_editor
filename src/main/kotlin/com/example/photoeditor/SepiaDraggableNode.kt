package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.effect.SepiaTone
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import nu.pattern.OpenCV
import org.opencv.core.Core.transform
import org.opencv.core.CvType
import org.opencv.core.Mat

class SepiaDraggableNode: DraggableNode() {
        val InputImageView = ImageView()
        val OutputImageView = ImageView()
        val inputImage: ObjectProperty<Image> = InputImageView.imageProperty()
        val outputImage: ObjectProperty<Image> = OutputImageView.imageProperty()
        val inputdCircle = DragCircle(inputImage, true, this, ObjectProperty::class.java)
        val outputdCircle = DragCircle(outputImage, false, this, ObjectProperty::class.java)
        val editableValueLayout = HBox()
        val button = Button("Selfdestroy")

        init {
            name.value = "Sepia filter"

            OutputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
            OutputImageView.isPreserveRatio = true

            children.addAll(OutputImageView, editableValueLayout, button)
        }

        init {
//            outputImage.bind(inputImage)
//            val sepia = SepiaTone()
//            sepia.level = 0.7
//            outputImage.value.effect
//            OutputImageView.effect = sepia
            inputImage.addListener { _, _, newValue ->
                val kernel = Mat(3, 3, CvType.CV_32F)
                kernel.put(0, 0,
                    0.272, 0.534, 0.131,
                    0.349, 0.686, 0.168,
                    0.393, 0.769, 0.189)
                val ocvImage = OpenCVUtils.image2Mat(newValue)
                transform(ocvImage, ocvImage, kernel)
                outputImage.value = OpenCVUtils.mat2Image(ocvImage)
            }

            editableValueLayout.children.addAll(inputdCircle, outputdCircle)
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