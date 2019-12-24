package com.abualgait.msayed.nearby.ui.nearbyplacesactivity

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abualgait.msayed.nearby.shared.data.DataManager
import com.abualgait.msayed.nearby.shared.data.model.Venue
import com.abualgait.msayed.nearby.shared.vm.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val nearbyActivityModule = module {
    viewModel { NearByActivityVm(get()) }
}

class NearByActivityVm(dataManager: DataManager) : BaseViewModel(dataManager) {
    var payload: MutableLiveData<List<Venue>> = MutableLiveData()
    var venuePOJO: MutableLiveData<Venue> = MutableLiveData()
    private val showError = MutableLiveData<String>()
    private val disposeDisposable = MutableLiveData<CompositeDisposable>()
    fun getNearByPlacesLiveData(): LiveData<List<Venue>> {
        return payload
    }

    fun getVenueLiveData(): LiveData<Venue> {
        return venuePOJO
    }

    fun getError(): LiveData<String> {
        return showError
    }

    fun getDisposable(): LiveData<CompositeDisposable> {
        return disposeDisposable
    }


    @SuppressLint("CheckResult")
    fun getVenuesFromDatabase() {
        Observable.defer { Observable.just(database.getVenueDao()!!.getItems()) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                view.hideLoading()
                payload.value = result
            }, { error ->
                view.hideLoading()
                showError.value = error.message
            })

    }

    fun getVenuesFromLocale(): Observable<List<Venue>> {
        return Observable.defer { Observable.just(database.getVenueDao()!!.getItems()) }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun getNearByPlaces(latLng: String) {

        view.showLoading()
        val placesObservable = api.getNearByPlaces(latLng).replay()

        val disposable = CompositeDisposable()

        disposable.add(
            Observable.concat(
                placesObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { it.response.venues }, getVenuesFromLocale()
            ).subscribeWith(object : DisposableObserver<List<Venue>>() {

                override fun onNext(result: List<Venue>) {
                    view.hideLoading()
                    payload.value = result
                    Observable.just(database)
                        .subscribeOn(Schedulers.io())
                        .subscribe { db ->
                            db.getVenueDao()?.deleteArticles()
                            db.getVenueDao()?.insertALLItems(result as MutableList<Venue>)
                        }

                }

                override fun onError(error: Throwable) {
                    view.hideLoading()
                    showError.value = error.message
                }

                override fun onComplete() {

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
                        venuePOJO.value = venue

                    }

                    override fun onError(error: Throwable) {
                        showError.value = error.message
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
}