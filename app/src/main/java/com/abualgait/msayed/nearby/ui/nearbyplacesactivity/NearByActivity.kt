package com.abualgait.msayed.nearby.ui.nearbyplacesactivity

import Utils
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.abualgait.msayed.nearby.R
import com.abualgait.msayed.nearby.shared.adapters.CommonAdapter
import com.abualgait.msayed.nearby.shared.data.model.Venue
import com.abualgait.msayed.nearby.shared.enums.OperationalMode
import com.abualgait.msayed.nearby.shared.interfaces.ILocationCallbacks
import com.abualgait.msayed.nearby.shared.interfaces.SimpleItemClickListener
import com.abualgait.msayed.nearby.shared.ui.activity.BaseActivity
import com.abualgait.msayed.nearby.shared.util.FlashbarUtil
import com.abualgait.msayed.nearby.shared.util.LocationProvider
import com.abualgait.msayed.nearby.shared.util.ThreadUtil
import com.abualgait.msayed.nearby.shared.util.configs.ConstValue
import com.google.android.gms.common.api.ResolvableApiException
import kotlinx.android.synthetic.main.activity_nearby.*
import kotlinx.android.synthetic.main.activity_nearby_main_view.*
import org.koin.android.viewmodel.ext.android.viewModel


class NearByActivity : BaseActivity<NearByActivityVm>(), SimpleItemClickListener, ILocationCallbacks {
    override fun getCurrentLocation(currentLocation: Location) {
        vm.pref.latitude = currentLocation.latitude.toString()
        vm.pref.longitude = currentLocation.longitude.toString()
        checkValidation()
    }

    override fun checkLocationSettings(rae: ResolvableApiException) {
        this.rae = rae
        startGpsResolution()
    }


    override fun onItemClick(`object`: Any) {
        val info = `object` as Venue
        FlashbarUtil.show(info.name, activity = activity())
    }


    override fun onRetryClicked() {
        super.onRetryClicked()
        checkValidation()
    }

    override val vm: NearByActivityVm by viewModel()
    override var layoutId: Int = R.layout.activity_nearby
    private var locationProvider: LocationProvider? = null
    private var rae: ResolvableApiException? = null
    private var dialog: AlertDialog? = null


    override fun doOnCreate() {
        super.doOnCreate()
        invalidateOptionsMenu()

        hasSwipeRefresh = true
        locationProvider = LocationProvider(activity(), 0, this)
        locationProvider!!.startLocationUpdates()

        if (!checkIfAlreadyhavePermission()) {
            requestForSpecificPermission()
        } else {
             val myLocationListener = MyLocationListener()
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocationListener)

        }
        checkValidation()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.operational_mode, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(R.id.mode)
        if (vm.pref.mode == OperationalMode.REALTIME.name) {
            item?.title = getString(R.string.realtime)
        } else {
            item?.title = getString(R.string.single_update)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mode -> {
                if (vm.pref.mode == OperationalMode.REALTIME.name) {
                    vm.pref.mode = OperationalMode.SINGLEUPDATE.name
                    item.title = getString(R.string.single_update)
                } else {
                    vm.pref.mode = OperationalMode.REALTIME.name
                    item.title = getString(R.string.realtime)
                }
            }
        }
        return true
    }

    private fun checkIfAlreadyhavePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            ).toString()
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), ConstValue.MY_PERMISSIONS_CODE
        )
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == ConstValue.MY_PERMISSIONS_CODE) {
            for (i in 0 until grantResults.size)
                if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    locationProvider!!.startLocationUpdates()
                    val myLocationListener = MyLocationListener()
                    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3f, myLocationListener)

                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(permissions[i] + " Permission Required")
                        .setPositiveButton("OK") { _, _ ->
                            openPermissionScreen()
                        }
                        .setNegativeButton("CANCEL") { dialog, _ ->

                            dialog.dismiss()
                        }
                    dialog = builder.show()
                }
            return
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            ConstValue.LOCATION_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.i("PendingIntent", "User agreed to make required location settings changes.")
                    locationProvider!!.startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    //if user cancels gps enabling
                    //show the enabling popup again
                    Log.i("PendingIntent", "User chose not to make required location settings changes.")
                    startGpsResolution()
                }
            }

        }


    }

    private fun openPermissionScreen() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(currentLocation: Location?) {
            if (vm.pref.mode == OperationalMode.REALTIME.name)
                checkValidation()
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        }

        override fun onProviderEnabled(p0: String?) {
        }

        override fun onProviderDisabled(p0: String?) {

        }

    }

    private fun startGpsResolution() {
        //Show the gps enable dialog by calling startResolutionForResult(), and check the
        //result in onActivityResult().
        try {
            rae!!.startResolutionForResult(activity(), ConstValue.LOCATION_REQUEST_CODE)
        } catch (sie: IntentSender.SendIntentException) {
            Log.i("PendingIntent", "PendingIntent unable to execute request.")
        }
    }


    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        checkValidation()

    }


    @SuppressLint("CheckResult")
    private fun checkValidation() {
        setAdapter()
        if (Utils.isOnline(activity())) {
            getPlacesFromApi()
        } else {
            loadPlaceFromDB()
        }
    }

    private fun loadPlaceFromDB() {
        vm.getNearByPlacesLiveData()
            .observe(
                this,
                Observer { response ->
                    showMainLayout(view)
                    checkResponse(response)

                })

        vm.getError()
            .observe(
                this,
                Observer { error ->
                    Log.e("Error", error)
                    showErrorData()

                })
        vm.getVenuesFromDatabase()
    }

    private fun getPlacesFromApi() {
        showLoader()
        vm.getNearByPlacesLiveData()
            .observe(
                this,
                Observer { response ->
                    showMainLayout(view)
                    checkResponse(response)

                })

        vm.getError()
            .observe(
                this,
                Observer { error ->
                    Log.e("Error", error)
                    showErrorData()

                })

        vm.getVenueLiveData()
            .observe(
                this,
                Observer { venue ->

                    val position = mList!!.indexOf(venue)
                    if (position == -1) return@Observer
                    mList!![position] = venue
                    commonadapter!!.notifyItemChanged(position)
                })

        vm.getNearByPlaces(vm.pref.latitude + "," + vm.pref.longitude)


    }

    private fun checkResponse(response: List<Venue>?) {
        mList?.clear()
        if (response != null) {
            mList!!.addAll(response)
            commonadapter!!.notifyDataSetChanged()

        } else {
            showEmptyData()
        }

    }

    private fun setAdapter() {
        mList = ArrayList()
        val layoutManager = LinearLayoutManager(activity())
        recycler_nearby.layoutManager = layoutManager
        commonadapter = CommonAdapter(activity(), mList!!)
        recycler_nearby.adapter = commonadapter
        commonadapter!!.setItemClick(this)
    }

    override fun showLoading() {
        super.showLoading()
        ThreadUtil.runOnUiThread { mSwipeRefresh.isRefreshing = true }
    }

    override fun hideLoading() {
        ThreadUtil.runOnUiThread { mSwipeRefresh.isRefreshing = false }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", "checkValidation")
        vm.getDisposable().value?.dispose()
    }


}
