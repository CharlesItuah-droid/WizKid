package com.example.wizkid.data.mapper

import com.example.wizkid.data.local.NoteEntity
import com.example.wizkid.model.Note

// Maps NoteEntity (data layer) to Note (domain layer)
fun NoteEntity.toDomain(): Note {
    return Note(
        id = this.id,
        subjectId = this.subjectId,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp
    )
}

// Maps a list of NoteEntity to a list of Note
fun List<NoteEntity>.toDomain(): List<Note> {
    return this.map { it.toDomain() }
}

// Maps Note (domain layer) to NoteEntity (data layer) - for saving/updating
fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        subjectId = this.subjectId,
        title = this.title,
        content = this.content,
        timestamp = this.timestamp
    )
}