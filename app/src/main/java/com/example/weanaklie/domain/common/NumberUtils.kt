package com.android.friendycar.domain.common


fun Float.formatTo(numberOfDecimals: Int) =
        String.format("%.${numberOfDecimals}f", this)