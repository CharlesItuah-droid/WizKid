package com.example.wizkid.ui.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wizkid.model.Note // Domain model for Note
import com.example.wizkid.model.Subject // Assuming you have a Subject domain model
import com.example.wizkid.ui.viewmodel.NotesViewModel
import com.example.wizkid.ui.viewmodel.SubjectDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    subjectId: String?, // Nullable: can be null if no subject is selected/passed
    subjectDetailViewModel: SubjectDetailViewModel = viewModel(
        factory = SubjectDetailViewModel.provideFactory(subjectId ?: "") // Handles null subjectId for VM init
    ),
    // NotesViewModel is conditionally created only if subjectId is not null
    notesViewModel: NotesViewModel? = subjectId?.let {
        val application = LocalContext.current.applicationContext as Application
        viewModel(factory = NotesViewModel.provideFactory(application, it))
    },
    onNavigateToEditNote: (noteId: String) -> Unit,
    onNavigateUp: () -> Unit
) {
    // Collect the subject state from the SubjectDetailViewModel
    // 'subjectFromVM' can be null initially or if the subjectId doesn't correspond to a subject
    val subjectFromVM: Subject? by subjectDetailViewModel.subject.collectAsState()

    // Collect notes if notesViewModel is available, otherwise default to empty list
    // This needs to be outside the conditional rendering of subject details
    // if the FAB to add notes should be visible even when subject details are loading.
    // However, it logically depends on a valid subjectId for notesViewModel to exist.
    val notes: List<Note> = if (subjectId != null && notesViewModel != null) {
        notesViewModel.notes.collectAsState(initial = emptyList()).value
    } else {
        emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Display title based on loaded subject or default if still loading/error
                    Text(subjectFromVM?.name ?: if (subjectId != null) "Loading..." else "Subject Details")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            // FAB should only be active if there's a valid subjectId and thus a notesViewModel
            if (subjectId != null && notesViewModel != null) {
                FloatingActionButton(onClick = {
                    notesViewModel.insertNote(
                        "New Note: ${subjectFromVM?.name ?: "Untitled"}",
                        "Content..."
                    )
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Note")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .padding(16.dp),      // Apply overall screen padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // --- Conditional UI Rendering Logic ---

            // Case 1: No subjectId was passed to this screen.
            if (subjectId == null) {
                Text(
                    "No subject selected or ID is missing.",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                // Optionally, add a button to navigate back or to a selection screen
            }
            // Case 2: A subjectId was provided.
            else {
                // Now, check the state of 'subjectFromVM' loaded by the SubjectDetailViewModel
                val currentSubject = subjectFromVM // Local val for smart casting and clarity

                if (currentSubject != null) {
                    // --- SUBJECT DATA SUCCESSFULLY LOADED ---
                    // This is where line ~84 would be, and currentSubject is guaranteed not null.
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                        Text(
                            "Details for: ${currentSubject.name}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            currentSubject.description ?: "No description available.",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Notes:", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        // notesViewModel should be non-null here because subjectId is non-null
                        if (notesViewModel == null) { // Defensive check, should ideally not happen
                            Text("Error: Notes ViewModel not available.")
                        } else if (notes.isEmpty()) {
                            Text("No notes yet. Tap the '+' button to add one.")
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(notes) { note ->
                                    NoteItem(
                                        note = note,
                                        onNoteClick = { selectedNote ->
                                            onNavigateToEditNote(selectedNote.id)
                                        },
                                        onDeleteClick = { noteToDelete ->
                                            // Ensure notesViewModel is used here
                                            notesViewModel.deleteNote(noteToDelete)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                // Case 2b: subjectId is valid, but subjectFromVM is still null
                // This means data is loading or an error occurred within SubjectDetailViewModel.
                else {
                    // This is where line ~81 would be if the outer 'if (subjectId == null)' was false.
                    // At this point, subjectId IS NOT NULL, but subjectFromVM IS NULL.
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    Text(
                        "Loading subject details for ID: $subjectId...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    // TODO: You could enhance this by having specific error/loading states
                    // in your SubjectDetailViewModel to show more informative messages.
                    // For example:
                    // if (subjectDetailViewModel.isLoading.collectAsState().value) { ... }
                    // else if (subjectDetailViewModel.error.collectAsState().value != null) { ... }
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNoteClick(note) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium
                )
                if (note.content.isNotBlank()) {
                    Text(
                        text = note.content.take(120) + if (note.content.length > 120) "..." else "",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }
                Text(
                    text = "Created: ${dateFormat.format(Date(note.timestamp))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { onDeleteClick(note) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Note",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}