package safi.doordashlite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import safi.doordashlite.R
import safi.doordashlite.api.DiscoverRestaurantsApi
import safi.doordashlite.api.Restaurant
import safi.doordashlite.databinding.FragmentDiscoverRestaurantsBinding
import safi.doordashlite.ui.adapter.RestaurantRowSpec
import safi.doordashlite.ui.adapter.RowAdapter
import safi.doordashlite.ui.viewmodel.DiscoverRestaurantsViewModel
import safi.doordashlite.util.extension.bind
import safi.doordashlite.util.extension.visible
import safi.doordashlite.util.scheduler.SchedulerProvider
import javax.inject.Inject


class DiscoverRestaurantsFragment : DoorDashFragment() {

    @Inject
    lateinit var api: DiscoverRestaurantsApi

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    private lateinit var viewBinding: FragmentDiscoverRestaurantsBinding
    private lateinit var viewModel: DiscoverRestaurantsViewModel
    private lateinit var adapter: RowAdapter<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(DiscoverRestaurantsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setToolbarTitle(R.string.title_discover_restaurants)
        viewBinding = FragmentDiscoverRestaurantsBinding.inflate(inflater, container, false)
        initResultListView()

        viewModel.discoverRestaurantsAtHQ()
        viewModel
            .viewState
            .subscribe { onViewStateUpdate(it) }
            .bind(disposables)

        return viewBinding.root
    }

    private fun onViewStateUpdate(viewState: DiscoverRestaurantsViewModel.ViewState) {
        viewBinding.errorMessage.visible = viewState.hasErrorMessage()
        viewBinding.tryAgain.visible = viewState.hasErrorMessage()
        viewBinding.tryAgain.isEnabled = !viewState.loadingInProgress
        viewBinding.resultList.visible = !viewState.hasErrorMessage()
        if (viewState.restaurants != null) {
            adapter.update(viewState.restaurants)
        }
        if (viewState.hasErrorMessage()) {
            viewBinding.errorMessage.text = viewState.errorMessage
            viewBinding.tryAgain.setOnClickListener {
                viewModel.tryAgain()
            }
        }
    }

    private fun initResultListView() {
        viewBinding.resultList.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        viewBinding.resultList.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        viewBinding.resultList.addItemDecoration(itemDecoration)

        adapter = RowAdapter(RestaurantRowSpec())
        adapter
            .clickEvent
            .observeOn(schedulerProvider.mainThread())
            .subscribe { onRestaurantClick(it) }
            .bind(disposables)
        viewBinding.resultList.adapter = adapter
    }

    private fun onRestaurantClick(restaurant: Restaurant) {
        val action = DiscoverRestaurantsFragmentDirections
            .actionDiscoverRestaurantsToRestaurantDetail(restaurant.id, restaurant.name)
        NavHostFragment.findNavController(this).navigate(action)
    }
}
