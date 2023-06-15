package com.lunatictiol.noteapp.feature_note.domain.use_cases

import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.domain.repository.NoteRepository

class DeleteNoteUSeCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.deleteNote(note)
    }
}