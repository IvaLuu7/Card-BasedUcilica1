package com.example.card_baseducilica.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.card_baseducilica.data.entity.SetEntity
import com.example.card_baseducilica.data.session.SessionManager
import com.example.card_baseducilica.navigation.Routes
import com.example.card_baseducilica.viewmodel.SetViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {


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
            ) { Text("ODJAVA") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "MOJI SETOVI",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                editingSet = null
                title = ""
                description = ""
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("+ Dodaj set") }

        Spacer(modifier = Modifier.height(8.dp))


        OutlinedButton(
            onClick = { navController.navigate(Routes.IMPORT_SET) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Preuzmi set") }

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
                                navController.navigate("${Routes.CARDS_ROUTE}/${set.id}/${set.title}")
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = set.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (!set.description.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(set.description!!)
                                }
                            }

                            Box {
                                IconButton(onClick = { expandedMenuForSetId = set.id }) {
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
                                        text = { Text("Dijeli set") },
                                        onClick = {
                                            setViewModel.exportSetAsJson(set.id, set.title) { json ->
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(
                                                    ClipData.newPlainText("Set JSON", json)
                                                )
                                            }
                                            expandedMenuForSetId = null
                                        }
                                    )


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
                                            setViewModel.deleteSet(setId = set.id, userId = userId)
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


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editingSet == null) "Novi set" else "Uredi set") },
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
                        label = { Text("Opis") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editingSet == null) {
                            setViewModel.addSet(userId, title, description)
                        } else {
                            setViewModel.updateSet(editingSet!!.id, title, description)
                            editingSet = null
                        }
                        showDialog = false
                    }
                ) { Text("Spremi") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Odustani") }
            }
        )
    }
}