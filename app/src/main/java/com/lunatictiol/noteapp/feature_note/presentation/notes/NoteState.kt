package com.lunatictiol.noteapp.feature_note.presentation.notes

import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.domain.util.NoteOrder
import com.lunatictiol.noteapp.feature_note.domain.util.OrderType

data class NoteState(
    val NoteList: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderMenuSelected: Boolean = false
)
