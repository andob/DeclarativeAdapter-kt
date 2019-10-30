package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_receipe.view.*
import ro.andreidobrescu.declarativeadapterkt.internal.CellView
import ro.andreidobrescu.declarativeadapterkt.internal.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.Receipe

class ReceipeCellView : CellView<Receipe>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_receipe

    @ModelBinder
    fun setReceipe(receipe : Receipe)
    {
        Picasso.get()
                .load(receipe.image)
                .into(imageView)

        nameLabel.text=receipe.name
    }
}
