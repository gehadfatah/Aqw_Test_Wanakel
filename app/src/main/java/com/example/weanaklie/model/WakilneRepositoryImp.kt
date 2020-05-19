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
        //my location aswan no result found for api so you can use this 26.2716025,50.2017993

        uid: String
    ): Single<SuggestResponse> = wakelnieApi.getSuggestDetail(uid/*uid*/)


}