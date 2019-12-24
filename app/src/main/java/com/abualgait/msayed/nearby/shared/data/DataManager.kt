package com.abualgait.msayed.nearby.shared.data


import com.abualgait.msayed.nearby.shared.databases.DBRepository
import com.abualgait.msayed.nearby.shared.network.ApiRepository
import com.abualgait.msayed.nearby.shared.rx.SchedulerProvider
import com.abualgait.msayed.nearby.shared.util.SharedPref

open class DataManager(
        val pref: SharedPref,
        val api: ApiRepository,
        val database: DBRepository,
        val schedulerProvider: SchedulerProvider
)
