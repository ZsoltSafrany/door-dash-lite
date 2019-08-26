package safi.doordashlite.model

import androidx.core.util.Preconditions
import io.reactivex.Single
import safi.doordashlite.api.DiscoverRestaurantsApi
import safi.doordashlite.api.Restaurant
import javax.inject.Inject

class DefaultRestaurantRepository @Inject constructor(
    private val api: DiscoverRestaurantsApi
) : RestaurantRepository {

    override fun discoverRestaurants(
        latitude: Double,
        longitude: Double,
        offset: Int,
        limit: Int
    ): Single<List<Restaurant>> {
        Preconditions.checkArgument(offset >= 0)
        Preconditions.checkArgument(limit > 0)
        return api.discoverRestaurants(latitude, longitude, offset, limit)
    }
}
