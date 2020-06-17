package net.rafaeltoledo.code.ui.screen.topusers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import net.rafaeltoledo.code.api.Collection
import net.rafaeltoledo.code.api.StackOverflowApi
import net.rafaeltoledo.code.api.User
import net.rafaeltoledo.code.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    fun onApiSuccess_publishesTheDataOnLiveData() = runBlockingTest {
        val api: StackOverflowApi = mockk()
        val viewModel = TopUsersViewModel(api)

        coEvery { api.fetchUsers(any()) } returns Collection(listOf(
            User("", "", "", ""),
            User("", "", "", "")
        ), false)

        viewModel.fetchUsers()

        assertThat(viewModel.result.getOrAwaitValue()).hasSize(2)
    }
}