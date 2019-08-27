package com.siili.sample.web

import com.siili.sample.core.Suspendable
import com.siili.sample.repository.EmployeeRepository
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

class EmployeeRouting<F>(
        private val repository: EmployeeRepository<F>,
        suspendable: Suspendable<F>): Suspendable<F> by suspendable {

    fun Routing.employees(path: String) {
        get(path) {
            call.respond(repository
                    .findAll()
                    .suspended()
            )
        }
    }

}
