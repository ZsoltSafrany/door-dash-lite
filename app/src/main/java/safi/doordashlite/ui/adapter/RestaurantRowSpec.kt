package safi.doordashlite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import safi.doordashlite.api.Restaurant
import safi.doordashlite.databinding.RowRestaurantBinding


class RestaurantRowSpec : RowAdapter.RowSpec<Restaurant> {

    override fun create(): RowAdapter.Row<Restaurant> {
        return Row()
    }

    private class Row : RowAdapter.Row<Restaurant> {

        private lateinit var binding: RowRestaurantBinding

        override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
            binding = RowRestaurantBinding.inflate(inflater, parent, false)
            return binding.root
        }

        override fun bind(model: Restaurant) {
            binding.name.text = model.name
            binding.description.text = model.description
            binding.status.text = model.status
            if (!model.coverImageUrl.isNullOrEmpty()) {
                Picasso.get().load(model.coverImageUrl).into(binding.thumbnail)
            }
        }
    }
}
