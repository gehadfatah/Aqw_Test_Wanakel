package com.android.friendycar.domain.common

import io.reactivex.Scheduler


interface ExecutionThread {
    val scheduler: Scheduler
}