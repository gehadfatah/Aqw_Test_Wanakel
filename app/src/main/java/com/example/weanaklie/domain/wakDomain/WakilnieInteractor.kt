package com.example.weanaklie.domain.wakDomain

import com.example.weanaklie.domain.common.mapNetworkErrors
import com.example.weanaklie.model.WakilneRepository
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.example.weanaklie.model.wakilneRepositoryRepositoryDep
import io.reactivex.Single


class WakilnieInteractor(private val friendyRepository: WakilneRepository = wakilneRepositoryRepositoryDep) :
    WakilnieUseCases {

    override fun getSuggestDetail(uid: String): Single<SuggestResponse> {
        return friendyRepository.getSuggestDetail(uid)
            .mapNetworkErrors()
    }

}