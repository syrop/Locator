package pl.org.seva.locator

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.org.seva.locator.presentation.ScannerPresentation

@Composable
fun ScannerScreen(presentation: ScannerPresentation) {

    val scope = rememberCoroutineScope()

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
        presentation.viewState.collectAsState().value.tags.onEach {
            TextButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {}
            ) {
                Text(
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    text = it.name,
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

}
