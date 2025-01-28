package com.example.organizer.presentation.screens.subjects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizer.data.local.entity.SubjectEntity
import javax.security.auth.Subject

@Composable
fun SubjectScreen(viewModel: SubjectViewModel = hiltViewModel()) {
    val subjects by viewModel.subjects.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        SubjectList(subjects = subjects)
        AddSubjectForm { name, teacherName ->
            viewModel.addSubject(name, teacherName)
        }
    }
}

@Composable
fun SubjectList(subjects: List<SubjectEntity>) {
    LazyColumn {
        items(subjects) { subject ->
            Text(text = "${subject.name} - ${subject.teacherName}")
        }
    }
}

@Composable
fun AddSubjectForm(onAddSubject: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }

    Column {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Subject Name") })
        TextField(value = teacherName, onValueChange = { teacherName = it }, label = { Text("Teacher Name") })
        Button(onClick = { onAddSubject(name, teacherName) }) {
            Text("Add Subject")
        }
    }
}