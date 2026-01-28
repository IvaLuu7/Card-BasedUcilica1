package com.example.card_baseducilica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.card_baseducilica.viewmodel.SetViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import com.example.card_baseducilica.navigation.Routes
import com.example.card_baseducilica.data.session.SessionManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun MySetsScreen(
    navController: NavController,
    setViewModel: SetViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    // USER ID IZ SESSIONA
    val userId = sessionManager.getUserId()

    // Ako nema sessiona → vrati na login (sigurnosna mreža)
    LaunchedEffect(userId) {
        if (userId == null) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0)
            }
        }
    }

    if (userId == null) return

    val sets by setViewModel.sets.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var expandedMenuForSetId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        setViewModel.loadSets(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // LOGOUT GUMB
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    sessionManager.clearSession()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                    }
                }
            ) {
                Text("ODJAVA")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "MOJI SETOVI",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Dodaj set")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (sets.isEmpty()) {
            Text("Nema setova. Dodaj prvi set.")
        } else {
            LazyColumn {
                items(sets) { set ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                navController.navigate(
                                    "${Routes.CARDS_ROUTE}/${set.id}/${set.title}"
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = set.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (!set.description.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(set.description)
                                }
                            }

                            Box {
                                IconButton(
                                    onClick = { expandedMenuForSetId = set.id }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu"
                                    )
                                }

                                DropdownMenu(
                                    expanded = expandedMenuForSetId == set.id,
                                    onDismissRequest = { expandedMenuForSetId = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Obriši") },
                                        onClick = {
                                            setViewModel.deleteSet(
                                                setId = set.id,
                                                userId = userId
                                            )
                                            expandedMenuForSetId = null
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // DIALOG ZA DODAVANJE SETA
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Novi set") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Naziv seta") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Opis (opcionalno)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        setViewModel.addSet(
                            userId = userId,
                            title = title,
                            description = description
                        )
                        title = ""
                        description = ""
                        showDialog = false
                    }
                ) {
                    Text("Spremi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Odustani")
                }
            }
        )
    }
}