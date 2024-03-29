package br.com.ladyplant.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T, A> : ViewModel() {
    abstract val viewState: T
    abstract fun dispatchViewAction(viewAction: A)
}
