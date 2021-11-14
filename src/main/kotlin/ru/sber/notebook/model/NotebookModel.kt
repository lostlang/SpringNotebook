package ru.sber.notebook.model

import java.util.concurrent.ConcurrentHashMap

data class NoteModel (
    var name: String,
    var lastname: String,
    var streetName: String,
    var houseNumber: String
)

class NotebookModel {

    private val notebook: ConcurrentHashMap<Int, NoteModel> = ConcurrentHashMap()

    fun addNote(note: NoteModel){
        notebook[notebook.size] = note
    }

    fun delNoteForId(id: Int): NoteModel?{
        var note: NoteModel? = null
        if (id < notebook.size){
            note = notebook[id]
            notebook.remove(id)
        }
        return note
    }

    fun editNote(id: Int, newNote: NoteModel){
        if (newNote.name.isNotEmpty())
            notebook[id]!!.name = newNote.name
        if (newNote.lastname.isNotEmpty())
            notebook[id]!!.lastname = newNote.lastname
        if (newNote.streetName.isNotEmpty())
            notebook[id]!!.streetName = newNote.streetName
        notebook[id]!!.houseNumber = newNote.houseNumber
    }

    fun getAll(): ConcurrentHashMap<Int, NoteModel> {
        return notebook
    }

    fun getById(id: Int): NoteModel?{
        var out: NoteModel? = null
        if (id < notebook.size) {
            out = notebook[id]
        }
        return out
    }

    fun getByQuery(querys: Map<String, String>): ConcurrentHashMap<Int, NoteModel>{
        val resultSearch = ConcurrentHashMap<Int, NoteModel>()
        for (query in querys){
            if (query.key == "id"){
                for (v in query.value.split(',')){
                    if (v.toInt() < notebook.size){
                        resultSearch[resultSearch.size] = notebook[v.toInt()]!!
                    }
                }
            }
        }
        return resultSearch
    }
}