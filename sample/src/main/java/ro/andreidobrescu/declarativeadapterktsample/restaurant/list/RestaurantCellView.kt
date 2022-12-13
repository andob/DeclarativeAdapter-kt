package ro.andreidobrescu.declarativeadapterktsample.restaurant.list

import android.content.Context
import android.util.AttributeSet
import com.squareup.picasso.Picasso
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.CellRestaurantBinding
import ro.andreidobrescu.declarativeadapterktsample.model.Restaurant
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.RestaurantDetailsActivityBundleBuilder
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding

class RestaurantCellView : CellView<Restaurant>
{
    @AutoViewBinding
    lateinit var binding : CellRestaurantBinding
    override fun layout() : Int = R.layout.cell_restaurant

    constructor(context : Context) : super(context)
    constructor(context : Context, attrs : AttributeSet) : super(context, attrs)

    @ModelBinder
    fun setRestaurant(restaurant : Restaurant)
    {
        Picasso.get()
            .load(restaurant.image)
            .into(binding.imageView)

        binding.nameLabel.text=restaurant.name
        binding.locationLabel.text=restaurant.location
        binding.ratingLabel.text="Rating: ${restaurant.rating}/5"

        binding.cell.setOnClickListener { view ->
            RestaurantDetailsActivityBundleBuilder()
                .restaurant(restaurant)
                .startActivity(view.context)
        }
    }
}
