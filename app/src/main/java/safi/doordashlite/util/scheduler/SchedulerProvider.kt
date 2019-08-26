package safi.doordashlite.util.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun mainThread(): Scheduler
}
