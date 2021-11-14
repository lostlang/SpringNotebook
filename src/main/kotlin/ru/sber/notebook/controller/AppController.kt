package ru.sber.notebook.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.notebook.model.NoteModel
import ru.sber.notebook.service.NotebookService

@Controller
@RequestMapping("/app")
class AppController @Autowired constructor(val notebookService: NotebookService) {

    @GetMapping("/add")
    fun addNote(model: Model): String?{
        model.addAttribute("name", "Добавление записи")

        return "add'n'edit"
    }

    @GetMapping("/{id}/edit")
    fun editNote(@PathVariable id: String, model: Model): String?{
        model.addAttribute("name", "Измение записи $id")

        return "add'n'edit"
    }

    @GetMapping("/list")
    fun getNoteByQuery(@RequestParam query: Map<String, String>, model: Model): String {
        val searchResult = notebookService.getByQuery(query)

        model.addAttribute("name", "Список записей по запросу")
        model.addAttribute("result", searchResult)

        return "list"
    }

    @GetMapping("/{id}")
    fun getNoteById(@PathVariable id: String, model: Model): String {
        val searchResult = notebookService.getById(id.toInt())

        model.addAttribute("name", "Запись номер $id")
        model.addAttribute("result", searchResult)

        return "list"
    }

    @GetMapping("/{id}/delete")
    fun deleteNote(@PathVariable id: String, model: Model): String {
        notebookService.delNote(id.toInt())
        model.addAttribute("result", "Записи больше нет")

        return "result"
    }

    @PostMapping("/add")
    fun addNote(@ModelAttribute noteModel: NoteModel, model: Model): String {
        notebookService.addNote(noteModel)
        model.addAttribute("result", "Запись добавлена")

        return "result"
    }

    @PostMapping("/{id}/edit")
    fun editNote(@PathVariable id: String, @ModelAttribute noteModel: NoteModel, model: Model): String {
        notebookService.editNote(id.toInt(), noteModel)
        model.addAttribute("result", "Запись изменена")

        return "result"
    }
}