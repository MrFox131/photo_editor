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
import kotlin.math.min

class AddImageDraggableNode: DraggableNode() {
    val xLayout = HBox()
    val yLayout = HBox()
    val textLayout = HBox()
    val imageLayout = HBox()
    val addingImageLayout = HBox()


    val outputImageView = ImageView()
    val outputImage = outputImageView.imageProperty()

    val inputImageView = ImageView()
    var inputImage: ObjectProperty<Image> = inputImageView.imageProperty()
    val inputSourceImagedCircle = DragCircle(inputImage, true, this, ObjectProperty::class.java)

    val addImageView = ImageView()
    val addingImage: ObjectProperty<Image> = addImageView.imageProperty()


    val outDragCircle = DragCircle(outputImage, false, this, ObjectProperty::class.java)

    val xCoord = SimpleIntegerProperty()
    val xdCircle = DragCircle(xCoord, true, this, SimpleIntegerProperty::class.java)

    val yCoord = SimpleIntegerProperty()
    val ydCircle = DragCircle(yCoord, true, this, SimpleIntegerProperty::class.java)


    val inputImagedCircle = DragCircle(addingImage, true, this, ObjectProperty::class.java)

    fun getResultingImage(x: Number, y: Number, addingImage: Image?, img: Image?) {
        if (img != null && addingImage != null) {
            val sourceImg = OpenCVUtils.image2Mat(img)
            val addingImageOcv = OpenCVUtils.image2Mat(addingImage)
            addingImageOcv.copyTo(
                sourceImg.rowRange(
                    min(x as Int, sourceImg.rows()),
                    min(x + addingImageOcv.rows(), sourceImg.rows())
                ).colRange(
                    min(y as Int, sourceImg.cols()-1),
                    min(y + addingImageOcv.cols(), sourceImg.cols())
                )
            )

            outputImage.value = OpenCVUtils.mat2Image(sourceImg)
        }
    }


    // imageProperty
    init {
        val label = Label("  Входное изо")
        imageLayout.children.addAll(inputSourceImagedCircle, label)
    }

    // addingImageProperty
    init {
        val label = Label("  Добавим")

        addingImage.addListener { _, _, newValue ->
            getResultingImage(xCoord.value, yCoord.value, newValue, inputImage.value)
        }
        inputImage.addListener { _, _, newValue ->
            getResultingImage(xCoord.value, yCoord.value, addingImage.value, newValue)
        }

        addingImageLayout.children.addAll(inputImagedCircle, label)
    }

    // x property initialization
    init {


        val Label = Label("  x координата")
        xLayout.children.addAll(
            xdCircle,
            Label
        )

        xCoord.addListener { _, _, newValue ->
            getResultingImage(newValue, yCoord.value, addingImage.value, inputImage.value)
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
            getResultingImage(xCoord.value, newValue, addingImage.value, inputImage.value)
        }
    }

    val button = Button("Selfdestroy")

    fun selfDestroy(it: MouseEvent) {
        LinerSinglton.deleteLine(outDragCircle)
        LinerSinglton.deleteLine(inputImagedCircle)
        LinerSinglton.deleteLine(inputSourceImagedCircle)
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
        name.value = "Добавить картинку"
        outputImageView.fitWidthProperty().bind(prefWidthProperty().subtract(1))
        outputImageView.isPreserveRatio = true
        children.addAll(
            outputImageView,
            xLayout,
            yLayout,
            textLayout,
            imageLayout,
            addingImageLayout,
            outDragCircle,
            button
        )
    }
}