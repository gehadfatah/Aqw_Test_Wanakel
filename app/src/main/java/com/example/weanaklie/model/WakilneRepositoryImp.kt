package com.example.weanaklie.model

import com.example.weanaklie.domain.common.JobExecutor
import com.example.weanaklie.model.remote.WakelnieApi
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.example.weanaklie.model.remote.WAKILNE_API_DEP
import io.reactivex.Single


class WakilneRepositoryImp(
    private val wakelnieApi: WakelnieApi = WAKILNE_API_DEP,
    private val executor: JobExecutor = JobExecutor()
) :
    WakilneRepository {

    override fun getSuggestDetail(
        uid: String    ): Single<SuggestResponse> = wakelnieApi.getSuggestDetail(/*uid*/)


}