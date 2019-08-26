package safi.doordashlite.ui.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import safi.doordashlite.R
import safi.doordashlite.api.Restaurant
import safi.doordashlite.model.RestaurantRepository
import safi.doordashlite.util.scheduler.TestSchedulerProvider
import java.io.IOException


@RunWith(RobolectricTestRunner::class)
class DiscoverRestaurantsViewModelTest {

    companion object {
        private val EXCEPTION = IOException("Throwing fake exception to imitate failure")
        private val RESTAURANT1 = Restaurant(1, "rest1", "desc1", "status1", "url1")
        private val RESTAURANT2 = Restaurant(2, "rest2", "desc2", "status2", "url2")
    }

    private var repository = mock(RestaurantRepository::class.java)

    private val appContext: Context = ApplicationProvider.getApplicationContext()

    private lateinit var subject: DiscoverRestaurantsViewModel

    @Before
    fun setup() {
        subject = DiscoverRestaurantsViewModel(
            appContext,
            repository,
            TestSchedulerProvider()
        )
    }

    @Test
    fun discoverRestaurantsAtHQ_atFirst_returnsLoadingInProgress() {
        // setup
        whenever(repository.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.just(listOf(RESTAURANT1, RESTAURANT2)))

        val testObserver = subject
            .viewState
            .test()

        // act
        subject.discoverRestaurantsAtHQ()

        // assert
        val lastViewState = testObserver
            .assertNoErrors()
            .values()
            .first()

        assertThat(lastViewState.loadingInProgress).isTrue()
    }

    @Test
    fun discoverRestaurantsAtHQ_eventually_returnsRestaurants() {
        // setup
        val restaurants = listOf(RESTAURANT1, RESTAURANT2)
        whenever(repository.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.just(restaurants))

        val testObserver = subject
            .viewState
            .test()

        // act
        subject.discoverRestaurantsAtHQ()

        // assert
        val lastViewState = testObserver
            .assertNoErrors()
            .values()
            .last()

        assertThat(lastViewState.restaurants).isSameInstanceAs(restaurants)
    }

    @Test
    fun discoverRestaurantsAtHQ_onNetworkError_returnsErrorMessage() {
        // setup
        whenever(repository.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.error(EXCEPTION))

        val testObserver = subject
            .viewState
            .test()

        // act
        subject.discoverRestaurantsAtHQ()

        // assert
        val lastViewState = testObserver
            .assertNoErrors()
            .values()
            .last() // ignoring first "loadingInProgress" view state

        assertThat(lastViewState.errorMessage)
            .isEqualTo(appContext.getString(R.string.error_message_default))
    }

    @Test
    fun discoverRestaurantsAtHQ_calledTheSecondTime_isNoOp() {
        // setup
        val restaurants = listOf(RESTAURANT1, RESTAURANT2)
        whenever(repository.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.just(restaurants))

        val testObserver = subject
            .viewState
            .test()

        // act
        subject.discoverRestaurantsAtHQ()
        val valueCount = testObserver.valueCount()
        subject.discoverRestaurantsAtHQ()

        // assert
        testObserver.assertNoErrors()
        assertThat(testObserver.valueCount()).isEqualTo(valueCount)
    }

    @Test
    fun tryAgain_afterFailedDiscover_returnsRestaurants() {
        // setup
        val restaurants = listOf(RESTAURANT1, RESTAURANT2)
        whenever(repository.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.error(EXCEPTION), Single.just(restaurants))

        val testObserver = subject
            .viewState
            .test()

        // act
        subject.discoverRestaurantsAtHQ()
        subject.tryAgain()

        // assert
        val lastViewState = testObserver
            .assertNoErrors()
            .values()
            .last()

        assertThat(lastViewState.restaurants).isSameInstanceAs(restaurants)
    }
}
