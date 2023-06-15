package com.lunatictiol.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgs
import androidx.navigation.navArgument
import com.lunatictiol.noteapp.feature_note.presentation.add_edit_notes.AddEditNoteScreen
import com.lunatictiol.noteapp.feature_note.presentation.notes.NotesScreen
import com.lunatictiol.noteapp.feature_note.presentation.util.Screen
import com.lunatictiol.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                    startDestination = Screen.NoteScreen.route){
                        composable(route = Screen.NoteScreen.route){
                            NotesScreen(navController = navController)
                        }
                        composable(route = Screen.AddEditScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                            ){
                                    type= NavType.IntType
                                    defaultValue =-1
                                },
                                navArgument(
                                    name = "noteColor"
                                ){
                                    type= NavType.IntType
                                    defaultValue =-1
                                }
                            )

                        )
                        {
                            val color = it.arguments?.getInt("noteColor")?: -1
                            AddEditNoteScreen(navController = navController, noteColor =color )
                        }

                    }
                    
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteAppTheme {

    }
}