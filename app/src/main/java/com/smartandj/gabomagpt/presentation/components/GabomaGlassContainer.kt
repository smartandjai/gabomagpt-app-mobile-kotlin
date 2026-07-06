package com.smartandj.gabomagpt.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * GabomaGlassContainer
 *
 * Implémentation du Glassmorphism de luxe ("Liquid Glass") avec Haze v2.
 * Ce composant gère le flou, le grain (bruit) et la bordure lumineuse (façon Apple Vision Pro).
 */
@Composable
fun GabomaGlassContainer(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    blurRadius: Dp = 25.dp,
    noiseFactor: Float = 0.05f,
    tintColor: Color = Color.White.copy(alpha = 0.15f),
    borderColor1: Color = Color.White.copy(alpha = 0.4f),
    borderColor2: Color = Color.White.copy(alpha = 0.1f),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            // Application du flou matériel Haze sur les éléments en arrière-plan
            .hazeEffect(state = hazeState) {
                blurEffect {
                    this.blurRadius = blurRadius
                    this.noiseFactor = noiseFactor
                }
            }
            // Teinte par-dessus le verre dépoli
            .androidx.compose.foundation.background(tintColor)
            // Bordure lumineuse pour l'effet de réfraction
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(borderColor1, borderColor2)
                ),
                shape = shape
            )
    ) {
        content()
    }
}
