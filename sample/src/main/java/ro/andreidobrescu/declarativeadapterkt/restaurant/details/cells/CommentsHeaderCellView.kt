package ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells

import android.content.Context
import ro.andreidobrescu.declarativeadapterkt.CellView
import ro.andreidobrescu.declarativeadapterkt.R
import ro.andreidobrescu.declarativeadapterkt.model.CommentsHeader

class CommentsHeaderCellView : CellView<CommentsHeader>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_comments_header

    override fun setData(data : CommentsHeader)
    {
    }
}
