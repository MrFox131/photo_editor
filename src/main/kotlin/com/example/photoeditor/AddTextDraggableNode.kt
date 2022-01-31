package com.example.photoeditor

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc


class AddTextDraggableNode : DraggableNode() {
    val xLayout = HBox()
    val yLayout = HBox()
    val textLayout = HBox()
    val imageLayout = HBox()
    val outputImageView = ImageView()
    val outputImage = outputImageView.imageProperty()
    val outDragCircle = DragCircle(outputImage, false, this, ObjectProperty::class.java)
    val xCoord = SimpleIntegerProperty()
    val yCoord = SimpleIntegerProperty()
    val text = SimpleStringProperty()

    val textdCircle = DragCircle(text, true, this, SimpleStringProperty::class.java)
    val ydCircle = DragCircle(yCoord, true, this, SimpleIntegerProperty::class.java)
    val xdCircle = DragCircle(xCoord, true, this, SimpleIntegerProperty::class.java)

    val inputImageView = ImageView()
    var inputImage = inputImageView.imageProperty()
    val imagedCircle = DragCircle(inputImage, true, this, ObjectProperty::class.java)

    fun getResultingImage(x: Number, y: Number, str: String, img: Image?) {
        if (img != null) {
            val sourceImg = OpenCVUtils.image2Mat(img)
            Imgproc.putText(
                sourceImg,
                str,
                Point((x as Int).toDouble(), (y as Int).toDouble()),
                Imgproc.FONT_HERSHEY_COMPLEX,
                1.0,
                Scalar(0.0, 0.0, 255.0),
                3
            )
            outputImage.value = OpenCVUtils.mat2Image(sourceImg)
        }
    }


    // imageProperty
    init {

        val label = Label("  Входное изо")

        inputImage.addListener { _, _, newValue ->
            getResultingImage(xCoord.value, yCoord.value, text.value, newValue)
        }

        imageLayout.children.addAll(imagedCircle, label)
    }

    // x property initialization
    init {


        val Label = Label("  x координата")
        xLayout.children.addAll(
            xdCircle,
            Label
        )

        xCoord.addListener { _, _, newValue ->
            getResultingImage(newValue, yCoord.value, text.value, inputImage.value)
        }
    }

    // y property
    init {

        val label = Label("  y координата")
        yLayout.children.addAll(
            ydCircle,
            label
        )

        yCoord.addListener { _, _, newValue ->
            getResultingImage(xCoord.value, newValue, text.value, inputImage.value)
        }
    }

    // text
    init {

        val label = Label("  текст")
        textLayout.children.addAll(
            textdCircle,
            label
        )

        text.addListener { _, _, newValue ->
            getResultingImage(xCoord.value, yCoord.value, newValue, inputImage.value)
        }
    }

    val button = Button("Selfdestroy")

    fun selfDestroy(it: MouseEvent) {
        LinerSinglton.deleteLine(outDragCircle)
        LinerSinglton.deleteLine(imagedCircle)
        LinerSinglton.deleteLine(textdCircle)
        LinerSinglton.deleteLine(xdCircle)
        LinerSinglton.deleteLine(ydCircle)
        (parent as Pane).children.remove(this)
    }

    init {
        button.onMouseClicked = EventHandler{
            selfDestroy(it)
        }
    }


    init {
        name.value = "Добавить текст"
        outputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        outputImageView.isPreserveRatio = true
        children.addAll(
            outputImageView,
            xLayout,
            yLayout,
            textLayout,
            imageLayout,
            outDragCircle,
            button
        )
    }
}