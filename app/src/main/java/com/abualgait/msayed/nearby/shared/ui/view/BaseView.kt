package com.abualgait.msayed.nearby.shared.ui.view


import android.content.Context
import com.abualgait.msayed.nearby.shared.ui.activity.BaseActivity
import com.abualgait.msayed.nearby.shared.vm.BaseViewModel

interface BaseView {


    fun baseViewModel(): BaseViewModel?

    fun activity(): BaseActivity<*>?

    fun context(): Context? {
        return activity()
    }


    fun showLoading() {}

    fun hideLoading() {}


}
