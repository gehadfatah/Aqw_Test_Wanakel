package com.example.weanaklie.presentation.main.wakilnieHome

import android.util.Log
import androidx.lifecycle.MutableLiveData

import com.example.weanaklie.domain.common.addTo


import com.example.weanaklie.domain.wakDomain.WakilnieUseCases
import com.example.weanaklie.domain.wakDomain.wakelineUseCasesDep
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.example.weanaklie.presentation.common.BaseViewModel
import com.example.weanaklie.presentation.common.androidMainThreadScheduler
import com.example.weanaklie.presentation.common.schedulerIo
import io.reactivex.Scheduler

class SuggestDetailViewModel (
    private val wakilnieUseCases: WakilnieUseCases= wakelineUseCasesDep,
    private val subscribeOnScheduler: Scheduler = schedulerIo,
    private val observeOnScheduler: Scheduler = androidMainThreadScheduler
) : BaseViewModel() {
    val suggestResponse: MutableLiveData<SuggestResponse> = MutableLiveData()

    fun getRequestDetails(uid: String) {
        wakilnieUseCases.getSuggestDetail(uid)
            .doOnSubscribe {

                isLoading.postValue(true)

            }.doOnTerminate {

                isLoading.postValue(false)
            }
            .subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribe({
                it?.let {
                    suggestResponse.value = it
                    Log.d("success ", it.toString())

                }
            }, {

                Log.d("error ", it.toString())
                error.value = it
            }).addTo(disposable)
    }

}