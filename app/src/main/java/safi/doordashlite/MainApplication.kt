package safi.doordashlite

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import safi.doordashlite.dagger.DaggerAppComponent


class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}
