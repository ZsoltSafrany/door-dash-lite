package safi.doordashlite.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Preconditions
import safi.doordashlite.databinding.FragmentRestaurantDetailBinding


class RestaurantDetailFragment : DoorDashFragment() {

    private lateinit var viewBinding: FragmentRestaurantDetailBinding
    private lateinit var arguments: RestaurantDetailFragmentArgs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments =
            RestaurantDetailFragmentArgs.fromBundle(Preconditions.checkNotNull(getArguments()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setToolbarTitle(arguments.restaurantName)
        viewBinding = FragmentRestaurantDetailBinding.inflate(inflater, container, false)
        viewBinding.orderButton.setOnClickListener {
            // TODO
        }
        return viewBinding.root
    }
}
