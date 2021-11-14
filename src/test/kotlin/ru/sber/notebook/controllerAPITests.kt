package ru.sber.notebook

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ru.sber.notebook.service.NotebookService
import java.util.concurrent.ConcurrentHashMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class controllerAPITests {
    private val headers = HttpHeaders()

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var notebookService: NotebookService

    @BeforeEach
    fun setUp(){
        headers.add("Cookie", logging())

        notebookService.addNote(NoteModel("Alec", "Lang", "Lenina Street", "10"))
        notebookService.addNote(NoteModel("Roman", "Michailov", "Akimova Street", "23"))
        notebookService.addNote(NoteModel("Ilia", "Lee", "Chapaeva Street", "134"))
        notebookService.addNote(NoteModel("Roman", "Ivanov", "M. Semenovskay Street", "1"))
        notebookService.addNote(NoteModel("Alexander", "Zemskov", "Bosova Street", "218"))
        notebookService.addNote(NoteModel("Kodi", "Benks", "Chapaeva Street", "4"))
    }

    private fun getUrl(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    private fun logging(): String? {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap()
        request.set("username", "lostlang")
        request.set("password", "admin")

        val response = restTemplate.postForEntity(getUrl("login"), HttpEntity(request, HttpHeaders()), String::class.java)

        return response!!.headers["Set-Cookie"]!![0]
    }

    @Test
    fun correctAddNoteToNotebook() {
        val noteModel = NoteModel("Alexander", "Zemskov", "Chapaeva Street", "121")

        val response = restTemplate.exchange(
            getUrl("api/add"),
            HttpMethod.POST,
            HttpEntity(noteModel, headers),
            String::class.java
        )

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun resultInListNotebookAll() {
        val response = restTemplate.exchange(
            getUrl("api/list"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun resultInListNotebookWithQuery() {
        val response = restTemplate.exchange(
            getUrl("api/list?id=1,2,5"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun editNote0() {
        val noteModel = NoteModel("Alexander", "Zemskov", "Chapaeva Street", "121")

        val response = restTemplate.exchange(
            getUrl("api/0/edit"),
            HttpMethod.PUT,
            HttpEntity(noteModel, headers),
            String::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun delNote0() {
        val response = restTemplate.exchange(
            getUrl("api/0/delete"),
            HttpMethod.DELETE,
            HttpEntity(null, headers),
            String::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }


    @ParameterizedTest
    @MethodSource("different id pages")
    fun idPages(id: Int) {
        val response = restTemplate.exchange(
            getUrl("api/$id"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    companion object {
        @JvmStatic
        fun `different id pages`() = listOf(
            2, 5, 1
        )
    }
}