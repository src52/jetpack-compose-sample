package com.example.sampleapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun BookCardContent(headerTitle: String) {

    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier.clickable {
        expanded = !expanded
    }) {
        TextLabel(headerTitle, true)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = { expanded = !expanded }, modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = stringResource(R.string.stats_expander)
            )
        }
    }

    if (expanded) {
        Column() {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.alice),
                    modifier = Modifier
                )
            }
        }
    }
}


@Composable
fun BookCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)

    ) {
        BookCardContent(headerTitle = "Book")
    }
}