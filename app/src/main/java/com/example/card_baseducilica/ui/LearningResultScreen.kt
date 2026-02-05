package com.example.card_baseducilica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
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
    val percent =
        if (total == 0) 0 else (known * 100) / total

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "REZULTAT",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Znam: $known")
        Text("Ne znam: $unknown")
        Text("Uspješnost: $percent %")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onBackToSets) {
            Text("Povratak na setove")
        }
    }
}