package com.example.photoeditor

import javafx.beans.value.ChangeListener
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.scene.transform.Transform

object LinerSinglton {
    val parents: MutableMap<DragCircle<*>, DragCircle<*>> = mutableMapOf()
    val connected: MutableMap<DragCircle<*>, ArrayList<DragCircle<*>>> = mutableMapOf()
    val lines: MutableMap<Pair<DragCircle<*>,DragCircle<*>>, Line> = mutableMapOf()
    val lineBindings: MutableMap<Pair<DragCircle<*>, DragCircle<*>>, Pair<ChangeListener<Transform>,ChangeListener<Transform>>> = mutableMapOf()
    var pane: Pane? = null

    fun addPair(first: DragCircle<*>, second: DragCircle<*>) {
        val line = Line()
        parents[first] = second
        if (connected[second] == null) {
            connected[second] = ArrayList()
        }
        connected[second]!!.add(first)
        var bounds = pane!!.sceneToLocal(second.localToScene(second.boundsInLocal))
        lineBindings[Pair(second, first)] = Pair(
            ChangeListener<Transform>{ _, _, _ ->
                var bounds = pane!!.sceneToLocal(second.localToScene(second.boundsInLocal))
                line.endX = bounds.maxX
                line.endY = (bounds.maxY+bounds.minY)/2
                bounds = pane!!.sceneToLocal(first.localToScene(first.boundsInLocal))
                line.startX = bounds.minX
                line.startY = (bounds.maxY+bounds.minY)/2
            },
            ChangeListener<Transform>{
                    _, _, _ ->
                var bounds = pane!!.sceneToLocal(second.localToScene(second.boundsInLocal))
                line.endX = bounds.maxX
                line.endY = (bounds.maxY+bounds.minY)/2
                bounds = pane!!.sceneToLocal(first.localToScene(first.boundsInLocal))
                line.startX = bounds.minX
                line.startY = (bounds.maxY+bounds.minY)/2

            }
        )

        second.localToSceneTransformProperty().addListener(lineBindings[Pair(second, first)]!!.first)
        first.localToSceneTransformProperty().addListener(lineBindings[Pair(second, first)]!!.second)
        line.endX = bounds.maxX
        line.endY = (bounds.maxY+bounds.minY)/2
        bounds = pane!!.sceneToLocal(first.localToScene(first.boundsInLocal))
        line.startX = bounds.minX
        line.startY = (bounds.maxY+bounds.minY)/2
        lines[Pair(second, first)] = line
        
        pane!!.children.addAll(line)
    }

    fun deleteLine(for_deletion: DragCircle<*>) {
        if (parents.containsKey(for_deletion)) {
            val parent = parents[for_deletion]!!
            parents.remove(for_deletion)
            for_deletion.unbindAll()
            connected[parent]!!.remove(for_deletion)
            parent.localToSceneTransformProperty().removeListener(lineBindings[Pair(parent, for_deletion)]!!.first)
            for_deletion.localToSceneTransformProperty().removeListener(lineBindings[Pair(parent, for_deletion)]!!.second)
            lineBindings.remove(Pair(parent, for_deletion))
            pane!!.children.remove(lines[Pair(parent, for_deletion)])
            lines.remove(Pair(parent, for_deletion))
        } else {
            if (connected.containsKey(for_deletion)) {
                while(connected.containsKey(for_deletion) && connected[for_deletion]!!.size > 0){
                    deleteLine(connected[for_deletion]!![0])
                }
                connected.remove(for_deletion)
            }
        }
    }

}