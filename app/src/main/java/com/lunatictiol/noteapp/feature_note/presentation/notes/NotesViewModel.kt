package com.lunatictiol.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.domain.use_cases.GetNotesUseCase
import com.lunatictiol.noteapp.feature_note.domain.use_cases.NoteUsecases
import com.lunatictiol.noteapp.feature_note.domain.util.NoteOrder
import com.lunatictiol.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUsecases
):ViewModel(){
    private val _state= mutableStateOf<NoteState>(NoteState())
    val state: State<NoteState> = _state
    private var recentlyDeletedNote:Note?=null
    private var getNotesJob:Job?=null
    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }
    fun onEvent(event: NoteEvents){
        when(event){
            is NoteEvents.Order->{
                if (state.value.noteOrder::class==event.noteOrder::class &&
                        state.value.noteOrder.orderType==event.noteOrder.orderType)
                {
                    return
                }
                getNotes(event.noteOrder)



            }
            is NoteEvents.DeleteNote->{
                viewModelScope.launch {
                    noteUseCases.deleteNoteUSeCase(event.note)
                    recentlyDeletedNote= event.note
                }
            }
            is NoteEvents.RestoreNote->{
                viewModelScope.launch {
                      noteUseCases.addNoteUseCase(recentlyDeletedNote ?:return@launch)
                    recentlyDeletedNote=null
                }
            }
            is NoteEvents.ToggleOrderSelection->{
                _state.value=state.value.copy(
                    isOrderMenuSelected = !state.value.isOrderMenuSelected
                )
            }


        }
    }
private fun getNotes(noteOrder:NoteOrder){
    getNotesJob?.cancel()
    getNotesJob=noteUseCases.getNotesUseCase(noteOrder).onEach {notes ->
    _state.value=state.value.copy(
        NoteList = notes,
        noteOrder=noteOrder
        )

    }.launchIn(viewModelScope
    )
}
}