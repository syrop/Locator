package pl.org.seva.locator.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.toSize
import pl.org.seva.locator.presentation.LocatorPresentation
import pl.org.seva.locator.presentation.model.LocatorViewState
import pl.org.seva.locator.presentation.model.TagPresentationModel


@Composable
fun LocatorScreen(presentation: LocatorPresentation) {

    var size by remember { mutableStateOf(Size.Zero) }
    val scope = rememberCoroutineScope()
    val viewState: LocatorViewState = presentation.viewState.collectAsState().value
    val color = if (isSystemInDarkTheme()) Color.White else Color.Black
    val minX = viewState.tags.minOfOrNull { it.x } ?: 0
    val maxX = viewState.tags.maxOfOrNull { it.x } ?: 0
    val minY = viewState.tags.minOfOrNull { it.y } ?: 0
    val maxY = viewState.tags.maxOfOrNull { it.y } ?: 0
    val dX = maxX - minX + 2
    val dY = maxY - minY + 2
    val textMeasurer = rememberTextMeasurer()

    fun Pair<Double, Double>.toOffset() = Offset(
        if (dX == 0) 0f else size.width / dX + (first.toFloat() - minX) * size.width / dX,
        if (dY == 0) 0f else size.height / dY + (second.toFloat() - minY) * size.height / dY,
    )

    fun TagPresentationModel.toOffset() = (x.toDouble() to y.toDouble()).toOffset()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size = it.toSize() }
    ) {
        viewState.tags.forEach { tag ->
            val offset = tag.toOffset()
            drawCircle(color, 6f, offset)
            drawText(
                topLeft = offset,
                style = TextStyle(color = color),
                textMeasurer = textMeasurer,
                text = buildAnnotatedString {
                    withStyle(ParagraphStyle()) {
                        append(tag.name)
                    }
                    viewState.rssiMap[tag.address]?.let { rssi ->
                        withStyle(ParagraphStyle()) {
                            append(rssi.toString())
                        }
                    }
                }
            )
        }
        viewState.location?.let { location ->
            if (location.first < minX || location.first > maxX ||
                location.second < minY || location.second > maxY) {
                return@let
            }
            drawCircle(Color.Red, 10f, location.toOffset())
        }
    }

}
