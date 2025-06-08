package com.example.wizkid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wizkid.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Dummy data for now - same as HomeViewModel's, but we'll filter
private val allSubjects = listOf(
    Subject(id = "1", name = "Mathematics", description = "Learn algebra, calculus, and more."),
    Subject(id = "2", name = "Science", description = "Explore physics, chemistry, and biology."),
    Subject(id = "3", name = "History", description = "Discover past events and civilizations."),
    Subject(id = "4", name = "English", description = "Improve grammar and literature skills.")
)

class SubjectDetailViewModel(private val subjectId: String) : ViewModel() {

    private val _subject = MutableStateFlow<Subject?>(null)
    val subject: StateFlow<Subject?> = _subject.asStateFlow()

    init {
        loadSubjectDetails()
    }

    private fun loadSubjectDetails() {
        viewModelScope.launch {
            // In a real app, you'd fetch this from a repository by ID
            _subject.value = allSubjects.find { it.id == subjectId }
        }
    }

    companion object {
        fun provideFactory(subjectId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(SubjectDetailViewModel::class.java)) {
                        return SubjectDetailViewModel(subjectId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}