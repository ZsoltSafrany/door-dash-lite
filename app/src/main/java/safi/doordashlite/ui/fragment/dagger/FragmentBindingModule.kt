package safi.doordashlite.ui.fragment.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import safi.doordashlite.ui.fragment.DiscoverRestaurantsFragment
import safi.doordashlite.ui.fragment.RestaurantDetailFragment


@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    internal abstract fun discoverRestaurantsFragment(): DiscoverRestaurantsFragment

    @ContributesAndroidInjector
    internal abstract fun restaurantDetailFragment(): RestaurantDetailFragment
}
