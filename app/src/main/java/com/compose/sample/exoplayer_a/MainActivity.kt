package com.compose.sample.exoplayer_a

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.compose.sample.exoplayer_a.ui.theme.ExoplayerATheme
import kotlinx.coroutines.launch

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

                    var isEditDir by remember { mutableStateOf(false) }
                    var aFileDir by remember { mutableStateOf(adir) }

                    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            TopAppBar(
                                title = {
                                    TextField(
                                        value = aFileDir,
                                        onValueChange = {
                                            aFileDir = it
                                        },
                                        placeholder = {
                                            Text(text = "set dir")
                                        },
                                        enabled = isEditDir,
                                    )

                                },
                                actions = {
                                    if (!isEditDir) {
                                        IconButton(onClick = {
                                            isEditDir = true
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                        }
                                    } else {
                                        IconButton(onClick = {
                                            isEditDir = false
                                            scope.launch {
                                                viewModel.setDir(aFileDir)
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Done,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(onClick = {
                                            isEditDir = false
                                            aFileDir = adir
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                },
                                scrollBehavior = scrollBehavior
                            )

                        }
                    ) { values ->
                        FileListScreen(
                            modifier = Modifier.padding(values),
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
