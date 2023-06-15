package com.lunatictiol.noteapp.feature_note.presentation.add_edit_notes


import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lunatictiol.noteapp.feature_note.domain.model.InvalidNoteException
import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.domain.use_cases.NoteUsecases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCase: NoteUsecases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    sealed class UiEvent{
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNotes: UiEvent()



    }

    private val _noteTitle= mutableStateOf(NoteFieldStates(
        hint = "Enter Title..."
    ))
    val noteTile: State<NoteFieldStates> =_noteTitle

    private val _noteContent= mutableStateOf(NoteFieldStates(
        hint = "Write Your Note Here...."
    ))
    val noteContent: State<NoteFieldStates> =_noteContent

    private val _noteColor = mutableStateOf<Int>(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId:Int?=null
    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId->
            if (noteId!=-1) {
                viewModelScope.launch {
                    noteUseCase.getNoteIdUseCase(noteId)?.also { note->
                        currentNoteId=note.id
                        _noteTitle.value = noteTile.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value= note.color
                    }
                }
            }
    }
    }

    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle->{
                _noteTitle.value = noteTile.value.copy(text = event.title)
            }
            is AddEditNoteEvent.ChangedTitleFocus->{
                _noteTitle.value = noteTile.value.copy(isHintVisible = !event.focusState.isFocused &&
                        noteTile.value.text.isBlank())
            }
            is AddEditNoteEvent.EnteredContent->{
                _noteContent.value = noteContent.value.copy(text = event.content)
            }

            is AddEditNoteEvent.ChangedContentFocus->{
                _noteContent.value = noteContent.value.copy(isHintVisible = !event.focusState.isFocused &&
                        noteContent.value.text.isBlank())
            }

            is AddEditNoteEvent.ChangedColor->{
                _noteColor.value = event.color
            }


            is AddEditNoteEvent.SaveNote->{
                viewModelScope.launch {
                    try {
                        noteUseCase.addNoteUseCase(
                            note = Note(
                                title = noteTile.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(
                            UiEvent.SaveNotes
                        )

                    }catch (e:InvalidNoteException){
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            message = e.message?: "Error while saving the note"
                        ))
                    }
                }
            }

        }
    }
    







}