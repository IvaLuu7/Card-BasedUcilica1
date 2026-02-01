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
import com.example.card_baseducilica.data.entity.SetEntity
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
    val userId = sessionManager.getUserId() ?: return

    val sets by setViewModel.sets.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var editingSet by remember { mutableStateOf<SetEntity?>(null) }

    var expandedMenuForSetId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        setViewModel.loadSets(userId)
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {
                sessionManager.clearSession()
                navController.navigate(Routes.LOGIN) { popUpTo(0) }
            }) { Text("ODJAVA") }
        }

        Spacer(Modifier.height(8.dp))
        Text("MOJI SETOVI", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            editingSet = null
            title = ""
            description = ""
            showDialog = true
        }, modifier = Modifier.fillMaxWidth()) {
            Text("+ Dodaj set")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = { navController.navigate(Routes.IMPORT_SET) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import set (JSON)")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(sets) { set ->
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable {
                        navController.navigate("${Routes.CARDS_ROUTE}/${set.id}/${set.title}")
                    }
                ) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(set.title, style = MaterialTheme.typography.titleMedium)
                            if (!set.description.isNullOrBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Text(set.description!!)
                            }
                        }

                        Box {
                            IconButton(onClick = { expandedMenuForSetId = set.id }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                            }

                            DropdownMenu(
                                expanded = expandedMenuForSetId == set.id,
                                onDismissRequest = { expandedMenuForSetId = null }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Uredi") },
                                    onClick = {
                                        editingSet = set
                                        title = set.title
                                        description = set.description ?: ""
                                        showDialog = true
                                        expandedMenuForSetId = null
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Obriši") },
                                    onClick = {
                                        setViewModel.deleteSet(set.id, userId)
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editingSet == null) "Novi set" else "Uredi set") },
            text = {
                Column {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Naziv seta") })
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Opis") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editingSet == null) {
                        setViewModel.addSet(userId, title, description)
                    } else {
                        setViewModel.updateSet(editingSet!!.id, title, description)
                        editingSet = null
                    }
                    showDialog = false
                }) { Text("Spremi") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Odustani") }
            }
        )
    }
}
