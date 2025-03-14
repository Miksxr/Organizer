package com.example.organizer.presentation.screens.grades

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.R
import com.example.organizer.data.local.entity.GradeEntity
import com.example.organizer.presentation.screens.homeworks.DropdownMenuComponent
import com.example.organizer.presentation.screens.subjects.SubjectViewModel

@Composable
fun GradesScreen(
    gradesViewModel: GradesViewModel = hiltViewModel(),
    subjectViewModel: SubjectViewModel = hiltViewModel()
) {
    val subjects by subjectViewModel.subjects.collectAsState()
    val grades by gradesViewModel.grades.collectAsState()
    var selectedSubjectId by remember { mutableStateOf<Int?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var newWorkType by remember { mutableStateOf("") }
    var newGrade by remember { mutableStateOf("") }
    var newDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Выбор предмета
        DropdownMenuComponent(
            subjects = subjects,
            onSubjectSelected = {
                selectedSubjectId = it.id.toInt()
                gradesViewModel.loadGradesForSubject(it.id.toInt())
            }
        )

        // Список оценок
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(grades) { grade ->
                GradeItem(
                    grade = grade,
                    onDelete = { gradesViewModel.deleteGrade(grade) }
                )
            }
        }

        // Кнопка добавления
        FloatingActionButton(
            onClick = { isDialogOpen = true },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить оценку")
        }
    }

    // Диалог добавления
    if (isDialogOpen) {
        AddGradeDialog(
            workType = newWorkType,
            grade = newGrade,
            date = newDate,
            onWorkTypeChange = { newWorkType = it },
            onGradeChange = { newGrade = it },
            onDateChange = { newDate = it },
            onConfirm = {
                selectedSubjectId?.let { id ->
                    gradesViewModel.addGrade(
                        workType = newWorkType,
                        gradeValue = newGrade.toIntOrNull() ?: 0,
                        date = newDate,
                        subjectId = id
                    )
                    isDialogOpen = false
                    newWorkType = ""
                    newGrade = ""
                    newDate = ""
                }
            },
            onDismiss = {
                isDialogOpen = false
                newWorkType = ""
                newGrade = ""
                newDate = ""
            }
        )
    }
}

@Composable
fun GradeItem(
    grade: GradeEntity,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = grade.workType,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Оценка: ${grade.grade}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Дата: ${grade.date}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}

@Composable
fun AddGradeDialog(
    workType: String,
    grade: String,
    date: String,
    onWorkTypeChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новая оценка") },
        text = {
            Column {
                OutlinedTextField(
                    value = workType,
                    onValueChange = onWorkTypeChange,
                    label = { Text("Тип работы (например: Практика 2)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = grade,
                    onValueChange = { onGradeChange(it.filter { c -> c.isDigit() }) },
                    label = { Text("Оценка") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = onDateChange,
                    label = { Text("Дата (дд.мм.гггг)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = workType.isNotBlank() && grade.isNotBlank() && date.isNotBlank()
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}