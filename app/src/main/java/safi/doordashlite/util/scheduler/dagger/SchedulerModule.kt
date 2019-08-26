package safi.doordashlite.util.scheduler.dagger

import dagger.Binds
import dagger.Module
import safi.doordashlite.util.scheduler.DefaultSchedulerProvider
import safi.doordashlite.util.scheduler.SchedulerProvider

@Module
abstract class SchedulerModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: DefaultSchedulerProvider): SchedulerProvider
}
