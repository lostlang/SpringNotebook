package ru.sber.notebook.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.sber.notebook.NoteModel
import ru.sber.notebook.service.NotebookService
import java.util.concurrent.ConcurrentHashMap

@Controller
@RequestMapping("/api")
class ApiController @Autowired constructor(val notebookService: NotebookService){

    @PostMapping("/add")
    fun addNote(@RequestBody note: NoteModel): ResponseEntity<String> {
        notebookService.addNote(note)
        return ResponseEntity("Записи добавлена",
                              HttpStatus.CREATED)
    }

    @GetMapping("/list")
    fun getNoteByQuery(@RequestParam query: Map<String, String>): ResponseEntity<ConcurrentHashMap<Int, NoteModel>> {
        val searchResult = notebookService.getByQuery(query)

        return ResponseEntity(searchResult, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getNoteById(@PathVariable id: String): ResponseEntity<ConcurrentHashMap<Int, NoteModel>> {
        return ResponseEntity(notebookService.getById(id.toInt()),
                              HttpStatus.OK)
    }

    @PutMapping("/{id}/edit")
    fun editNote(@PathVariable id: String, @RequestBody note: NoteModel): ResponseEntity<String> {
        notebookService.editNote(id.toInt(), note)
        return ResponseEntity("Запись изменена",
                              HttpStatus.OK)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteNote(@PathVariable id: String): ResponseEntity<String> {
        notebookService.delNote(id.toInt())
        return ResponseEntity("Записи больше нет",
                              HttpStatus.OK)
    }
}