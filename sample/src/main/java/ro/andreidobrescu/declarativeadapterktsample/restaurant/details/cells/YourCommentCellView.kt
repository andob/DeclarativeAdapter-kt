package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import kotlinx.android.synthetic.main.cell_your_comment.view.*
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.model.Comment

class YourCommentCellView
(
    context : Context?,
    private val onDeleteListener : (Comment) -> (Unit)
) : CellView<Comment>(context)
{
    override fun layout() : Int = R.layout.cell_your_comment

    @ModelBinder
    fun setComment(comment : Comment)
    {
        authorLabel.text=comment.author
        createdAtLabel.text=comment.createdAt
        messageLabel.text=comment.message

        deleteButton.setOnClickListener {
            onDeleteListener(comment)
        }
    }
}
