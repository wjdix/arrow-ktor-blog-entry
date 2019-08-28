package com.siili.sample.web

import arrow.core.ForId
import arrow.core.Id
import arrow.core.IdOf
import com.siili.sample.core.suspendable
import com.siili.sample.domain.Employee
import com.siili.sample.repository.EmployeeRepository
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import java.nio.charset.StandardCharsets.UTF_8

object EmployeeRoutingSpec: Spek({

    class MockEmployeeRepository(private val list: List<Employee>): EmployeeRepository<ForId> {
        override fun findAll(): Id<List<Employee>> = Id.just(list)
    }

    val employees = listOf(Employee(1, "John", "Smith"))
    val employeeRoutingForTests = EmployeeRouting(MockEmployeeRepository(employees), Id.suspendable())

    fun Application.testApp() {
        install(ContentNegotiation) {
            jackson {}
        }
        install(Routing) {
            with(employeeRoutingForTests) { employees("/test/employees") }
        }
    }

    test("get returns list of employees") {
        withTestApplication(Application::testApp) {
            with(handleRequest(HttpMethod.Get, "/test/employees")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(UTF_8), response.contentType())
                assertEquals("""[{"id":1,"firstName":"John","lastName":"Smith"}]""", response.content)
            }
        }
    }

})
