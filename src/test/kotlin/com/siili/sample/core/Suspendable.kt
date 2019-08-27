package com.siili.sample.core

import arrow.Kind
import arrow.core.ForId
import arrow.core.Id
import arrow.core.fix
import kotlinx.coroutines.runBlocking

fun Id.Companion.suspendable(): Suspendable<ForId> = object: Suspendable<ForId> {
    override suspend fun <A : Any> Kind<ForId, A>.suspended(): A {
        val id = this.fix()
        return runBlocking { id.extract() }
    }
}
