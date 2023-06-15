package com.lunatictiol.noteapp.feature_note.domain.use_cases

data class NoteUsecases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUSeCase: DeleteNoteUSeCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteIdUseCase: GetNoteIdUseCase
)
