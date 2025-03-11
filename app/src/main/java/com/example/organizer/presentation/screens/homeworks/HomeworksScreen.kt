package com.example.organizer.presentation.screens.homeworks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.model.Subject
import com.example.organizer.presentation.screens.subjects.SubjectViewModel

@Composable
fun HomeworksScreen(
    viewModel: HomeworksViewModel = hiltViewModel(),
    subjectViewModel: SubjectViewModel = hiltViewModel()
) {
    val subjects by subjectViewModel.subjects.collectAsState()
    val homeworks by viewModel.homeworks.collectAsState()
    var selectedSubjectId by remember { mutableStateOf<Int?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var newDescription by remember { mutableStateOf("") }
    var newDueDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Выбор предмета
        DropdownMenuComponent(
            subjects = subjects,
            onSubjectSelected = {
                selectedSubjectId = it.id.toInt()
                viewModel.loadHomeworksForSubject(it.id.toInt())
            }
        )

        // Список домашних заданий
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(homeworks) { homework ->
                HomeworkItem(
                    homework = homework,
                    onToggleComplete = { viewModel.toggleHomeworkCompletion(homework) },
                    onDelete = { viewModel.deleteHomework(homework) }
                )
            }
        }

        // Кнопка добавления
        FloatingActionButton(
            onClick = { isDialogOpen = true },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Homework")
        }
    }

    // Диалог добавления
    if (isDialogOpen) {
        AddHomeworkDialog(
            description = newDescription,
            dueDate = newDueDate,
            onDescriptionChange = { newDescription = it },
            onDueDateChange = { newDueDate = it },
            onConfirm = {
                selectedSubjectId?.let {
                    viewModel.addHomework(newDescription, newDueDate, it)
                    isDialogOpen = false
                    newDescription = ""
                    newDueDate = ""
                }
            },
            onDismiss = {
                isDialogOpen = false
                newDescription = ""
                newDueDate = ""
            }
        )
    }
}

@Composable
fun HomeworkItem(
    homework: HomeworkEntity,
    onToggleComplete: () -> Unit,
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
            Checkbox(
                checked = homework.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = homework.description,
                    style = if (homework.isCompleted) {
                        MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.LineThrough
                        )
                    } else {
                        MaterialTheme.typography.bodyMedium
                    }
                )
                Text(
                    text = "Срок: ${homework.dueDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(
    subjects: List<Subject>,
    onSubjectSelected: (Subject) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Выберите предмет") }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedText)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Выбрать предмет")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            subjects.forEach { subject ->
                DropdownMenuItem(
                    text = { Text(subject.name) },
                    onClick = {
                        selectedText = subject.name
                        onSubjectSelected(subject)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddHomeworkDialog(
    description: String,
    dueDate: String,
    onDescriptionChange: (String) -> Unit,
    onDueDateChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новое задание") },
        text = {
            Column {
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Описание задания") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = onDueDateChange,
                    label = { Text("Срок выполнения (дд.мм.гггг)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = description.isNotBlank() && dueDate.isNotBlank()
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