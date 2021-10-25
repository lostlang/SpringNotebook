package ru.sber.notebook

import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ru.sber.notebook.service.NotebookService

@SpringBootTest
@AutoConfigureMockMvc
class controllerMVCTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var notebookService: NotebookService

    @BeforeEach
    fun setUp() {
        notebookService.addNote(NoteModel("Alec", "Lang", "Lenina Street", "10"))
        notebookService.addNote(NoteModel("Roman", "Michailov", "Akimova Street", "23"))
        notebookService.addNote(NoteModel("Ilia", "Lee", "Chapaeva Street", "134"))
        notebookService.addNote(NoteModel("Roman", "Ivanov", "M. Semenovskay Street", "1"))
        notebookService.addNote(NoteModel("Alexander", "Zemskov", "Bosova Street", "218"))
        notebookService.addNote(NoteModel("Kodi", "Benks", "Chapaeva Street", "4"))
    }

    @Test
    fun correctAddNoteToNotebook() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "Alexander")
        note.add("lastname", "Zemskov")
        note.add("streetName", "Chapaeva Street")
        note.add("houseNumber", "121")

        mockMvc.perform(post("/app/add")
            .params(note))
            .andExpect(status().isOk)
            .andExpect(view().name("result"))
            .andExpect(content().string(containsString("Запись добавлена")))
    }

    @Test
    fun resultInListNotebookAll() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(content().string(containsString("M. Semenovskay Street")))
    }

    @Test
    fun resultInListNotebookWithQuery() {
        mockMvc.perform(get("/app/list?id=1,2,5"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @Test
    fun editNote0() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "Alexander")
        note.add("lastname", "Zemskov")
        note.add("streetName", "Chapaeva Street")
        note.add("houseNumber", "121")

        mockMvc.perform(post("/app/0/edit")
            .params(note))
        mockMvc.perform(get("/app/0"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(content().string(containsString("Alexander")))
    }

    @Test
    fun delNote0() {
        mockMvc.perform(get("/app/0/delete"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("result"))
            .andExpect(content().string(containsString("Записи больше нет")))
    }

    @ParameterizedTest
    @MethodSource("different id pages")
    fun idPages(id: Int) {
        mockMvc.perform(get("/app/$id"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    companion object {
        @JvmStatic
        fun `different id pages`() = listOf(
            2, 5, 1
        )
    }
}