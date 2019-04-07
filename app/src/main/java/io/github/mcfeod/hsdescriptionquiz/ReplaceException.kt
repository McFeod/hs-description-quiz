package io.github.mcfeod.hsdescriptionquiz

import java.lang.Exception

class ReplaceException(private val exceptionClass: Class<out Exception>, private val log: (String) -> Unit) {
    // I just tried to write python-style decorator. Damn static types :(
    fun <T, R> wrap(f: (arg: T) -> R): (T) -> R = {
        arg -> try {
            f(arg)
        } catch (e: Exception) {
            log(e.toString())
            throw exceptionClass.newInstance()
        }
    }

    fun <T1, T2, R> wrap(f: (arg1: T1, arg2: T2) -> R): (T1, T2) -> R = {
        arg1, arg2 -> try {
            f(arg1, arg2)
        } catch (e: Exception) {
            log(e.toString())
            throw exceptionClass.newInstance()
        }
    }
}