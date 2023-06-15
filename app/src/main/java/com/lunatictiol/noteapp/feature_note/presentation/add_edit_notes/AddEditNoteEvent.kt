package com.lunatictiol.noteapp.feature_note.presentation.add_edit_notes

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent {
    data class EnteredTitle(val title:String): AddEditNoteEvent()
    data class EnteredContent(val content:String): AddEditNoteEvent()
    data class ChangedTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangedContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class ChangedColor(val color:Int):AddEditNoteEvent()
    object SaveNote:AddEditNoteEvent()
}