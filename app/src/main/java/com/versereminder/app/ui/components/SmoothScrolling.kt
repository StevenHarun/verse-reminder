package com.versereminder.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.versereminder.app.ui.theme.FigmaDarkBorderGold
import com.versereminder.app.ui.theme.FigmaDarkSurface
import com.versereminder.app.ui.theme.FigmaGold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Modifier that draws a sleek, glowing gold scrollbar for [LazyListState].
 * Fades in gently during scrolling and glides away smoothly when scrolling stops.
 */
@Composable
fun Modifier.smoothScrollbar(
    state: LazyListState,
    color: Color = FigmaGold,
    width: Dp = 3.5.dp,
    paddingEnd: Dp = 3.dp,
    minThumbHeight: Dp = 28.dp
): Modifier {
    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(state.isScrollInProgress) {
        if (state.isScrollInProgress) {
            isScrolling = true
        } else {
            delay(700)
            isScrolling = false
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (isScrolling) 0.75f else 0f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "ScrollbarAlpha"
    )

    return this.drawWithContent {
        drawContent()

        if (alpha > 0.01f) {
            val totalItems = state.layoutInfo.totalItemsCount
            val visibleItems = state.layoutInfo.visibleItemsInfo.size

            if (totalItems > 0 && totalItems > visibleItems) {
                val viewportHeight = size.height
                val barWidthPx = width.toPx()
                val paddingEndPx = paddingEnd.toPx()
                val minThumbHeightPx = minThumbHeight.toPx()

                val estimatedRatio = visibleItems.toFloat() / totalItems.toFloat()
                val thumbHeightPx = (viewportHeight * estimatedRatio).coerceIn(minThumbHeightPx, viewportHeight * 0.75f)

                val firstIndex = state.firstVisibleItemIndex.toFloat()
                val firstOffset = state.firstVisibleItemScrollOffset.toFloat()
                val firstItemSize = state.layoutInfo.visibleItemsInfo.firstOrNull()?.size?.toFloat() ?: 1f

                val scrollProgress = ((firstIndex + (firstOffset / firstItemSize)) / totalItems.toFloat())
                    .coerceIn(0f, 1f)

                val availableTrack = viewportHeight - thumbHeightPx
                val thumbTopY = (scrollProgress * availableTrack).coerceIn(0f, availableTrack)

                drawRoundRect(
                    color = color.copy(alpha = alpha),
                    topLeft = Offset(x = size.width - barWidthPx - paddingEndPx, y = thumbTopY),
                    size = Size(width = barWidthPx, height = thumbHeightPx),
                    cornerRadius = CornerRadius(barWidthPx / 2f, barWidthPx / 2f)
                )
            }
        }
    }
}

/**
 * Modifier that draws a smooth scrollbar for regular [ScrollState] (vertical scroll).
 */
@Composable
fun Modifier.smoothScrollbar(
    state: ScrollState,
    color: Color = FigmaGold,
    width: Dp = 3.5.dp,
    paddingEnd: Dp = 3.dp,
    minThumbHeight: Dp = 28.dp
): Modifier {
    var isScrolling by remember { mutableStateOf(false) }

    LaunchedEffect(state.isScrollInProgress) {
        if (state.isScrollInProgress) {
            isScrolling = true
        } else {
            delay(700)
            isScrolling = false
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (isScrolling) 0.75f else 0f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "ScrollStateAlpha"
    )

    return this.drawWithContent {
        drawContent()

        if (alpha > 0.01f && state.maxValue > 0) {
            val viewportHeight = size.height
            val barWidthPx = width.toPx()
            val paddingEndPx = paddingEnd.toPx()
            val minThumbHeightPx = minThumbHeight.toPx()

            val scrollRange = state.maxValue.toFloat() + viewportHeight
            val thumbHeightPx = (viewportHeight * (viewportHeight / scrollRange))
                .coerceIn(minThumbHeightPx, viewportHeight * 0.75f)

            val scrollProgress = (state.value.toFloat() / state.maxValue.toFloat()).coerceIn(0f, 1f)
            val availableTrack = viewportHeight - thumbHeightPx
            val thumbTopY = (scrollProgress * availableTrack).coerceIn(0f, availableTrack)

            drawRoundRect(
                color = color.copy(alpha = alpha),
                topLeft = Offset(x = size.width - barWidthPx - paddingEndPx, y = thumbTopY),
                size = Size(width = barWidthPx, height = thumbHeightPx),
                cornerRadius = CornerRadius(barWidthPx / 2f, barWidthPx / 2f)
            )
        }
    }
}

/**
 * Modifier that adds subtle vertical fading edges (top & bottom) so that scrolling content
 * melts seamlessly into the background rather than hard-clipping.
 */
fun Modifier.verticalFadingEdges(
    topFadeHeight: Dp = 18.dp,
    bottomFadeHeight: Dp = 24.dp,
    isTopFaded: Boolean = true,
    isBottomFaded: Boolean = true
): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val topPx = topFadeHeight.toPx()
        val bottomPx = bottomFadeHeight.toPx()
        val height = size.height

        if (isTopFaded && topPx > 0) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 0f,
                    endY = topPx
                ),
                blendMode = BlendMode.DstIn
            )
        }

        if (isBottomFaded && bottomPx > 0) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color.Transparent),
                    startY = height - bottomPx,
                    endY = height
                ),
                blendMode = BlendMode.DstIn
            )
        }
    }

/**
 * Floating Quick Scroll-to-Top Button that smoothly slides into view when the user
 * has scrolled down, providing a smooth spring glide back to the top of the list.
 */
@Composable
fun BoxScope.SmoothScrollToTopButton(
    listState: LazyListState,
    thresholdItemIndex: Int = 3,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex >= thresholdItemIndex }
    }

    AnimatedVisibility(
        visible = showButton,
        enter = fadeIn(animationSpec = tween(250)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = spring(stiffness = 400f)
        ),
        exit = fadeOut(animationSpec = tween(200)) + scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(200)
        ),
        modifier = modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 84.dp)
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(
                        index = 0,
                        scrollOffset = 0
                    )
                }
            },
            shape = CircleShape,
            containerColor = FigmaDarkSurface,
            contentColor = FigmaGold,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Kembali ke Atas",
                    tint = FigmaGold,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
