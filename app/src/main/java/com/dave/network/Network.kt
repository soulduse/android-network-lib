package com.dave.network

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Usage
 * Network.request(
 *          call = call,
 *          success = { },
 *          fail = { })
 */

object Network {
    fun <T> request(
            call: Deferred<T>,
            success: ((response: T)-> Unit)?,
            fail: ((throwable: Throwable)-> Unit)?= null) {
        launch(UI) {
            try {
                success?.invoke(call.await())
            } catch (t: Throwable) {
                fail?.invoke(t)
            }
        }
    }
}
