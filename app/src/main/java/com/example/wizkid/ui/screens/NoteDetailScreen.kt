package com.example.wizkid.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wizkid.ui.viewmodel.NotesViewModel // Assuming you reuse NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: String, // Passed as navigation argument
    subjectId: String, // Needed if reusing NotesViewModel for its factory
    onNavigateUp: () -> Unit
    // Potentially add onSave: (Note) -> Unit
) {
    val application = LocalContext.current.applicationContext as Application
    // Assuming NotesViewModel is also used for loading/managing single notes
    // Or you might have a dedicated NoteDetailViewModel
    val notesViewModel: NotesViewModel = viewModel(
        factory = NotesViewModel.provideFactory(application, subjectId)
    )

    // *** 1. Load the note when the screen is shown (or noteId changes) ***
    LaunchedEffect(key1 = noteId) {
        notesViewModel.loadNoteById(noteId)
    }

    // *** 2. Collect the selectedNote StateFlow ***
    val selectedNoteFromVM by notesViewModel.selectedNote.collectAsState()

    var title by remember(selectedNoteFromVM) { mutableStateOf(selectedNoteFromVM?.title ?: "") }
    var content by remember(selectedNoteFromVM) { mutableStateOf(selectedNoteFromVM?.content ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedNoteFromVM?.title ?: "Note Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (selectedNoteFromVM == null) {
                Text("Loading note...")
                // Or if it remains null after loading: Text("Note not found.")
            } else {
                // Example: Displaying and allowing edits
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        selectedNoteFromVM?.let { currentNote ->
                            val updatedNote = currentNote.copy(title = title, content = content)
                            notesViewModel.updateNote(updatedNote) // Call the VM's update function
                            onNavigateUp() // Optionally navigate back after save
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
            }
        }
    }
}