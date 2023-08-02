package com.compose.sample.exoplayer_a

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileListScreen(
    filenames: List<AFile>,
    onFileClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn {
            items(filenames, key = { it.filename }) { file ->
                Text(
                    text = file.filename,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onFileClick(file.filePath)
                        }
                        .padding(5.dp)
                )
                Divider()
            }
        }
    }
}