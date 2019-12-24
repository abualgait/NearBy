package com.abualgait.msayed.nearby.shared.koin

import android.content.Context
import com.abualgait.msayed.nearby.ui.nearbyplacesactivity.nearbyActivityModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinHelper {
    companion object {
        fun start(context: Context) {
            startKoin {
                androidContext(context)
                modules(
                        listOf(
                                appModule,
                                nearbyActivityModule


                        )
                )
            }
        }
    }
}