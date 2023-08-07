package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import android.util.AttributeSet
import com.squareup.picasso.Picasso
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.CellReceipeBinding
import ro.andreidobrescu.declarativeadapterktsample.model.Receipe
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding

class ReceipeCellView : CellView<Receipe>
{
    @AutoViewBinding
    lateinit var binding : CellReceipeBinding
    override fun layout() : Int = R.layout.cell_receipe

    constructor(context : Context) : super(context)
    constructor(context : Context, attrs : AttributeSet) : super(context, attrs)

    @ModelBinder
    fun setReceipe(receipe : Receipe)
    {
        //todo
        Picasso.get()
                .load(receipe.image)
                .into(binding.imageView)

        binding.nameLabel.text = receipe.name
    }
}
