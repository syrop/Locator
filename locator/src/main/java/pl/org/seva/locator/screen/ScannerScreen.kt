package pl.org.seva.locator.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.org.seva.locator.presentation.ScannerPresentation
import pl.org.seva.locator.presentation.model.TagPresentationModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(presentation: ScannerPresentation) {

    val scope = rememberCoroutineScope()

    var saveDialog by remember { mutableStateOf(false) }
    var selectedTag by remember { mutableStateOf<TagPresentationModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        HorizontalDivider(
            thickness = 2.dp,
        )
        TextButton(
            modifier = Modifier
                .fillMaxSize(),
            onClick = { presentation.scan(scope) }
        ) {
            Text(
                text = "Scan",
                fontSize = 16.sp,
            )
        }
        HorizontalDivider(
            thickness = 2.dp,
        )
        presentation.viewState.collectAsState().value.tags.onEach { tag ->
            TextButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    selectedTag = tag
                    saveDialog = true
                }
            ) {
                Text(
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    text = tag.name,
                    fontSize = 16.sp,
                )
            }
            HorizontalDivider(
                thickness = 2.dp,
            )
        }
        TextButton(
            modifier = Modifier
                .fillMaxSize(),
            onClick = { presentation.stopScan(scope) }
        ) {
            Text(
                text = "Stop Scan",
                fontSize = 16.sp,
            )
        }
        HorizontalDivider(
            thickness = 2.dp,
        )
    }
    if (saveDialog) {
        BasicAlertDialog(
            onDismissRequest = { saveDialog = false }
        ) {
            val tag = requireNotNull(selectedTag) { "Tag not selected"}
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Do you want to save this tag?")
                    Text("Name: ${tag.name}")
                    Text("Address: ${tag.address}")
                    Row {
                        TextButton(
                            onClick = { saveDialog = false }
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                saveDialog = false
                                presentation.save(scope, tag)
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
