package com.compose.sample.exoplayer_a

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun FileListScreen(
    modifier: Modifier,
    files: List<AFile>,
    onFileClick: (String) -> Unit,
    onDeleteFileClick: (String) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(files, key = { it.filename }) { file ->
            var offsetX by remember { mutableStateOf(0f) }
            var isDel by remember { mutableStateOf(false) }
            var showDelDialog by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(text = file.filename,
                    modifier = Modifier
                        .draggable(orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                if (delta < 0) {
                                    offsetX = delta
                                    if (delta < -70f) {
                                        isDel = true
                                    }
                                } else if (delta > 50) {
                                    isDel = false
                                }
                            })
                        .weight(1f)
                        .clickable {
                            onFileClick(file.filePath)
                        }
                        .padding(5.dp))

                AnimatedVisibility(visible = isDel) {
                    IconButton(onClick = {
                        showDelDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
                if (showDelDialog) {
                    AlertDialog(onDismissRequest = {
                        showDelDialog = false
                    }, confirmButton = {
                        TextButton(onClick = {
                            onDeleteFileClick(file.filePath)
                            showDelDialog = false
                        }) {
                            Text(text = "Confirm")
                        }
                    }, dismissButton = {
                        TextButton(onClick = {
                            showDelDialog = false
                            isDel = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }, title = {
                        Text(text = "Delete File")
                    })
                }
            }
            Divider()
        }
    }
}

