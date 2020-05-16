package com.example.weanaklie.domain.wakDomain

import com.example.weanaklie.model.datamodel.SuggestResponse
import io.reactivex.Single


val wakelineUseCasesDep by lazy {
    WakilnieInteractor()
}

interface WakilnieUseCases {


    fun getSuggestDetail(uid:String): Single<SuggestResponse>
}

