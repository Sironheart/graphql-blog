package dev.beisenherz.plugins

import com.apurebase.kgraphql.GraphQL
import io.ktor.server.application.Application
import io.ktor.server.application.install
import java.time.LocalDate
import java.util.UUID

fun Application.configureGraphQL() {
    val posts = mutableListOf(
        Post(UUID.randomUUID(), "bla", "foo", LocalDate.now(), null, LocalDate.now(), LocalDate.now()),
        Post(UUID.randomUUID(), "bar", "foo", LocalDate.now(), null, LocalDate.now(), LocalDate.now()),
        Post(UUID.randomUUID(), "miep", "foo", null, null, LocalDate.now(), LocalDate.now()),
        Post(UUID.randomUUID(), "miau", "foo", LocalDate.now(), null, LocalDate.now(), LocalDate.now()),
        Post(UUID.randomUUID(), "what", "foo", LocalDate.now(), null, LocalDate.now(), LocalDate.now()),
    )
    install(GraphQL) {
        playground = true
        schema {
            query("posts") {
                resolver { ->
                    posts
                }
            }
            query("post") {
                resolver { id: UUID -> Post(id, "bla", "foo", LocalDate.now(), null, LocalDate.now(), LocalDate.now()) }
            }
            mutation("createPost") {
                resolver { input: PostInput ->
                    posts.add(
                        Post(
                            UUID.randomUUID(),
                            input.title,
                            input.content,
                            input.releaseDate,
                            input.image,
                            LocalDate.now(),
                            null,
                        ),
                    )
                }
            }
            mutation("updatePost") {
                resolver { id: UUID, input: PostInput ->
                    val element = posts.find { it.id == id }?.copy(
                        title = input.title,
                        content = input.content,
                        releaseDate = input.releaseDate,
                        image = input.image,
                        updatedAt = LocalDate.now(),
                    ) ?: TODO("error handling")
                    posts.add(element)
                    posts.removeAt(posts.indexOfFirst { it.id == id })
                }
            }
            mutation("deletePost") {
                resolver { id: UUID ->
                    posts.removeAll { it.id == id }
                }
            }
            stringScalar<UUID> {
                deserialize = { uuid: String -> UUID.fromString(uuid) }
                serialize = UUID::toString
            }
            stringScalar<LocalDate> {
                deserialize = { localdate: String -> LocalDate.parse(localdate) }
                serialize = LocalDate::toString
            }
        }
    }
}

data class Post(
    val id: UUID,
    var title: String,
    var content: String,
    var releaseDate: LocalDate?,
    var image: String?,
    val createdAt: LocalDate,
    var updatedAt: LocalDate?,
)

data class PostInput(
    val title: String,
    val content: String,
    val releaseDate: LocalDate?,
    val image: String?,
)
