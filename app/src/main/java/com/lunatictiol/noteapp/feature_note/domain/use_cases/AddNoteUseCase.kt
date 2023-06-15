package com.lunatictiol.noteapp.feature_note.domain.use_cases

import com.lunatictiol.noteapp.feature_note.domain.model.InvalidNoteException
import com.lunatictiol.noteapp.feature_note.domain.model.Note
import com.lunatictiol.noteapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note:Note)
    {
        if (note.title.isBlank())
            throw InvalidNoteException("Title can't be empty")
        if (note.content.isBlank())
            throw InvalidNoteException("note can't be empty")
        repository.insertNote(note)
    }
}