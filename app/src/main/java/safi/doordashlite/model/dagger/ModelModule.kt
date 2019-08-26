package safi.doordashlite.model.dagger

import dagger.Binds
import dagger.Module
import safi.doordashlite.model.DefaultRestaurantRepository
import safi.doordashlite.model.RestaurantRepository


@Module
abstract class ModelModule {

    @Binds
    internal abstract fun bindRestaurantRepository(repository: DefaultRestaurantRepository): RestaurantRepository
}
