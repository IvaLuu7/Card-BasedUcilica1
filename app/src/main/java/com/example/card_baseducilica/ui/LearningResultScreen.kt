package com.example.card_baseducilica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.card_baseducilica.viewmodel.LearningViewModel

@Composable
fun LearningResultScreen(
    viewModel: LearningViewModel,
    onBackToSets: () -> Unit
) {
    val known by viewModel.knownCount.collectAsState()
    val unknown by viewModel.unknownCount.collectAsState()

    val total = known + unknown
    val percentage =
        if (total == 0) 0 else (known * 100) / total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "REZULTAT",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Broj točnih (znam): $known")
        Text("Broj netočnih (ne znam): $unknown")
        Text("Uspješnost: $percentage%")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onBackToSets) {
            Text("Povratak na setove")
        }
    }
}