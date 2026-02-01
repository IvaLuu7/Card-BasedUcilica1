package com.example.card_baseducilica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.card_baseducilica.viewmodel.SetViewModel
import com.example.card_baseducilica.viewmodel.CardViewModel
import com.example.card_baseducilica.data.session.SessionManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImportSetScreen(
    navController: NavController,
    setViewModel: SetViewModel = viewModel(),
    cardViewModel: CardViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId() ?: return

    var json by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Import seta (JSON)", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = json,
            onValueChange = { json = it },
            label = { Text("Zalijepi JSON ovdje") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            minLines = 10
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                setViewModel.importSetFromJson(userId, json, cardViewModel)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import set")
        }
    }
}
