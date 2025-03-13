package com.example.organizer.data.repositoryimpl

import android.content.Context
import android.net.Uri
import com.example.organizer.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val context: Context
) : FileRepository {
    override suspend fun saveImage(uri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = "img_${System.currentTimeMillis()}.jpg"
                val file = File(context.filesDir, fileName)

                FileOutputStream(file).use { output ->
                    inputStream?.copyTo(output)
                }
                file.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }
}