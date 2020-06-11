package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.CellCommentBinding
import ro.andreidobrescu.declarativeadapterktsample.model.Comment
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding

class CommentCellView : CellView<Comment>
{
    @AutoViewBinding
    lateinit var binding : CellCommentBinding

    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_comment

    @ModelBinder
    fun setComment(comment : Comment)
    {
        binding.authorLabel.text=comment.author
        binding.createdAtLabel.text=comment.createdAt
        binding.messageLabel.text=comment.message
    }
}
