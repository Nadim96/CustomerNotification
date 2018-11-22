package com

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import javafx.geometry.Pos
import java.awt.SystemColor.text
import java.util.*

data class PostSnippet(val snippet: PostSnippet.Text) {
    data class Text(val text: String)
}

data class Snippet(val text: String)

val snippets = Collections.synchronizedList(mutableListOf(
    Snippet("hello"),
    Snippet("world")
))

fun Application.main() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    routing {
        get("/snippets") {
            call.respond(mapOf("snippets" to synchronized(snippets) { snippets.toString() }))
        }
        post("/snippets") {
            val post = call.receive<PostSnippet>()
            snippets += Snippet(post.snippet.text)
            call.respond(mapOf("OK" to true))
        }
    }
}