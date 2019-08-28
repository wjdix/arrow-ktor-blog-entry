package com.siili.sample.core

import arrow.Kind
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.fix
import arrow.effects.rx2.ForSingleK
import arrow.effects.rx2.SingleK
import arrow.effects.rx2.fix
import arrow.effects.rx2.value
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface Suspendable<F> {
    suspend fun <A: Any> Kind<F, A>.suspended(): A
}

fun IO.Companion.suspendable(): Suspendable<ForIO> = object: Suspendable<ForIO> {
    override suspend fun <A : Any> Kind<ForIO, A>.suspended(): A {
        val io = this.fix()
        return suspendCoroutine { cont ->
            io.unsafeRunAsync { result -> result.fold(cont::resumeWithException, cont::resume) }
        }
    }
}

fun SingleK.Companion.suspendable(): Suspendable<ForSingleK> = object: Suspendable<ForSingleK> {
    override suspend fun <A: Any> Kind<ForSingleK, A>.suspended(): A {
        val single = this.fix().value()
        return suspendCoroutine {
            single.subscribe(it::resume, it::resumeWithException)
        }
    }
}
