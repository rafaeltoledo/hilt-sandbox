package net.rafaeltoledo.code.ui.screen.topusers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.rafaeltoledo.code.api.StackOverflowApi
import net.rafaeltoledo.code.api.User
import net.rafaeltoledo.code.ui.BaseViewModel

class TopUsersViewModel @ViewModelInject constructor(private val api: StackOverflowApi) :
    BaseViewModel() {

    private val _result = MutableLiveData<List<User>>()
    val result: LiveData<List<User>>
        get() = _result

    fun fetchUsers() {
        launchDataLoad { _result.postValue(api.fetchUsers(1).items) }
    }
}