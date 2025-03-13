package com.example.organizer.presentation.screens.subjects

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.data.mapper.toEntity
import com.example.organizer.domain.model.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Composable
fun SubjectsScreen(viewModel: SubjectViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val subjects by viewModel.subjects.collectAsState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }
    var name by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            coroutineScope.launch {
                val savedPath = viewModel.saveImage(it)
                selectedPhotoUri = savedPath
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Предметы",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(subjects) { subject ->
                SubjectCard(
                    subject = subject,
                    onClick = {
                        selectedSubject = subject
                        name = subject.name
                        teacherName = subject.teacherName
                        selectedPhotoUri = subject.photoPath
                        isEditDialogOpen = true
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { isDialogOpen = true },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить предмет")
        }

        if (isDialogOpen) {
            AddEditSubjectDialog(
                title = "Добавление нового предмета",
                name = name,
                teacherName = teacherName,
                photoUri = selectedPhotoUri,
                onNameChange = { name = it },
                onTeacherChange = { teacherName = it },
                onPhotoSelect = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onConfirm = {
                    viewModel.addSubject(name, teacherName, selectedPhotoUri)
                    isDialogOpen = false
                    name = ""
                    teacherName = ""
                    selectedPhotoUri = null
                },
                onDismiss = {
                    isDialogOpen = false
                    name = ""
                    teacherName = ""
                    selectedPhotoUri = null
                }
            )
        }

        if (isEditDialogOpen && selectedSubject != null) {
            AddEditSubjectDialog(
                title = "Редактирование предмета",
                name = name,
                teacherName = teacherName,
                photoUri = selectedPhotoUri,
                onNameChange = { name = it },
                onTeacherChange = { teacherName = it },
                onPhotoSelect = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onConfirm = {
                    selectedSubject?.let { subject ->
                        viewModel.updateSubject(
                            subject.id,
                            name,
                            teacherName,
                            selectedPhotoUri
                        )
                    }
                    isEditDialogOpen = false
                    name = ""
                    teacherName = ""
                    selectedPhotoUri = null
                },
                onDelete = {
                    selectedSubject?.let { viewModel.deleteSubject(it.toEntity()) }
                    isEditDialogOpen = false
                    name = ""
                    teacherName = ""
                    selectedPhotoUri = null
                },
                onDismiss = {
                    isEditDialogOpen = false
                    name = ""
                    teacherName = ""
                    selectedPhotoUri = null
                }
            )
        }
    }
}

@Composable
private fun SubjectCard(
    subject: Subject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            subject.photoPath?.let { path ->
                val bitmap = remember {
                    BitmapFactory.decodeFile(path)
                }
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Фото предмета",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(60.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = subject.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subject.teacherName,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun AddEditSubjectDialog(
    title: String,
    name: String,
    teacherName: String,
    photoUri: String?,
    onNameChange: (String) -> Unit,
    onTeacherChange: (String) -> Unit,
    onPhotoSelect: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column {
                photoUri?.let { uri ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Превью фото",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(60.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onPhotoSelect,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Выбрать фото", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Выбрать фото", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Название предмета") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = teacherName,
                    onValueChange = onTeacherChange,
                    label = { Text("ФИО преподавателя") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                onDelete?.let {
                    TextButton(
                        onClick = it,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Удалить")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = onConfirm,
                    enabled = name.isNotBlank() && teacherName.isNotBlank()
                ) {
                    Text("Сохранить")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}