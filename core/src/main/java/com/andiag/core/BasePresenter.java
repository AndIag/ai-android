package com.andiag.core;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter {

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

}
