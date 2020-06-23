package net.rafaeltoledo.code.ui.screen.topusers

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import net.rafaeltoledo.code.R
import net.rafaeltoledo.code.di.NetworkModule
import net.rafaeltoledo.code.launchFragmentInHiltContainer
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(NetworkModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class TopUsersFragmentTest {

    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val server = MockWebServer()

    @Inject
    lateinit var client: OkHttpClient

    private lateinit var resource: IdlingResource

    @Before
    fun setupMockWebServer() {
        hiltRule.inject()

        TestModule.url = server.url("/")
        resource = OkHttp3IdlingResource.create("OkHttp", client)
        IdlingRegistry.getInstance().register(resource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(resource)
    }

    @Test
    fun onSuccessResult_ShouldShowListOnScreen() {
        // Given
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(readFile("response.json"))
        )

        // When
        launchFragmentInHiltContainer<TopUsersFragment>()

        // Then
        onView(withId(R.id.button_try_again)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

        server.takeRequest().apply {
            assertThat(path).isEqualTo("/2.2/users?order=desc&sort=reputation&site=stackoverflow&page=1")
            assertThat(this.method).isEqualTo("GET")
        }
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupTrustStore() {
            System.setProperty("javax.net.ssl.trustStore", "NONE")
        }
    }
}

@InstallIn(ApplicationComponent::class)
@Module
object TestModule {
    var url: HttpUrl = "http://localhost".toHttpUrlOrNull()!!

    @Provides
    fun mockWebServerHost() = url
}

fun Any.readFile(filename: String) = this::class.java.classLoader?.getResource(filename)?.readText()
    ?: InstrumentationRegistry.getInstrumentation().context.assets.open(filename)
        .reader().readText()