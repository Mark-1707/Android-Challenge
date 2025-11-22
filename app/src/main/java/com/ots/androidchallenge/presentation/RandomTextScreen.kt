package com.ots.androidchallenge.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ots.androidchallenge.data.RandomTextEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomTextScreen(
    viewModel: RandomTextViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var length by remember { mutableStateOf("") }
    val allStrings by viewModel.allStrings.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Error -> {
                val msg = (uiState as UiState.Error).message
                snackbarHostState.showSnackbar("Error: $msg")
                viewModel.clearUiState()
            }
            is UiState.Success -> {
                snackbarHostState.showSnackbar("Saved Successfully")
                viewModel.clearUiState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Random String Generator") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                OutlinedTextField(
                    value = length,
                    onValueChange = {
                        length = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    label = { Text("Length") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))
                val isLoading = uiState is UiState.Loading
                Button(
                    onClick = { viewModel.generateRandomString(length.toIntOrNull() ?: 0) },
                    enabled = !isLoading
                ) {
                    Text("Generate")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Generated strings:", style = MaterialTheme.typography.bodySmall)

                Row(
                    modifier = Modifier
                        .clickable {
                            viewModel.deleteAll()
                        }
                ) {
                    Text("Delete All", style = MaterialTheme.typography.bodySmall)
                    Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = "Clear all")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (allStrings.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No generated strings yet.")
                }
            } else {
                LazyColumn {
                    items(allStrings) { item ->
                        RandomStringRow(item, onDelete = { viewModel.deleteById(item.id) })
                    }
                }
            }
        }
        if (uiState is UiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun RandomStringRow(entity: RandomTextEntity, onDelete: (Long) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
        ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entity.value, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(6.dp))
                Text("Requested length: ${entity.length}", style = MaterialTheme.typography.bodyMedium)
                Text("Created: ${entity.created}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = { onDelete(entity.id) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}