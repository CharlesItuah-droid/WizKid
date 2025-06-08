package com.example.wizkid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wizkid.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Dummy data for now
private val dummySubjects = listOf(
    Subject(id = "1", name = "Mathematics", description = "Learn algebra, calculus, and more."),
    Subject(id = "2", name = "Science", description = "Explore physics, chemistry, and biology."),
    Subject(id = "3", name = "History", description = "Discover past events and civilizations."),
    Subject(id = "4", name = "English", description = "Improve grammar and literature skills.")
)

class HomeViewModel : ViewModel() {

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            _isLoading.value = true
            // In a real app, you'd fetch this from a repository (which might get it from Room or an API)
            kotlinx.coroutines.delay(1000) // Simulate network delay
            _subjects.value = dummySubjects
            _isLoading.value = false
        }
    }
}