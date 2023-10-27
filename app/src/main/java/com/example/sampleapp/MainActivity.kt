package com.example.sampleapp

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sampleapp.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * The main activity of the application.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                LandingScreen()
            }
        }
    }
}

/**
 * Creates a text label with a specified style.
 */
@Composable
fun TextLabel(text: String, header: Boolean = false) {
    val textModifier = Modifier.padding(24.dp)
    var fontSize = if (header) 28.sp else 18.sp
    Text(text = text, color = MaterialTheme.colorScheme.onSurface, modifier = textModifier, fontSize = fontSize)
}

/**
 * Renders a row in the Stats card given the stat title, and it's state from the view model.
 */
@Composable
fun StatRow(statTitle: String, state: State<Any>) {
    Divider()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.clickable {}
    ) {
        TextLabel(text = statTitle)
        Spacer(Modifier.weight(1f))
        TextLabel(text = "${state.value}")
    }
}

/**
 * Renderes the content of the Stats card.
 */
@Composable
fun StatsCardContent(
    buttonState: State<ButtonState>,
    requestState: State<RequestState>,
    headerTitle: String
) {
    var expanded by remember { mutableStateOf(true) }

    Row(modifier = Modifier.clickable {
        expanded = !expanded
    }) {
        TextLabel(headerTitle, true)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { expanded = !expanded }, modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = stringResource(R.string.stats_expander)
            )
        }
    }

    if (expanded) {
        StatRow("Button State", buttonState)
        StatRow("Request State", requestState)
    }
}
/**
 * Renders a card to display information about the app's state.
 */
@Composable
fun StatsCard(buttonState: State<ButtonState>, requestState: State<RequestState>) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .fillMaxWidth()

    ) {
        StatsCardContent(
            buttonState = buttonState,
            requestState = requestState,
            headerTitle = "Stats"
        )
    }
}

/**
 * Renders a button that creates a mock request, and returns a random outcome.
 */
@Composable
fun RequestButton(
    mockRequest: suspend () -> Unit,
    updateButtonState: (buttonState: ButtonState) -> Unit = {},
    buttonState: State<ButtonState>
) {
    val composableScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,


    ) {
        Button(
            shape = CircleShape,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .size(108.dp),
            onClick =
            {
                updateButtonState(ButtonState.Loading)
                composableScope.launch {
                    mockRequest()
                }
            },
            enabled = (buttonState.value == ButtonState.Idle)
        ) {
            Text("Run", fontSize = 20.sp)
        }
    }
}

/**
 * Composes the landing screen with a Material card, and button.
 */
@Composable
fun LandingScreen(
    mainViewModel: MainViewModel = viewModel(),
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        content = {
            Box(modifier = Modifier.padding(it)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .background(MaterialTheme.colorScheme.background),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    StatsCard(
                        buttonState = mainViewModel.buttonState.collectAsState(),
                        requestState = mainViewModel.requestState.collectAsState()
                    )
                    BookCard()
                }
            }

        },
        bottomBar = {
        RequestButton(
            mockRequest = { mainViewModel.executeMockRequest() },
            buttonState = mainViewModel.buttonState.collectAsState()
        )
    })

}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)

@Preview
@Composable
fun AppPreview() {
    AppTheme {
        LandingScreen()
    }
}