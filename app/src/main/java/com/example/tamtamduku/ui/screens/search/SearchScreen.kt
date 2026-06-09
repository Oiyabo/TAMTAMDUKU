package com.example.tamtamduku.ui.screens.search
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R
import androidx.compose.material3.MaterialTheme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.animation.*
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamtamduku.ui.components.WorkerCard
import com.example.tamtamduku.ui.viewmodels.WorkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchScreen(
    viewModel: WorkerViewModel = viewModel(),
    onBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFilterSheet by remember { mutableStateOf(false) }

    Surface(color = Color(0xFFFFF9F5)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.Undo, "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        placeholder = { Text(stringResource(R.string.search), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            cursorColor = MaterialTheme.colorScheme.onBackground
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                            .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(8.dp))
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.filteredWorkers) { worker ->
                        WorkerCard(
                            worker = worker, 
                            onClick = { onNavigateToDetail(worker.nama) },
                            isFavorite = uiState.favoriteWorkerIds.contains(worker.id),
                            onFavoriteToggle = { viewModel.toggleFavorite(worker.id) }
                        )
                    }
                    if (uiState.filteredWorkers.isEmpty() && !uiState.isLoading) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text(stringResource(R.string.tidak_ada_hasil_yang_cocok), color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }

            if (viewModel.isAnyFilterActive()) {
                ExtendedFloatingActionButton(
                    onClick = viewModel::onResetFilter,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding()
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    icon = { Icon(Icons.Default.Close, null) },
                    text = { Text(stringResource(R.string.reset_filter)) }
                )
            }
        }

        AnimatedVisibility(
            visible = showFilterSheet,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.fillMaxSize()
        ) {
            BackHandler(enabled = showFilterSheet) {
                showFilterSheet = false
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF9F5))
            ) {
                FilterBottomSheetContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    onDismiss = { showFilterSheet = false }
                )
            }
        }
    }
}
