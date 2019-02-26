package ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells

import android.content.Context
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cell_receipe.view.*
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterkt.model.Receipe
import ro.andreidobrescu.declarativeadapterkt.view.CellView

class ReceipeCellView : CellView<Receipe>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_receipe

    override fun setData(receipe : Receipe)
    {
        Glide.with(context)
                .load(receipe.image)
                .into(imageView)

        nameTv.text=receipe.name
    }
}
