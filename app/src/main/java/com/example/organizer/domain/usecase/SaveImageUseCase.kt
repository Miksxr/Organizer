package com.example.organizer.domain.usecase

import android.net.Uri
import com.example.organizer.domain.repository.FileRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(uri: Uri): String? {
        return fileRepository.saveImage(uri)
    }
}