package com.example.card_baseducilica.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.card_baseducilica.viewmodel.LearningViewModel

@Composable
fun LearningScreen(
    setId: Int,
    setTitle: String,
    onlyFavorites: Boolean,
    onFinished: () -> Unit,
    onCancel: () -> Unit,
    viewModel: LearningViewModel = viewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val showAnswer by viewModel.showAnswer.collectAsState()
    val finished by viewModel.finished.collectAsState()

    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(setId, onlyFavorites) {
        viewModel.startLearning(setId, onlyFavorites)
    }

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    if (cards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nema kartica za učenje.")
        }
        return
    }

    val card = cards[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "UČENJE: $setTitle",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("${currentIndex + 1} / ${cards.size}")

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable { viewModel.revealAnswer() }
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (showAnswer) card.answer else card.question,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.markKnown() }) {
                Text("ZNAM")
            }
            Button(onClick = { viewModel.markUnknown() }) {
                Text("NE ZNAM")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = { showCancelDialog = true }) {
            Text("Prestani s učenjem")
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Prekid učenja") },
            text = { Text("Želiš li prekinuti učenje?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        onCancel()
                    }
                ) {
                    Text("Da")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Ne")
                }
            }
        )
    }
}