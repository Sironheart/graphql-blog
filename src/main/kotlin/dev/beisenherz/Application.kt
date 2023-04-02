package dev.beisenherz

import dev.beisenherz.plugins.configureDatabases
import dev.beisenherz.plugins.configureGraphQL
import dev.beisenherz.plugins.configureHTTP
import dev.beisenherz.plugins.configureMonitoring
import dev.beisenherz.plugins.configureRouting
import dev.beisenherz.plugins.configureSecurity
import dev.beisenherz.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureGraphQL()
}
