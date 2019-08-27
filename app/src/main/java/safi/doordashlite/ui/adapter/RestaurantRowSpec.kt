package safi.doordashlite.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import safi.doordashlite.api.Restaurant
import safi.doordashlite.databinding.RowRestaurantBinding


class RestaurantRowSpec : RowAdapter.RowSpec<Restaurant> {

    private val favoriteChangeEventSubject = PublishSubject.create<FavoriteChangeEvent>()
    val favoriteChangeEvent: Observable<FavoriteChangeEvent> = favoriteChangeEventSubject

    override fun create(): RowAdapter.Row<Restaurant> {
        return Row()
    }

    private inner class Row : RowAdapter.Row<Restaurant> {

        private lateinit var binding: RowRestaurantBinding

        override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
            binding = RowRestaurantBinding.inflate(inflater, parent, false)
            return binding.root
        }

        override fun bind(model: Restaurant, modelMutator: (Restaurant) -> Unit) {
            binding.name.text = model.name
            binding.description.text = model.description
            binding.status.text = model.status
            if (!model.coverImageUrl.isNullOrEmpty()) {
                Picasso.get().load(model.coverImageUrl).into(binding.thumbnail)
            }
            binding.favorite.isChecked = model.favorite
            binding.favorite.setOnClickListener {
                val favorite = binding.favorite.isChecked
                modelMutator(model.copy(favorite = favorite))
                favoriteChangeEventSubject.onNext(FavoriteChangeEvent(model.id, favorite))
            }
        }
    }

    data class FavoriteChangeEvent(
        val restaurantId: Long,
        val favorite: Boolean
    )
}
