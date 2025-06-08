package com.example.wizkid.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // ... your existing methods like insertNote, updateNote, deleteNote ...

    @Query("SELECT * FROM notes WHERE subjectId = :subjectId ORDER BY timestamp DESC")
    fun getNotesForSubject(subjectId: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // VVVVVV ADD THIS METHOD VVVVVV
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): NoteEntity? // It can be nullable if note not found

}