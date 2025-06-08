package com.example.wizkid.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes",
    // Example of a foreign key if you also store Subjects in the DB
    // foreignKeys = [ForeignKey(
    // entity = SubjectEntity::class, // You'd need a SubjectEntity
    // parentColumns = ["id"],
    // childColumns = ["subjectId"],
    // onDelete = ForeignKey.CASCADE
    // )]
)
data class NoteEntity(
    @PrimaryKey val id: String,
    val subjectId: String,
    val title: String,
    val content: String,
    val timestamp: Long
)