package safi.doordashlite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable


abstract class DoorDashFragment : DaggerFragment() {

    protected lateinit var disposables: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        disposables = CompositeDisposable()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.dispose()
    }

    protected fun setToolbarTitle(@StringRes titleId: Int) {
        setToolbarTitle(getString(titleId))
    }

    protected fun setToolbarTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar!!.title = title
    }
}
