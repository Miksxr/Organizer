package com.example.organizer.domain.repository

import android.net.Uri

interface FileRepository {
    suspend fun saveImage(uri: Uri): String?
}