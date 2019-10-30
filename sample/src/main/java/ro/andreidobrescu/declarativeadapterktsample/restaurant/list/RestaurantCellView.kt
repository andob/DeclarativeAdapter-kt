package ro.andreidobrescu.declarativeadapterktsample.restaurant.list

import android.content.Context
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_restaurant.view.*
import ro.andreidobrescu.declarativeadapterkt.internal.CellView
import ro.andreidobrescu.declarativeadapterkt.internal.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.Restaurant
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.RestaurantDetailsActivityBundleBuilder

class RestaurantCellView : CellView<Restaurant>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_restaurant

    @ModelBinder
    fun setRestaurant(restaurant : Restaurant)
    {
        Picasso.get()
                .load(restaurant.image)
                .into(imageView)

        nameLabel.text=restaurant.name
        locationLabel.text=restaurant.location
        ratingLabel.text="Rating: ${restaurant.rating}/5"

        cell.setOnClickListener { view ->
            RestaurantDetailsActivityBundleBuilder()
                    .restaurant(restaurant)
                    .startActivity(view.context)
        }
    }
}
