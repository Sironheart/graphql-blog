package dev.beisenherz.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val hikari = HikariDataSource(
        HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("db.url")
            username = System.getenv("db.user")
            password = System.getenv("db.password")
            maximumPoolSize = 3
            isAutoCommit = false
        },
    )
    val database = Database.connect(hikari)
}
