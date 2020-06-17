package net.rafaeltoledo.code.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    protected fun launchDataLoad(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            try {
                _loading.postValue(true)
                block()
            } catch (error: Exception) {
                errorHandler(error)
            } finally {
                _loading.value = false
            }
        }
    }

    val errorHandler = { e: Throwable ->
        Log.e("ViewModel", "Failed to perform action", e)
        _loading.postValue(false)
        _error.postValue(e)
    }
}