package com.compose.sample.exoplayer_a

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.compose.sample.exoplayer_a.ui.theme.ExoplayerATheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 0
        )

        val afileDir = "${Environment.getExternalStorageDirectory()}/Quark/Download/"
        val aFilesDir = File(afileDir)
        val afiles = aFilesDir.listFiles()?.filter { file ->
            file.extension == "m3u8"
        }?.map { f ->
            AFile(f.name, f.absolutePath)
        }

        setContent {
            ExoplayerATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    afiles?.let {
                        FileListScreen(filenames = it, onFileClick = { filepath ->
                            startActivity(Intent(this@MainActivity, PlayerActivity::class.java)
                                .apply {
                                    putExtra("filepath", filepath)
                                }
                            )
                        })
                    }
                }
            }
        }
    }
}
