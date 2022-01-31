package com.example.photoeditor

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.ClipboardContent
import javafx.scene.input.TransferMode
import javafx.scene.layout.HBox
import javafx.scene.shape.Circle

abstract class InputDraggableNode<T> : DraggableNode() {
    abstract var value: T
    var editableValueLayout = HBox()

    class OutputLine<T>(var value: T, field: Node): HBox() {


        var outputCircle: Circle = Circle()
        init {
            outputCircle.radius = 5.0
            outputCircle.layoutY = 15.0
            outputCircle.fill = javafx.scene.paint.Color.GREEN
            children.addAll(field, outputCircle)
            prefWidth = 100.0
            prefHeight = 30.0

            outputCircle.onMousePressed = EventHandler {
                it.consume()
            }
            outputCircle.onMouseDragged = EventHandler {
                it.consume()
            }
            outputCircle.onMouseClicked = EventHandler {
                it.consume()
            }
            outputCircle.onDragDetected = EventHandler {
                val dragboard = outputCircle.startDragAndDrop(TransferMode.LINK)
                val cc = ClipboardContent()
                cc.putString("Why this shit dows not start without content in dragboard???!!!?!?!")
                dragboard.setContent(cc)
                println("drag started")
            }
            outputCircle.onDragEntered = EventHandler {
                it.acceptTransferModes(TransferMode.LINK)
                outputCircle.fill = javafx.scene.paint.Color.RED
            }
            outputCircle.onDragExited = EventHandler {
                it.acceptTransferModes(TransferMode.LINK)
                outputCircle.fill = javafx.scene.paint.Color.GREEN
            }
//            outputCircle.onDragOver = EventHandler {
//                if (it.gestureSource != outputCircle) {
//                    it.acceptTransferModes(TransferMode.LINK)
//                }
//            }
//            outputCircle.onDragDropped = EventHandler {
//
//            }
        }
    }
    var inputWidgets: ArrayList<OutputLine<T>> = ArrayList()


    init {
        children.addAll(editableValueLayout)
    }

    fun initFields() {
        editableValueLayout.children.addAll(*inputWidgets.toTypedArray())
    }

}