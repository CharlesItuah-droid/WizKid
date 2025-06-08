package com.example.wizkid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController // Import for clarity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wizkid.ui.screens.HomeScreen
import com.example.wizkid.ui.screens.NoteDetailScreen // Import for your new screen
import com.example.wizkid.ui.screens.SubjectDetailScreen

object AppDestinations {
    const val HOME_ROUTE = "home"

    const val SUBJECT_DETAIL_ROUTE = "subject_detail" // Base for subject detail
    const val SUBJECT_ID_KEY = "subjectId"           // Key for subjectId argument in subject detail

    // For Note Detail Screen
    private const val NOTE_DETAIL_ROUTE_BASE = "note_detail" // Base for note detail
    const val NOTE_ID_KEY = "noteId"                 // Key for noteId argument in note detail
    // We'll reuse SUBJECT_ID_KEY if it's part of the route to note_detail as well.

    // Define the full route pattern for note detail
    // Example: "note_detail/{subjectId}/{noteId}"
    // This allows passing both subjectId (for ViewModel factory) and noteId
    fun noteDetailRoute(subjectId: String = "{${SUBJECT_ID_KEY}}", noteId: String = "{${NOTE_ID_KEY}}"): String {
        return "$NOTE_DETAIL_ROUTE_BASE/$subjectId/$noteId"
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_ROUTE,
        modifier = modifier
    ) {
        // Destination for HomeScreen
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(
                onNavigateToSubject = { subjectId ->
                    navController.navigate("${AppDestinations.SUBJECT_DETAIL_ROUTE}/$subjectId")
                }
            )
        }

        // Destination for SubjectDetailScreen
        composable(
            route = "${AppDestinations.SUBJECT_DETAIL_ROUTE}/{${AppDestinations.SUBJECT_ID_KEY}}",
            arguments = listOf(navArgument(AppDestinations.SUBJECT_ID_KEY) { type = NavType.StringType })
        ) { backStackEntry ->
            val subjectIdArg = backStackEntry.arguments?.getString(AppDestinations.SUBJECT_ID_KEY)
            SubjectDetailScreen(
                subjectId = subjectIdArg,
                onNavigateToEditNote = { noteId ->
                    // Ensure subjectIdArg is not null before navigating
                    subjectIdArg?.let { currentSubjectId ->
                        // Navigate using the defined route pattern
                        navController.navigate(
                            AppDestinations.noteDetailRoute(subjectId = currentSubjectId, noteId = noteId)
                        )
                    }
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        // VVVVVV YOUR NEW CODE BLOCK PLACED HERE VVVVVV
        composable(
            route = AppDestinations.noteDetailRoute(), // Uses the pattern "note_detail/{subjectId}/{noteId}"
            arguments = listOf(
                navArgument(AppDestinations.SUBJECT_ID_KEY) { type = NavType.StringType },
                navArgument(AppDestinations.NOTE_ID_KEY) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Retrieve arguments safely
            val subjectIdArg = backStackEntry.arguments?.getString(AppDestinations.SUBJECT_ID_KEY)
            val noteIdArg = backStackEntry.arguments?.getString(AppDestinations.NOTE_ID_KEY)

            // Ensure arguments are not null before passing to the screen
            // You might want more robust error handling or a loading state if they are null unexpectedly
            if (subjectIdArg != null && noteIdArg != null) {
                NoteDetailScreen(
                    noteId = noteIdArg,
                    subjectId = subjectIdArg, // Pass subjectId to NoteDetailScreen
                    onNavigateUp = { navController.navigateUp() }
                )
            } else {
                // Handle missing arguments, e.g., navigate back or show an error
                // For simplicity, we can just navigate up, but ideally, you'd log this or show a message.
                navController.navigateUp()
            }
        }
        // ^^^^^^ YOUR NEW CODE BLOCK ENDS HERE ^^^^^^

        // Add more destinations here if needed
    }
}