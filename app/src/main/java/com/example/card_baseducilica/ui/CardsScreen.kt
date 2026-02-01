package com.example.card_baseducilica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.card_baseducilica.navigation.Routes
import com.example.card_baseducilica.viewmodel.CardViewModel
import com.example.card_baseducilica.data.entity.CardEntity

@Composable
fun CardsScreen(
    navController: NavController,
    setId: Int,
    setTitle: String,
    cardViewModel: CardViewModel = viewModel()
) {
    val showOnlyFavorites by cardViewModel.showOnlyFavorites.collectAsState()
    val cards by cardViewModel.cards.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var editingCard by remember { mutableStateOf<CardEntity?>(null) }
    var expandedMenuForCardId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        cardViewModel.loadCards(setId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "$setTitle",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // + Dodaj karticu
        Button(
            onClick = {
                editingCard = null
                question = ""
                answer = ""
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Dodaj karticu")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Učenje
        Button(
            onClick = {
                navController.navigate(
                    "${Routes.LEARNING}/$setId/$setTitle/$showOnlyFavorites"
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (showOnlyFavorites)
                    "Uči samo favorite"
                else
                    "Uči sve kartice"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter
        OutlinedButton(
            onClick = { cardViewModel.toggleFavoriteFilter(setId) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (showOnlyFavorites)
                    "Prikaži sve kartice"
                else
                    "Prikaži samo favorite"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (cards.isEmpty()) {
            Text("Nema kartica u ovom setu.")
        } else {
            LazyColumn {
                items(cards) { card ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = card.question,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    cardViewModel.toggleFavorite(card)
                                }
                            ) {
                                Icon(
                                    imageVector = if (card.isFavorite)
                                        Icons.Filled.Favorite
                                    else
                                        Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favorit",
                                    tint = if (card.isFavorite)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }

                            Box {
                                IconButton(onClick = { expandedMenuForCardId = card.id }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu"
                                    )
                                }

                                DropdownMenu(
                                    expanded = expandedMenuForCardId == card.id,
                                    onDismissRequest = { expandedMenuForCardId = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Uredi") },
                                        onClick = {
                                            editingCard = card
                                            question = card.question
                                            answer = card.answer
                                            showDialog = true
                                            expandedMenuForCardId = null
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text("Obriši") },
                                        onClick = {
                                            cardViewModel.deleteCard(card.id, setId)
                                            expandedMenuForCardId = null
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
            title = { Text(if (editingCard == null) "Nova kartica" else "Uredi karticu") },
            text = {
                Column {
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Pitanje") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("Odgovor") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editingCard == null) {
                            cardViewModel.addCard(setId, question, answer)
                        } else {
                            cardViewModel.updateCard(editingCard!!.id, question, answer)
                            cardViewModel.loadCards(setId)
                            editingCard = null
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