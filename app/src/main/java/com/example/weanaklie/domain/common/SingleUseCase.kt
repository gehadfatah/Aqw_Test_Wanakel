package com.android.friendycar.domain.common

import io.reactivex.Single


abstract class SingleUseCase<in Params,out Type> where Type : Any {
    abstract fun build(params: Params): Single<out Type>
}