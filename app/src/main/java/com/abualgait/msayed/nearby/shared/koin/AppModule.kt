package com.abualgait.msayed.nearby.shared.koin


import androidx.preference.PreferenceManager
import androidx.room.Room
import com.abualgait.msayed.nearby.BuildConfig
import com.abualgait.msayed.nearby.shared.data.DataManager
import com.abualgait.msayed.nearby.shared.databases.AppDatabase
import com.abualgait.msayed.nearby.shared.databases.DBRepository
import com.abualgait.msayed.nearby.shared.network.ApiInterface
import com.abualgait.msayed.nearby.shared.network.ApiRepository
import com.abualgait.msayed.nearby.shared.rx.SchedulerProvider
import com.abualgait.msayed.nearby.shared.rx.SchedulerProviderImpl
import com.abualgait.msayed.nearby.shared.util.SharedPref
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {

    // ApiInterface
    single {

        val gson = GsonBuilder()
                .setLenient()
                .create()

        Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(get())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(ApiInterface::class.java)
    }


    // OkHttpClient
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val builder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        builder.build()
    }

    single { DataManager(get(), get(), get(), get()) }

    single { ApiRepository(get()) }

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "venue-db")
                .build()
    }

    single { DBRepository(get()) }

    single { SharedPref(get()) }

    // default pref
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    single<SchedulerProvider> { SchedulerProviderImpl() }


}