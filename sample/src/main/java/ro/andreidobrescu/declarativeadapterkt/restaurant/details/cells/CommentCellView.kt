package ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells

import android.content.Context
import kotlinx.android.synthetic.main.cell_comment.view.*
import ro.andreidobrescu.declarativeadapterkt.CellView
import ro.andreidobrescu.declarativeadapterkt.R
import ro.andreidobrescu.declarativeadapterkt.model.Comment

class CommentCellView : CellView<Comment>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_comment

    override fun setData(comment : Comment)
    {
        authorTv.text=comment.author
        createdAtTv.text=comment.createdAt
        messageTv.text=comment.message
    }
}
