package com.siili.sample.core

import arrow.Kind
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.fix
import arrow.fx.rx2.ForSingleK
import arrow.fx.rx2.SingleK
import arrow.fx.rx2.fix
import arrow.fx.rx2.value
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface Suspendable<F> {
    suspend fun <A: Any> Kind<F, A>.suspended(): A
}

fun IO.Companion.suspendable(): Suspendable<ForIO> = object: Suspendable<ForIO> {
    override suspend fun <A : Any> Kind<ForIO, A>.suspended(): A {
        val io = this.fix()
        return io.suspended()
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
