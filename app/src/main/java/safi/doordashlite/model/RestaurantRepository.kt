package safi.doordashlite.model

import io.reactivex.Single
import safi.doordashlite.api.Restaurant

interface RestaurantRepository {

    fun discoverRestaurants(
        latitude: Double,
        longitude: Double,
        offset: Int,
        limit: Int
    ): Single<List<Restaurant>>
}
