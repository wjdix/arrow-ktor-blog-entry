package com.siili.sample.repository

import arrow.Kind
import arrow.effects.rx2.ForSingleK
import arrow.effects.rx2.SingleK
import arrow.effects.rx2.k
import arrow.effects.typeclasses.Async
import com.siili.sample.domain.Employee
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore

interface EmployeeRepository<F> {
    fun findAll(): Kind<F, List<Employee>>
}

class AsyncEmployeeRepository<F>(
        private val store: KotlinEntityDataStore<Persistable>,
        private val async: Async<F>): EmployeeRepository<F> {

    override fun findAll(): Kind<F, List<Employee>> = async.delay {
        store.invoke {
            select(Employee::class)
                .get()
                .toList()
        }
    }

}

class SingleKEmployeeRepository(
        private val reactiveStore: KotlinReactiveEntityStore<Persistable>): EmployeeRepository<ForSingleK> {

    override fun findAll(): SingleK<List<Employee>> = reactiveStore
            .select(Employee::class)
            .get()
            .flowable()
            .toList()
            .k()

}
