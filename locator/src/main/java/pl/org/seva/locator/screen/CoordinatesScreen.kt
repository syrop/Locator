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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import pl.org.seva.locator.presentation.CoordinatesPresentation
import pl.org.seva.locator.presentation.model.CoordinatesViewState
import pl.org.seva.locator.presentation.model.TagPresentationModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
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
    var dialogOpen by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf<TagPresentationModel?>(null) }
    var enteredName by remember { mutableStateOf("") }
    var enteredX by remember { mutableStateOf("") }
    var enteredY by remember { mutableStateOf("") }

    fun TagPresentationModel.toOffset() = Offset(
        if (dX == 0) 0f else size.width / dX + (x - minX) * size.width / dX,
        if (dY == 0) 0f else size.height / dY + (y - minY) * size.height / dY,
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size = it.toSize() }
            .pointerInput(viewState) {
                detectTapGestures(
                    onTap = { offset ->
                        selectedTag = viewState.tags.find { tag ->
                            abs(offset.x - tag.toOffset().x) < 50f &&
                                    abs(offset.y - tag.toOffset().y) < 50f
                        }?.apply {
                            enteredName = name
                            enteredX = x.toString()
                            enteredY = y.toString()
                            dialogOpen = true
                        }
                    }
                )
            }
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
                    withStyle(ParagraphStyle()) {
                        append(tag.address)
                    }

                }
            )
        }
    }
    if (dialogOpen) {
        BasicAlertDialog(
            onDismissRequest = { dialogOpen = false }
        ) {
            val tag = requireNotNull(selectedTag) { "Tag not selected" }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        value = enteredName,
                        onValueChange = { enteredName = it },
                        label = { Text("Name") }
                    )
                    TextField(
                        value = enteredX,
                        onValueChange = { enteredX = it },
                        label = { Text("X") }
                    )
                    TextField(
                        value = enteredY,
                        onValueChange = { enteredY = it },
                        label = { Text("Y") }
                    )
                    Row {
                        TextButton(
                            onClick = {
                                dialogOpen = false
                                presentation.delete(scope, tag.address)
                            }
                        ) {
                            Text("Delete")
                        }
                        TextButton(
                            onClick = { dialogOpen = false }
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                dialogOpen = false
                                presentation.update(scope, tag.copy(
                                    name = enteredName,
                                    x = enteredX.toIntOrNull() ?: 0,
                                    y = enteredY.toIntOrNull() ?: 0,
                                ))
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}
