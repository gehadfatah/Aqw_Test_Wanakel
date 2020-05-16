package com.example.weanaklie.model

import com.example.weanaklie.model.datamodel.SuggestResponse
import io.reactivex.Single

val wakilneRepositoryRepositoryDep by lazy {
    WakilneRepositoryImp()
}

interface WakilneRepository {


    fun getSuggestDetail(uid: String): Single<SuggestResponse>
}