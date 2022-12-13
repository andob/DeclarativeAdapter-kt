package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import android.util.AttributeSet
import ro.andreidobrescu.declarativeadapterkt.sticky_headers.StickyHeaderView
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.CommentsStickyHeader

class CommentsHeaderCellView : StickyHeaderView<CommentsStickyHeader>
{
    constructor(context : Context) : super(context)
    constructor(context : Context, attrs : AttributeSet) : super(context, attrs)

    override fun layout() : Int = R.layout.cell_comments_header
}
