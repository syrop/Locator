package pl.org.seva.locator.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.toSize
import pl.org.seva.locator.presentation.CoordinatesPresentation
import pl.org.seva.locator.presentation.model.CoordinatesViewState

@Composable
fun CoordinatesScreen(
    presentation: CoordinatesPresentation
) {

    var size by remember { mutableStateOf(Size.Zero) }
    val scope = rememberCoroutineScope()
    val viewState: CoordinatesViewState = presentation.viewState.collectAsState().value
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    val minX = viewState.tags.minOfOrNull { it.x } ?: 0
    val maxX = viewState.tags.maxOfOrNull { it.x } ?: 0
    val minY = viewState.tags.minOfOrNull { it.y } ?: 0
    val maxY = viewState.tags.maxOfOrNull { it.y } ?: 0
    val dX = maxX - minX + 2
    val dY = maxY - minY + 2
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size = it.toSize() }
        ) {
        viewState.tags.forEach { tag ->
            val offset = Offset(
                if (dX == 0) 0f else size.width / 2 - (tag.x - minX) * size.minDimension / dX,
                if (dY == 0) 0f else size.height / 2 - (tag.y - minY) * size.minDimension / dY,
            )
            drawCircle(color, 6f, offset)
            drawText(
                topLeft = offset,
                style = TextStyle(color = color),
                textMeasurer = textMeasurer,
                text = buildAnnotatedString {
                    withStyle(ParagraphStyle()) {
                        append(tag.name)
                    }
                    withStyle(ParagraphStyle()) {
                        append(tag.address)
                    }

                }
            )
        }


    }
}
