package net.rafaeltoledo.code.ui.screen.topusers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import net.rafaeltoledo.code.api.StackOverflowApi
import net.rafaeltoledo.code.api.User
import net.rafaeltoledo.code.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class TopUsersViewModel @Inject constructor(private val api: StackOverflowApi) : BaseViewModel() {

    private val _result = MutableLiveData<List<User>>()
    val result: LiveData<List<User>>
        get() = _result

    fun fetchUsers() {
        launchDataLoad { _result.postValue(api.fetchUsers(1).items) }
    }
}