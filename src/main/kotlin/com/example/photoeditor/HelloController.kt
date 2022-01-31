package com.example.photoeditor

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import nu.pattern.OpenCV


class HelloController {
    @FXML
    lateinit var ImageViewContainer: ScrollPane
    @FXML
    lateinit var ImageViewObj: ImageView
    @FXML
    lateinit var DnDPane: Pane
    @FXML
    lateinit var layoutBase: VBox

    fun initialize() {
        OpenCV.loadLocally()
        ImageViewContainer.prefWidthProperty().bind(layoutBase.widthProperty())
        ImageViewContainer.prefHeightProperty().bind(layoutBase.heightProperty().multiply(0.50))
        ImageViewObj.isPreserveRatio = true
        ImageViewObj.fitWidthProperty().bind(ImageViewContainer.prefWidthProperty())
        ImageViewObj.fitHeightProperty().bind(ImageViewContainer.prefHeightProperty())

        val inputImage = InputImageDraggableNode()
        val outputImage = OutputImageNode()

        LinerSinglton.pane = DnDPane
        ImageViewObj.imageProperty().bind(outputImage.value)


        DnDPane.children += inputImage
        DnDPane.children += outputImage
        inputImage.prefWidth = 100.0
        inputImage.prefHeight = 100.0
        outputImage.prefWidth = 100.0
        outputImage.prefHeight = 100.0

        //context menu init

        val contextMenu = ContextMenu()
        
        val addAddImageNodeItem = MenuItem("PiP")
        addAddImageNodeItem.onAction = EventHandler {
            val node = AddImageDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addAddTextItem = MenuItem("Текст на картинку")
        addAddTextItem.onAction = EventHandler {
            val node = AddTextDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addBrightnessNodeItem = MenuItem("Яркость")
        addBrightnessNodeItem.onAction = EventHandler {
            val node = BrightnessDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addFloatNodeItem = MenuItem("Дробное число")
        addFloatNodeItem.onAction = EventHandler {
            val node = FloatDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addBlurNodeItem = MenuItem("Блюр")
        addBlurNodeItem.onAction = EventHandler {
            val node = GaussianBlurDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addGrayscaleNodeItem = MenuItem("Оттенки серого")
        addGrayscaleNodeItem.onAction = EventHandler {
            val node = GrayscaleDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addImageNodeItem = MenuItem("Картинка")
        addImageNodeItem.onAction = EventHandler {
            val node = ImageDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addIntegerNodeItem = MenuItem("Целое")
        addIntegerNodeItem.onAction = EventHandler {
            val node = IntegerDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addInvertNodeItem = MenuItem("Инверсия")
        addInvertNodeItem.onAction = EventHandler {
            val node = InvertDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addSepiaNodeItem = MenuItem("Сепия")
        addSepiaNodeItem.onAction = EventHandler {
            val node = SepiaDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }
        val addStringNodeItem = MenuItem("Строка")
        addStringNodeItem.onAction = EventHandler {
            val node = StringDraggableNode()
            node.prefWidth = 180.0
            node.prefHeight = 50.0

            DnDPane.children.addAll(node)
        }

        contextMenu.items.addAll(addAddImageNodeItem, addAddTextItem, addBrightnessNodeItem, addFloatNodeItem, addBlurNodeItem, addGrayscaleNodeItem,
            addImageNodeItem, addIntegerNodeItem, addInvertNodeItem, addSepiaNodeItem, addStringNodeItem
            )

        DnDPane.onContextMenuRequested = EventHandler {
            contextMenu.show(DnDPane, it.screenX, it.screenY)
        }
    }
}