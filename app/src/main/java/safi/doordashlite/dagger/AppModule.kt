package safi.doordashlite.dagger

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import safi.doordashlite.MainApplication
import safi.doordashlite.api.dagger.ApiModule
import safi.doordashlite.model.dagger.ModelModule
import safi.doordashlite.ui.fragment.dagger.FragmentBindingModule
import safi.doordashlite.ui.viewmodel.dagger.ViewModelModule
import safi.doordashlite.util.scheduler.dagger.SchedulerModule


@Module(
    includes = [
        ApiModule::class,
        FragmentBindingModule::class,
        ModelModule::class,
        ViewModelModule::class,
        SchedulerModule::class
    ]
)
abstract class AppModule {

    @Binds
    internal abstract fun application(app: MainApplication): Application

    @Binds
    @AppContext
    internal abstract fun applicationContext(app: MainApplication): Context
}