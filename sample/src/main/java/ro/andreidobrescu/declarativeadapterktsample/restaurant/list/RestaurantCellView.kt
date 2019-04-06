package ro.andreidobrescu.declarativeadapterktsample.restaurant.list

import android.content.Context
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cell_restaurant.view.*
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.Restaurant
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.RestaurantDetailsActivityBundleBuilder
import java.lang.RuntimeException

class RestaurantCellView : CellView<Restaurant>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_restaurant

    override fun setData(restaurant : Restaurant)
    {
        Glide.with(context)
                .load(restaurant.image)
                .into(imageView)

        nameTv.text=restaurant.name
        locationTv.text=restaurant.location
        ratingTv.text="Rating: ${restaurant.rating}/5"

        cell.setOnClickListener { view ->
            RestaurantDetailsActivityBundleBuilder()
                    .restaurant(restaurant)
                    .startActivity(view.context)
        }

        throw RuntimeException("test")
    }
}
