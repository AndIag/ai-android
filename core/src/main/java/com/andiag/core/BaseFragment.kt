package com.andiag.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class BaseFragment<P : BasePresenter> : Fragment() {

    private var mUnbinder: Unbinder? = null
    protected var mPresenter: P? = null

    @get:LayoutRes
    protected abstract val layout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layout, container, false)
        mUnbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder?.unbind()
        mPresenter?.unsubscribe()
        mUnbinder = null
        mPresenter = null
    }
}
