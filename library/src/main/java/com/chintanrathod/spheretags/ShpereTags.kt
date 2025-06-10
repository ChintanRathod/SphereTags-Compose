package com.chintanrathod.spheretags

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import kotlin.math.*

/*
 Defines a data class named Tag3D.
 */
data class Tag3D(
    var x: Double, // Represents the X-coordinate of the 3D tag
    var y: Double, // Represents the Y-coordinate of the 3D tag.
    var z: Double, // Represents the Z-coordinate of the 3D tag
    val text: String // The text content of the tag, which is immutable ('val')
)

/**
 * Following function represent the Sphere logic.
 * It requires additional parameters to make it work
 *
 * @param radius: radius of the sphere
 * @param tags: List of string tags as Input
 * @param selectedTag: a clickable callback when tag is clicked
 */
@Composable
fun SphereTags(
    radius: Double,
    tags: List<String>,
    textColor: Int,
    textSize: TextUnit,
    selectedTag: (String) -> Unit,
) {
    val tagsList = generateTags(tags, radius)
    val baseTags = remember { tagsList }
    val animatedTags = remember { baseTags.map { it.copy() }.toMutableStateList() }
    val textSizeInFloat = SpToFloatConverter(textSize)

    Box(
        modifier = Modifier
            .fillMaxSize()
            // Attaches a pointer input listener to detect drag gestures.
            .pointerInput(Unit) {
                // Detects drag gestures within this Composable.
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        // Destructures the dragAmount to get X and Y displacement.
                        val (dx, dy) = dragAmount
                        // Calculates the magnitude (length) of the drag vector.
                        val dragLength = sqrt(dx * dx + dy * dy)

                        // If there's no drag, exit early.
                        if (dragLength == 0f) return@detectDragGestures

                        // Rotation axis: perpendicular to drag
                        // 3D axis from 2D drag
                        val axis = normalize3D(-dy, dx, 0f)
                        // Rotation angle scale
                        val angle = dragLength * 0.005

                        // Rotate tags around computed axis
                        for (i in animatedTags.indices) {
                            animatedTags[i] = rotateTag(animatedTags[i], axis, angle)
                        }

                        change.consume()
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val center = Offset(size.width / 2.0f, size.height / 2.0f)
                    val clicked = findTagAtOffset(animatedTags, tapOffset, center)
                    clicked?.let {
                        selectedTag(it.text)
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            drawTags(
                scope = this,
                tags = animatedTags,
                center = center,
                textColor = textColor,
                textSize = textSizeInFloat
            )
        }
    }
}

/**
 * This function calculate new coordinates to rotate the sphere based on user touch and drag position
 */
fun rotateTag(tag: Tag3D, axis: Triple<Float, Float, Float>, angle: Double): Tag3D {
    val (x, y, z) = tag
    val (u, v, w) = axis

    // Calculate the cosine of the rotation angle.
    val cosA = cos(angle)
    // Calculate the sine of the rotation angle.
    val sinA = sin(angle)

    // Calculate the dot product of the tag vector and the axis vector.
    val dot = u * x + v * y + w * z
    // Calculate the X component of the cross product (axis x tag).
    val crossX = v * z - w * y
    // Calculate the Y component of the cross product.
    val crossY = w * x - u * z
    // Calculate the Z component of the cross product.
    val crossZ = u * y - v * x

    // Apply Rodrigues' rotation formula to calculate the new X coordinate.
    val newX = x * cosA + crossX * sinA + u * dot * (1 - cosA)
    // Apply Rodrigues' rotation formula to calculate the new Y coordinate.
    val newY = y * cosA + crossY * sinA + v * dot * (1 - cosA)
    // Apply Rodrigues' rotation formula to calculate the new Z coordinate.
    val newZ = z * cosA + crossZ * sinA + w * dot * (1 - cosA)

    // Returns a new Tag3D object with the rotated coordinates and the original text.
    return Tag3D(newX.toDouble(), newY.toDouble(), newZ.toDouble(), tag.text)
}

fun normalize3D(x: Float, y: Float, z: Float): Triple<Float, Float, Float> {
    // Calculate the magnitude (length) of the vector.
    val length = sqrt(x * x + y * y + z * z)
    // Returns a normalized vector, or (0,0,0) if the original vector has zero length to avoid division by zero.
    return if (length == 0f) Triple(0f, 0f, 0f) else Triple(x / length, y / length, z / length)
}

/**
 * Function to draw the tags on the Canvas.
 */
fun drawTags(
    scope: DrawScope,
    tags: List<Tag3D>,
    center: Offset,
    textColor: Int,
    textSize: Float
) {
    // Creates an Android Paint object for drawing text.
    val paint = Paint().apply {
        color = textColor
        textAlign = Paint.Align.CENTER
    }

    paint.textSize = textSize

    for (tag in tags.sortedByDescending { it.z }) {
        val scale = (tag.z / 300) + 1
        val alpha = ((tag.z + 300) / 600f).coerceIn(0.4, 1.0)

        paint.alpha = (alpha * 255).toInt()
        paint.textSize = textSize * scale.toFloat()

        scope.drawContext.canvas.nativeCanvas.drawText(
            tag.text,
            center.x + tag.x.toFloat(),
            center.y + tag.y.toFloat(),
            paint
        )
    }
}

/**
 * Following function will take list of string (tags)
 */
fun generateTags(
    tagsInput: List<String>,
    radius: Double
): List<Tag3D> {
    val tags = mutableListOf<Tag3D>()
    for (i in 0 until tagsInput.size) {
        val phi = acos(-1.0 + (2.0 * i) / tagsInput.size)
        val theta = sqrt(tagsInput.size * Math.PI) * phi
        val x = radius * cos(theta) * sin(phi)
        val y = radius * sin(theta) * sin(phi)
        val z = radius * cos(phi)
        tags.add(Tag3D(x, y, z, tagsInput[i]))
    }
    return tags
}

fun findTagAtOffset(tags: List<Tag3D>, tap: Offset, center: Offset): Tag3D? {
    val touchRadius = 60f
    return tags
        .map {
            val x = (center.x + it.x).toFloat()
            val y = (center.y + it.y).toFloat()
            it to Offset(x, y)
        }
        .filter { (_, pos) ->
            (tap - pos).getDistance() < touchRadius
        }
        .maxByOrNull { it.first.z }?.first // pick front-most tag
}

@Composable
fun SpToFloatConverter(spValue: TextUnit): Float {
    // Get the current density scope
    val density = LocalDensity.current

    // Convert sp to pixels (Float)
    val pixelsFloat: Float = with(density) {
        spValue.toPx()
    }

    return pixelsFloat
}
