package ru.sber.notebook.service

import org.springframework.stereotype.Service
import ru.sber.notebook.NoteModel
import ru.sber.notebook.NotebookModel
import java.util.concurrent.ConcurrentHashMap

@Service
class NotebookService {
    private val notebook = NotebookModel()

    fun addNote(note: NoteModel) = notebook.addNote(note)

    fun delNote(id: Int) = notebook.delNoteForId(id)

    fun editNote(id: Int, note: NoteModel) = notebook.editNote(id, note)

    fun getAll() = notebook.getAll()

    fun getById(id: Int): ConcurrentHashMap<Int, NoteModel> {
        val out: ConcurrentHashMap<Int, NoteModel> = ConcurrentHashMap()

        val value = notebook.getById(id)
        if (value != null){
            out[0] = value
        }

        return out
    }
}