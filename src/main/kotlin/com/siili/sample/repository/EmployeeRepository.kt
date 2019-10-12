package com.siili.sample.repository

import arrow.Kind
import arrow.fx.rx2.ForSingleK
import arrow.fx.rx2.SingleK
import arrow.fx.rx2.k
import arrow.fx.typeclasses.MonadDefer
import com.siili.sample.domain.Employee
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore

interface EmployeeRepository<F> {
    fun findAll(): Kind<F, List<Employee>>
}

class DeferEmployeeRepository<F>(
        private val store: KotlinEntityDataStore<Persistable>,
        private val monadDefer: MonadDefer<F>): EmployeeRepository<F> {

    override fun findAll(): Kind<F, List<Employee>> = monadDefer.later {
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
