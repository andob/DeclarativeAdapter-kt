package ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells

import android.content.Context
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterkt.model.CommentsHeader
import ro.andreidobrescu.declarativeadapterkt.view.CellView

class CommentsHeaderCellView : CellView<CommentsHeader>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_comments_header

    override fun setData(data : CommentsHeader)
    {
    }
}
