package com.example.organizer.presentation.screens.subjects

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.data.local.entity.SubjectEntity
import com.example.organizer.domain.usecase.Subject
import com.example.organizer.domain.usecase.toEntity

@Composable
fun SubjectsScreen(viewModel: SubjectViewModel = hiltViewModel()) {
    val subjects by viewModel.subjects.collectAsState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }
    var name by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Предметы",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(subjects) { subject ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                selectedSubject = subject
                                name = subject.name
                                teacherName = subject.teacherName
                                isEditDialogOpen = true
                            },
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = subject.name,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(text = subject.teacherName)
                            }
                        }
                    }
                }
            }

            // Диалог добавления
            if (isDialogOpen) {
                AddEditSubjectDialog(
                    title = "Добавление нового предмета",
                    name = name,
                    teacherName = teacherName,
                    onNameChange = { name = it },
                    onTeacherChange = { teacherName = it },
                    onConfirm = {
                        viewModel.addSubject(name, teacherName)
                        isDialogOpen = false
                        name = ""
                        teacherName = ""
                    },
                    onDismiss = {
                        isDialogOpen = false
                        name = ""
                        teacherName = ""
                    }
                )
            }

            // Диалог редактирования
            if (isEditDialogOpen && selectedSubject != null) {
                AddEditSubjectDialog(
                    title = "Редактирование предмета",
                    name = name,
                    teacherName = teacherName,
                    onNameChange = { name = it },
                    onTeacherChange = { teacherName = it },
                    onConfirm = {
                        selectedSubject?.let { subject ->
                            viewModel.updateSubject(
                                subject.id, // Передаем ID
                                name,       // Новое имя
                                teacherName // Новое имя преподавателя
                            )
                        }
                        isEditDialogOpen = false
                    },
                    onDelete = {
                        selectedSubject?.let {
                            viewModel.deleteSubject(it.toEntity())
                        }
                        isEditDialogOpen = false
                    },
                    onDismiss = {
                        isEditDialogOpen = false
                        name = ""
                        teacherName = ""
                    }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                FloatingActionButton(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить предмет")
                }
            }
        }
    }
}

@Composable
private fun AddEditSubjectDialog(
    title: String,
    name: String,
    teacherName: String,
    onNameChange: (String) -> Unit,
    onTeacherChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Название предмета") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                TextButton(onClick = onConfirm) {
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

