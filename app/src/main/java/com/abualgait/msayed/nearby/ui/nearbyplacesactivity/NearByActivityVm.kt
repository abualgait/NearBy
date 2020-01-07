package com.abualgait.msayed.nearby.ui.nearbyplacesactivity

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abualgait.msayed.nearby.shared.data.DataManager
import com.abualgait.msayed.nearby.shared.data.model.NearByPlacesResponse
import com.abualgait.msayed.nearby.shared.data.model.Venue
import com.abualgait.msayed.nearby.shared.vm.BaseViewModel
import com.abualgait.msayed.thiqah.shared.util.ext.with
import com.abualgait.msayed.thiqah.shared.util.ext.withDB
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observables.ConnectableObservable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val nearbyActivityModule = module {
    viewModel { NearByActivityVm(get()) }
}

class NearByActivityVm(dataManager: DataManager) : BaseViewModel(dataManager) {


    fun isValidPoint(point: String): Boolean {
        return point != "0.0"
    }

    fun isValidLatLng(latLng: String): Boolean {
        return latLng.isNotEmpty()
    }

    var payload: MutableLiveData<ResultUIModel> = MutableLiveData()
    private val disposeDisposable = MutableLiveData<CompositeDisposable>()


    fun getDisposable(): LiveData<CompositeDisposable> {
        return disposeDisposable
    }


    private fun getVenuesFromLocale(): Observable<List<Venue>> {
        return Observable.defer { Observable.just(database.getVenueDao()!!.getItems()) }
            .withDB(scheduler)
    }

    private fun getVenuesFromApiCall(placesObservable: ConnectableObservable<NearByPlacesResponse>): Observable<List<Venue>> {
        return placesObservable.map {
            Observable.just(database)
                .withDB(scheduler)
                .subscribe { db ->
                    db.getVenueDao()?.deleteArticles()
                    db.getVenueDao()?.insertALLItems(it.response.venues as MutableList<Venue>)
                }

            it
        }.subscribeOn(Schedulers.io())
            .map { it.response.venues }
    }

    @SuppressLint("CheckResult")
    fun getNearByPlaces(latLng: String) {
        payload.value = ResultUIModel(isLoading = true)
        val placesObservable = api.getNearByPlaces(latLng).replay()
        val disposable = CompositeDisposable()
        disposable.add(
            Observable.concat(
                getVenuesFromApiCall(placesObservable), getVenuesFromLocale()
            ).with(scheduler)

                .subscribeWith(object : DisposableObserver<List<Venue>>() {
                    override fun onComplete() {

                    }

                    override fun onNext(result: List<Venue>) {
                        payload.value = ResultUIModel(list = result)
                        Observable.just(database)
                            .subscribeOn(Schedulers.io())
                            .subscribe { db ->
                                db.getVenueDao()?.deleteArticles()
                                db.getVenueDao()?.insertALLItems(result as MutableList<Venue>)
                            }
                    }

                    override fun onError(error: Throwable) {
                        payload.value = ResultUIModel(error = error)
                    }
                })

        )

        disposable.add(
            placesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    return@flatMap Observable.fromIterable(it.response.venues)
                }
                .flatMap {
                    return@flatMap getPhotoObservable(it)
                }
                .subscribeWith(object : DisposableObserver<Venue>() {

                    override fun onNext(venue: Venue) {
                        payload.value = ResultUIModel(venue = venue)

                    }

                    override fun onError(error: Throwable) {
                        payload.value = ResultUIModel(error = error)

                    }

                    override fun onComplete() {

                    }
                })
        )
        placesObservable.connect()
        disposeDisposable.value = disposable
    }

    private fun getPhotoObservable(venue: Venue): Observable<Venue> {
        return api.getPhotos(venue.id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).map { photoResponse ->
                if (!photoResponse.response.photos.items.isNullOrEmpty()) {
                    venue.image =
                        photoResponse.response.photos.items[0].prefix + photoResponse.response.photos.items[0].width + "x" +
                                photoResponse.response.photos.items[0].height + photoResponse.response.photos.items[0].suffix
                }
                return@map venue
            }
    }


    data class ResultUIModel(
        val venue: Venue? = null,
        val list: List<Venue> = emptyList(),
        val error: Throwable? = null,
        val isLoading: Boolean = false
    )
}