package safi.doordashlite.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import safi.doordashlite.api.DiscoverRestaurantsApi
import safi.doordashlite.api.Restaurant
import java.io.IOException


@RunWith(RobolectricTestRunner::class)
class DefaultRestaurantRepositoryTest {

    companion object {
        private val EXCEPTION = IOException("Throwing fake exception to imitate failure")
        private val RESTAURANT1 = Restaurant(1, "rest1", "desc1", "status1", "url1", false)
        private val RESTAURANT2 = Restaurant(2, "rest2", "desc2", "status2", "url2", false)

        private const val LATITUDE = 1.2
        private const val LONGITUDE = 2.3
        private const val OFFSET = 0
        private const val LIMIT = 10
    }

    private val appContext: Context = ApplicationProvider.getApplicationContext()
    private val api = mock(DiscoverRestaurantsApi::class.java)

    private lateinit var subject: DefaultRestaurantRepository

    @Before
    fun setup() {
        subject = DefaultRestaurantRepository(api, appContext)
    }

    @Test(expected = IllegalArgumentException::class)
    fun discoverRestaurants_onInvalidOffset_throws() {
        subject.discoverRestaurants(LATITUDE, LONGITUDE, -1, LIMIT)
    }

    @Test(expected = IllegalArgumentException::class)
    fun discoverRestaurants_onInvalidLimit_throws() {
        subject.discoverRestaurants(LATITUDE, LONGITUDE, OFFSET, 0)
    }

    @Test
    fun discoverRestaurants_onNetworkError_returnsError() {
        // setup
        whenever(api.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.error(EXCEPTION))

        // act & assert
        subject
            .discoverRestaurants(LATITUDE, LONGITUDE, OFFSET, LIMIT)
            .test()
            .assertNoValues()
            .assertError(EXCEPTION.javaClass)
    }

    @Test
    fun discoverRestaurants_returnsRestaurants() {
        // setup
        val restaurants = listOf(RESTAURANT1, RESTAURANT2)
        whenever(api.discoverRestaurants(anyDouble(), anyDouble(), anyInt(), anyInt()))
            .thenReturn(Single.just(restaurants))

        // act & assert
        subject
            .discoverRestaurants(LATITUDE, LONGITUDE, OFFSET, LIMIT)
            .test()
            .assertNoErrors()
            .assertValue(restaurants)
    }
}
