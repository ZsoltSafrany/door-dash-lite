package safi.doordashlite.ui.viewmodel.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import safi.doordashlite.ui.viewmodel.DiscoverRestaurantsViewModel


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverRestaurantsViewModel::class)
    internal abstract fun bindDiscoverRestaurantsViewModel(viewModel: DiscoverRestaurantsViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerModelViewFactory): ViewModelProvider.Factory
}