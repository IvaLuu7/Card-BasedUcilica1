package com.example.card_baseducilica.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.card_baseducilica.viewmodel.LearningViewModel

@Composable
fun LearningScreen(
    setId: Int,
    setTitle: String,
    viewModel: LearningViewModel,
    onFinished: () -> Unit
) {
    val cards by viewModel.cards.collectAsState()
    val index by viewModel.currentIndex.collectAsState()
    val showAnswer by viewModel.showAnswer.collectAsState()
    val finished by viewModel.finished.collectAsState()

    LaunchedEffect(setId) {
        viewModel.startLearning(setId)
    }

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    if (cards.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nema kartica u ovom setu.")
        }
        return
    }

    val card = cards[index]

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "UČENJE: $setTitle  ${index + 1} / ${cards.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { viewModel.revealAnswer() }
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (showAnswer) card.answer else card.question,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = viewModel::markKnown) { Text("✅ ZNAM") }
            Button(onClick = viewModel::markUnknown) { Text("❌ NE ZNAM") }
        }
    }
}