package com.andiag.core

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {

    protected var mCompositeDisposable = CompositeDisposable()

    fun unsubscribe() {
        mCompositeDisposable.clear()
    }

}
