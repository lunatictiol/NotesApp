package com.lunatictiol.noteapp.feature_note.domain.use_cases

import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.domain.repository.NoteRepository

class GetNoteIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id:Int):Note?{
        return repository.getNoteById(id)
    }
}