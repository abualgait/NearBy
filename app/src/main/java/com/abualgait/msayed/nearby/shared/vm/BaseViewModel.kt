package com.abualgait.msayed.nearby.shared.vm


import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.abualgait.msayed.nearby.shared.data.DataManager
import com.abualgait.msayed.nearby.shared.databases.DBRepository
import com.abualgait.msayed.nearby.shared.network.ApiRepository
import com.abualgait.msayed.nearby.shared.ui.view.BaseView
import com.abualgait.msayed.nearby.shared.util.SharedPref
import com.abualgait.msayed.nearby.shared.util.io.app.MyApp
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(dm: DataManager)
    : ViewModel() {

    lateinit var view: BaseView
    var pref: SharedPref = dm.pref
    var api: ApiRepository = dm.api
    var database: DBRepository = dm.database

    val disposables: CompositeDisposable = CompositeDisposable()

    protected fun activity(): FragmentActivity? {
        return view.activity()
    }


    protected fun getString(@StringRes res: Int): String {
        return MyApp.context.getString(res)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    fun loading(isLoading: Boolean) {
        if (isLoading) {
            view.showLoading()
            return
        }
        view.hideLoading()
    }

}
