package safi.doordashlite.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import safi.doordashlite.R
import safi.doordashlite.api.Restaurant
import safi.doordashlite.dagger.AppContext
import safi.doordashlite.model.RestaurantRepository
import safi.doordashlite.util.scheduler.SchedulerProvider
import javax.inject.Inject

class DiscoverRestaurantsViewModel @Inject constructor(
    @AppContext private val context: Context,
    private val restaurantRepository: RestaurantRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {

    companion object {
        private const val LATITUDE = 37.422740
        private const val LONGITUDE = -122.139956
        private const val OFFSET = 0
        private const val LIMIT = 30
    }

    private var disposable: Disposable? = null
    private var longitude: Double? = null
    private var latitude: Double? = null

    private val viewStateSubject = BehaviorSubject.create<ViewState>()
    val viewState: Observable<ViewState> = viewStateSubject

    override fun onCleared() {
        disposable?.dispose()
    }

    fun discoverRestaurantsAtHQ() {
        discoverRestaurants(LATITUDE, LONGITUDE)
    }

    fun tryAgain() {
        val latitude = this.latitude!!
        val longitude = this.longitude!!
        this.longitude = null
        this.latitude = null
        discoverRestaurants(latitude, longitude)
    }

    private fun discoverRestaurants(latitude: Double, longitude: Double) {
        if (this.latitude == latitude && this.longitude == longitude) {
            // Do not fetch again in case of screen rotation
            return
        }
        this.latitude = latitude
        this.longitude = longitude

        viewStateSubject.onNext(createLoadingInProgressViewState())
        disposable?.dispose()
        disposable = restaurantRepository
            .discoverRestaurants(latitude, longitude, OFFSET, LIMIT)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())
            .map { ViewState(restaurants = it) }
            .onErrorReturn { ViewState(errorMessage = context.getString(R.string.error_message_default)) }
            .subscribe { viewState -> viewStateSubject.onNext(viewState) }
    }

    private fun createLoadingInProgressViewState(): ViewState {
        val currentState = viewStateSubject.value
        return currentState?.copy(loadingInProgress = true) ?: ViewState(loadingInProgress = true)
    }

    fun setFavorite(restaurantId: Long, favorite: Boolean) {
        restaurantRepository.setFavorite(restaurantId, favorite)
    }

    data class ViewState(
        val restaurants: List<Restaurant>? = null,
        val errorMessage: String? = null,
        val loadingInProgress: Boolean = false
    ) {
        fun hasErrorMessage() = errorMessage != null
    }
}
