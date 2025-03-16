package com.example.organizer.presentation.screens.homeworks

import android.service.autofill.DateTransformation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.data.local.entity.HomeworkEntity
import com.example.organizer.domain.model.Subject
import com.example.organizer.presentation.screens.subjects.SubjectViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import java.util.*

@Composable
fun HomeworksScreen(
    viewModel: HomeworksViewModel = hiltViewModel(),
    subjectViewModel: SubjectViewModel = hiltViewModel()
) {
    val subjects by subjectViewModel.subjects.collectAsState()
    val homeworks by viewModel.homeworks.collectAsState()
    var selectedSubjectId by remember { mutableStateOf<Int?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newDueDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Домашка",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 12.dp)
        )

        DropdownMenuComponent(
            subjects = subjects,
            onSubjectSelected = {
                selectedSubjectId = it.id.toInt()
                viewModel.loadHomeworksForSubject(it.id.toInt())
            }
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(homeworks) { homework ->
                HomeworkItem(
                    homework = homework,
                    onToggleComplete = { viewModel.toggleHomeworkCompletion(homework) },
                    onDelete = { viewModel.deleteHomework(homework) }
                )
            }
        }

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
            title = newTitle,
            description = newDescription,
            dueDate = newDueDate,
            onTitleChange = { newTitle = it },
            onDescriptionChange = { newDescription = it },
            onDueDateChange = { newDueDate = it },
            onConfirm = {
                selectedSubjectId?.let {
                    viewModel.addHomework(
                        title = newTitle,
                        description = newDescription,
                        dueDate = newDueDate,
                        subjectId = it
                    )
                    isDialogOpen = false
                    newTitle = ""
                    newDescription = ""
                    newDueDate = ""
                }
            },
            onDismiss = {
                isDialogOpen = false
                newTitle = ""
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
                    text = homework.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = homework.description)
                Text(
                    text = "До ${homework.dueDate}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null)
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
    title: String,
    description: String,
    dueDate: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDueDateChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var isDateValid by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новое задание") },
        text = {
            Column {
                // Поле для названия
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Название работы") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Поле для описания
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Подробное описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Поле для даты с маской и валидацией
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = {
                        onDueDateChange(it)
                        isDateValid = isValidDate(it)
                    },
                    label = { Text("Дата сдачи") },
                    placeholder = { Text("дд.мм.гггг") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, "Выбрать дату")
                        }
                    },
                    isError = !isDateValid,
                    visualTransformation = DateTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (!isDateValid) {
                    Text(
                        "✖ Неверный формат! Используйте дд.мм.гггг",
                        color = Color.Red,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = title.isNotBlank() && isDateValid
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

    // Диалог выбора даты
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                onDueDateChange(date)
                isDateValid = true
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    selectedDate?.let {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                            timeInMillis = it
                        }
                        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                        onDateSelected(dateFormat.format(calendar.time))
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// Валидация даты
private fun isValidDate(date: String): Boolean {
    return try {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
            isLenient = false
        }.parse(date)
        true
    } catch (e: ParseException) {
        false
    }
}

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(8) // Ограничиваем длину
        val formatted = buildString {
            trimmed.forEachIndexed { index, c ->
                if (index == 2 || index == 4) append('.')
                append(c)
            }
        }
        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = DateOffsetMapper
        )
    }

    private object DateOffsetMapper : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = when (offset) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 4 // После точки
            4 -> 5
            5 -> 6
            6 -> 8 // После второй точки
            7 -> 9
            else -> 10
        }

        override fun transformedToOriginal(offset: Int): Int = when (offset) {
            in 0..2 -> offset
            in 3..4 -> offset - 1
            in 5..7 -> offset - 2
            else -> 8
        }
    }
}