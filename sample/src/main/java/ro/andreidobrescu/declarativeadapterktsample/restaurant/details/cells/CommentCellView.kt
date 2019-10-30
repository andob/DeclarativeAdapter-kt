package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import kotlinx.android.synthetic.main.cell_comment.view.*
import ro.andreidobrescu.declarativeadapterkt.internal.CellView
import ro.andreidobrescu.declarativeadapterkt.internal.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.Comment

class CommentCellView : CellView<Comment>
{
    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_comment

    @ModelBinder
    fun setComment(comment : Comment)
    {
        authorLabel.text=comment.author
        createdAtLabel.text=comment.createdAt
        messageLabel.text=comment.message
    }
}
