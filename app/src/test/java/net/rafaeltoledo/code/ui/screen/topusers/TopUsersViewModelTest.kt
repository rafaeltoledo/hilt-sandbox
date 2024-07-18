package net.rafaeltoledo.code.ui.screen.topusers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import net.rafaeltoledo.code.api.Collection
import net.rafaeltoledo.code.api.StackOverflowApi
import net.rafaeltoledo.code.api.User
import net.rafaeltoledo.code.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class TopUsersViewModelTest {

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @get:Rule
    val server = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun onApiSuccess_publishesTheDataOnLiveData() = runTest {
        val api: StackOverflowApi = mock()
        val viewModel = TopUsersViewModel(api)

        whenever(api.fetchUsers(any())) doReturn Collection(listOf(
            User("", "", "", ""),
            User("", "", "", "")
        ), false)

        viewModel.fetchUsers()

        assertThat(viewModel.result.getOrAwaitValue()).hasSize(2)
    }
}