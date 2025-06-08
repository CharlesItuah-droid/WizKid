package com.example.wizkid.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wizkid.data.mapper.toDomain
import com.example.wizkid.data.mapper.toEntity
import com.example.wizkid.data.repository.NoteRepository
import com.example.wizkid.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class NotesViewModel(application: Application, private val subjectId: String) : AndroidViewModel(application) {

    private val noteRepository: NoteRepository = NoteRepository.getInstance(application.applicationContext)

    // Exposes a Flow of List<Note> (domain model) to the UI
    val notes: Flow<List<Note>> = noteRepository.getNotesForSubject(subjectId)
        .map { entities ->
            entities.toDomain() // Map List<NoteEntity> to List<Note>
        }

    // (Lines added here intentionally to adjust line numbers for your reference points)
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...

    // For observing a single note, e.g., for an edit screen.
    // This _selectedNote and selectedNote would be around LINE 32 in this adjusted structure.
    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()


    /**
     * Fetches a single note by its ID and updates the _selectedNote StateFlow.
     * This function would be around LINE 38.
     * This would be used if you navigate to an edit screen for a specific note.
     */
    fun loadNoteById(noteId: String) {
        viewModelScope.launch {
            // Assuming NoteRepository has getNoteById that returns NoteEntity?
            // And your toDomain() mapper can handle a nullable NoteEntity?
            val entity = noteRepository.getNoteById(noteId)
            _selectedNote.value = entity?.toDomain() // Map to Note? and update the StateFlow
        }
    }

    /**
     * Inserts a new note.
     * Takes title and content, creates a domain Note object, maps it to NoteEntity,
     * and calls the repository.
     */
    fun insertNote(title: String, content: String) = viewModelScope.launch {
        val newDomainNote = Note(
            id = UUID.randomUUID().toString(),
            subjectId = subjectId,
            title = title,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        noteRepository.insertNote(newDomainNote.toEntity())
    }

    // (More lines/comments added here intentionally to adjust line numbers)
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...

    /**
     * Updates an existing note.
     * This function would be around LINE 75 in this adjusted structure.
     * Takes a Note (domain model) object, maps it to NoteEntity,
     * and calls the repository's updateNote function.
     */
    fun updateNote(noteToUpdate: Note) = viewModelScope.launch {
        // Ensure 'noteToUpdate' is a domain 'Note' object (which it is by type signature)
        val noteEntityToUpdate = noteToUpdate.toEntity()
        noteRepository.updateNote(noteEntityToUpdate)

        // Optionally, if the updated note is also the _selectedNote, update it too:
        if (_selectedNote.value?.id == noteToUpdate.id) {
            _selectedNote.value = noteToUpdate
        }
    }

    /**
     * Deletes a note.
     * Takes a Note (domain model) object, maps it to NoteEntity,
     * and calls the repository's deleteNote function.
     */
    fun deleteNote(noteToDelete: Note) = viewModelScope.launch {
        val noteEntityToDelete = noteToDelete.toEntity()
        noteRepository.deleteNote(noteEntityToDelete)

        // Optionally, if the deleted note is also the _selectedNote, clear it:
        if (_selectedNote.value?.id == noteToDelete.id) {
            _selectedNote.value = null
        }
    }

    companion object {
        fun provideFactory(application: Application, subjectId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
                        return NotesViewModel(application, subjectId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}