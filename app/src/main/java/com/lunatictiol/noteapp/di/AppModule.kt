package com.lunatictiol.noteapp.di

import android.app.Application
import androidx.room.Room
import com.lunatictiol.noteapp.feature_note.data.data_source.NoteDatabase
import com.lunatictiol.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.lunatictiol.noteapp.feature_note.domain.repository.NoteRepository
import com.lunatictiol.noteapp.feature_note.domain.use_cases.AddNoteUseCase
import com.lunatictiol.noteapp.feature_note.domain.use_cases.DeleteNoteUSeCase
import com.lunatictiol.noteapp.feature_note.domain.use_cases.GetNoteIdUseCase
import com.lunatictiol.noteapp.feature_note.domain.use_cases.GetNotesUseCase
import com.lunatictiol.noteapp.feature_note.domain.use_cases.NoteUsecases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesDataBase(app:Application):NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()

    }

    @Provides
    @Singleton
    fun providesRepository(db:NoteDatabase):NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }
    @Provides
    @Singleton
    fun providesUseCases(repository: NoteRepository):NoteUsecases{
        return NoteUsecases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUSeCase = DeleteNoteUSeCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteIdUseCase = GetNoteIdUseCase(repository)
        )
    }

}