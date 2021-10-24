package ru.sber.notebook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
class NotebookApplication

fun main(args: Array<String>) {
	runApplication<NotebookApplication>(*args)
}
