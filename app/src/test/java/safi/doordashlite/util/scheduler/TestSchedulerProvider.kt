package safi.doordashlite.util.scheduler

import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ImmediateThinScheduler


class TestSchedulerProvider : SchedulerProvider {

    override fun io(): Scheduler = ImmediateThinScheduler.INSTANCE

    override fun computation(): Scheduler = ImmediateThinScheduler.INSTANCE

    override fun mainThread(): Scheduler = ImmediateThinScheduler.INSTANCE
}
