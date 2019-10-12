package com.siili.sample

import arrow.fx.ForIO
import arrow.fx.rx2.SingleK
import arrow.fx.rx2.ForSingleK
import arrow.fx.IO
import arrow.fx.extensions.io.monadDefer.monadDefer
import com.fasterxml.jackson.databind.SerializationFeature
import com.siili.sample.core.suspendable
import com.siili.sample.database.RequeryDatabase
import com.siili.sample.repository.DeferEmployeeRepository
import com.siili.sample.repository.EmployeeRepository
import com.siili.sample.repository.SingleKEmployeeRepository
import com.siili.sample.web.EmployeeRouting
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {

    val ioRepository: EmployeeRepository<ForIO> = DeferEmployeeRepository(RequeryDatabase.store, IO.monadDefer())
    val rx2Repository: EmployeeRepository<ForSingleK> = SingleKEmployeeRepository(RequeryDatabase.reactiveStore)

    val ioRouting = EmployeeRouting(ioRepository, IO.suspendable())
    val rx2Routing = EmployeeRouting(rx2Repository, SingleK.suspendable())

    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        install(Routing) {
            with(ioRouting) { employees("/io/employees") }
            with(rx2Routing) { employees("/rx2/employees") }
        }
    }
    server.start(wait = true)

}
