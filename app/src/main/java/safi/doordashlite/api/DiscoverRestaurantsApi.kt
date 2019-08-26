package safi.doordashlite.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverRestaurantsApi {

    @GET("/v2/restaurant")
    fun discoverRestaurants(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<List<Restaurant>>
}
