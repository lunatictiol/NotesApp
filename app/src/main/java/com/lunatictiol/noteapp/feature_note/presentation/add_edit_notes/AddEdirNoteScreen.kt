package com.lunatictiol.noteapp.feature_note.presentation.add_edit_notes

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.presentation.add_edit_notes.components.TransparentTextField
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel =hiltViewModel(),
    noteColor:Int

){
    val titleState = viewModel.noteTile.value
    val contentState = viewModel.noteContent.value
    val snackbarHostState = remember { SnackbarHostState() }
    val backgroundAnimatable = remember{
        Animatable(
            Color(if (noteColor!=-1) noteColor else viewModel.noteColor.value )
        )

    }
   BackHandler() {
       viewModel.onEvent(AddEditNoteEvent.SaveNote)

   }

    val scope  = rememberCoroutineScope()
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event->
             when(event){
                 is AddEditNoteViewModel.UiEvent.ShowSnackbar->{
                     snackbarHostState.showSnackbar(
                         message = event.message
                     )
                 }
                 is AddEditNoteViewModel.UiEvent.SaveNotes->{
                    navController.navigateUp()
                 }


             }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {viewModel.onEvent(AddEditNoteEvent.SaveNote)}
            )
            {
                Icon(imageVector = Icons.Default.Create, contentDescription ="save note" )

                
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundAnimatable.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween


            ) {
                 Note.noteColors.forEach { color ->
                 val colorInt = color.toArgb()
                 Box(modifier = Modifier
                     .size(50.dp)
                     .shadow(15.dp, CircleShape)
                     .clip(CircleShape)
                     .background(color)
                     .border(
                         width = 3.dp,
                         color = if (viewModel.noteColor.value == colorInt) Color.Black else Color.Transparent,
                         shape = CircleShape
                     )
                     .clickable {
                         scope.launch {
                             backgroundAnimatable.animateTo(
                                 targetValue = Color(colorInt),
                                 animationSpec = tween(
                                     delayMillis = 500
                                 )
                             )
                         }
                         viewModel.onEvent(AddEditNoteEvent.ChangedColor(colorInt))
                     }
                 )

                 }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                                viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },

                onFocusChange ={
                    viewModel.onEvent(AddEditNoteEvent.ChangedTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineLarge

            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },

                onFocusChange ={
                    viewModel.onEvent(AddEditNoteEvent.ChangedContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()

            )


        }
    }
    
    
}

