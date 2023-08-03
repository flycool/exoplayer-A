package com.compose.sample.exoplayer_a

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.io.File

class FileViewModel : ViewModel() {

    private val AFILE_Dir = "${Environment.getExternalStorageDirectory().path}/quark/download/"
    val fileDir = MutableStateFlow(AFILE_Dir)

    val fileList = MutableStateFlow<List<AFile>>(emptyList())

    init {
        fileDir.onEach { dir ->
            getAFiles(dir)
        }.launchIn(viewModelScope)
    }

    suspend fun setDir(dir: String) {
        fileDir.emit(dir)
    }

    fun getAFiles(dir: String) {
        val list = File(dir).listFiles()?.filter { file ->
            file.extension == "m3u8"
        }?.map { f ->
            AFile(f.name, f.absolutePath)
        }
        if (list != null) {
            fileList.value = list
        } else {
            fileList.value = emptyList()
        }
    }

    fun deleteFile(path: String) {
        fileList.update {
            it.filterNot {
                it.filePath == path
            }
        }

        try {
            File(path).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}