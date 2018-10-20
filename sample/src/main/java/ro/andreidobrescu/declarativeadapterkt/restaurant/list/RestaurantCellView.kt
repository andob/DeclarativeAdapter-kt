package ro.andreidobrescu.declarativeadapterkt.restaurant.list

import android.content.Context
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cell_restaurant.view.*
import ro.andreidobrescu.declarativeadapterkt.CellView
import ro.andreidobrescu.declarativeadapterkt.R
import ro.andreidobrescu.declarativeadapterkt.model.Restaurant
import ro.andreidobrescu.declarativeadapterkt.restaurant.details.RestaurantDetailsActivityBundleBuilder

class RestaurantCellView : CellView<Restaurant>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_restaurant

    override fun setData(restaurant : Restaurant)
    {
        Glide.with(context)
                .load(restaurant.image)
                .into(imageView)

        nameTv.text=restaurant.name;
        locationTv.text=restaurant.location;
        ratingTv.text="Rating: ${restaurant.rating}/5";

        cell.setOnClickListener { view ->
            RestaurantDetailsActivityBundleBuilder()
                    .restaurant(restaurant)
                    .startActivity(view.context)
        }
    }
}
