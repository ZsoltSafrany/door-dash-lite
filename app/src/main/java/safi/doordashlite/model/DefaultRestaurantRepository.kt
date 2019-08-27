package safi.doordashlite.model

import android.content.Context
import androidx.core.util.Preconditions
import io.reactivex.Single
import safi.doordashlite.api.DiscoverRestaurantsApi
import safi.doordashlite.api.Restaurant
import safi.doordashlite.dagger.AppContext
import javax.inject.Inject

class DefaultRestaurantRepository @Inject constructor(
    private val api: DiscoverRestaurantsApi,
    @AppContext context: Context
) : RestaurantRepository {

    companion object {
        private const val SHARED_PREFS_NAME_FAVORITES = "favorite_restaurant"
    }

    private val sharedPrefs =
        context.getSharedPreferences(SHARED_PREFS_NAME_FAVORITES, Context.MODE_PRIVATE)

    override fun discoverRestaurants(
        latitude: Double,
        longitude: Double,
        offset: Int,
        limit: Int
    ): Single<List<Restaurant>> {
        Preconditions.checkArgument(offset >= 0)
        Preconditions.checkArgument(limit > 0)
        return api
            .discoverRestaurants(latitude, longitude, offset, limit)
            .map { it.map { it.copy(favorite = isFavorite(it.id)) } }
    }

    private fun isFavorite(restaurantId: Long): Boolean {
        return sharedPrefs.getBoolean(restaurantId.toString(), false)
    }

    override fun setFavorite(restaurantId: Long, favorite: Boolean) {
        sharedPrefs
            .edit()
            .putBoolean(restaurantId.toString(), favorite)
            .apply()
    }
}
