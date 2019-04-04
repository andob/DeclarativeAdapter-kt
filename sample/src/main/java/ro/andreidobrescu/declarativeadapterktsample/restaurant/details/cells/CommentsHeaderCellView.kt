package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.CommentsHeader

class CommentsHeaderCellView : CellView<CommentsHeader>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_comments_header

    override fun setData(data : CommentsHeader)
    {
    }
}
