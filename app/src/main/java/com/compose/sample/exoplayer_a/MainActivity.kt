package com.compose.sample.exoplayer_a

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compose.sample.exoplayer_a.ui.theme.ExoplayerATheme
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : ComponentActivity() {


    private val viewModel by viewModels<FileViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 0
        )

        setContent {
            ExoplayerATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val fileList by viewModel.fileList.collectAsState()
                    val adir by viewModel.fileDir.collectAsState()
                    val scope = rememberCoroutineScope()

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        var isEditDir by remember { mutableStateOf(false) }
                        var aFileDir by remember { mutableStateOf(adir) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .align(Alignment.TopCenter),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .weight(1f),
                                value = aFileDir,
                                onValueChange = {
                                    aFileDir = it
                                },
                                placeholder = {
                                    Text(text = "set dir")
                                },
                                enabled = isEditDir,
                                maxLines = 2,
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            if (!isEditDir) {
                                Button(onClick = {
                                    isEditDir = true
                                }) {
                                    Text(text = "Set Dir")
                                }
                            } else {
                                Button(onClick = {
                                    isEditDir = false
                                    scope.launch {
                                        viewModel.setDir(aFileDir)
                                    }
                                }) {
                                    Text(text = "OK")
                                }
                                Spacer(modifier = Modifier.width(2.dp))
                                Button(onClick = {
                                    isEditDir = false
                                    aFileDir = adir
                                }) {
                                    Text(text = "Cancel")
                                }
                            }
                        }

                        FileListScreen(
                            modifier = Modifier.padding(top = 64.dp),
                            files = fileList,
                            onFileClick = { filepath ->
                                startActivity(
                                    Intent(this@MainActivity, PlayerActivity::class.java)
                                        .apply {
                                            putExtra("filepath", filepath)
                                        },
                                )
                            },
                            onDeleteFileClick = viewModel::deleteFile,
                        )
                    }
                }
            }
        }
    }
}
