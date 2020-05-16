package com.example.weanaklie.domain.common


import com.android.friendycar.domain.common.InternalServerErrorException
import com.android.friendycar.domain.common.UnAuthorizedException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun <T> Single<T>.retryIfOffline(): Single<T> {
    return retry { err ->
        val retry: Boolean = (err is UnknownHostException)
        if (retry) Thread.sleep(3000)
        retry
    }
}

fun <T> Flowable<T>.mapNetworkErrors(): Flowable<T> {
    return singleOrError().onErrorResumeNext { error ->

        when (error) {
            is SocketTimeoutException -> Single.error(TimeoutException())
            is HttpException -> {
                val single: Single<T> = when {
                    error.code() == 401 -> {

                            Single.error(UnAuthorizedException(error.message()))
                    }
                    else -> {
                       Single.error(InternalServerErrorException())

                    }
                }

                single
            }
            else -> Single.error(error)
        }
    }.toFlowable()
}

fun <T> Single<T>.mapNetworkErrors(): Single<T> {
    return onErrorResumeNext { error ->

        when (error) {
            is SocketTimeoutException -> Single.error(TimeoutException())
            is HttpException -> {
                val single: Single<T> = when {
                    error.code() == 401 -> {

                            Single.error(UnAuthorizedException(error.message()))
                    }

                    else -> {
                     Single.error(InternalServerErrorException())

                    }
                }

                single
            }
            else -> Single.error(error)
        }
    }
}



fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun Completable.applyIoScheduler() = applyScheduler(Schedulers.io())

fun Completable.applyComputationScheduler() = applyScheduler(Schedulers.computation())

private fun Completable.applyScheduler(scheduler: Scheduler) =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applyIoScheduler() = applyScheduler(Schedulers.io())

fun <T> Single<T>.applyComputationScheduler() = applyScheduler(Schedulers.computation())

private fun <T> Single<T>.applyScheduler(scheduler: Scheduler) =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

