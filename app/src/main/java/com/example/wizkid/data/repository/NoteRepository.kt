package com.example.wizkid.data.repository

import android.content.Context
import com.example.wizkid.data.local.AppDatabase
import com.example.wizkid.data.local.NoteDao
import com.example.wizkid.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    fun getNotesForSubject(subjectId: String): Flow<List<NoteEntity>> {
        return noteDao.getNotesForSubject(subjectId)
    }

    suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    // VVVVVV ADD THIS METHOD VVVVVV
    suspend fun getNoteById(noteId: String): NoteEntity? {
        return noteDao.getNoteById(noteId)
    }


    companion object {
        @Volatile
        private var INSTANCE: NoteRepository? = null

        fun getInstance(context: Context): NoteRepository {
            return INSTANCE ?: synchronized(this) {
                val database = AppDatabase.getDatabase(context)
                val instance = NoteRepository(database.noteDao())
                INSTANCE = instance
                instance
            }
        }
    }
}