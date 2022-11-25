package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.CellYourCommentBinding
import ro.andreidobrescu.declarativeadapterktsample.model.Comment
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding

class YourCommentCellView
(
    context : Context?,
    private val onDeleteListener : (Comment) -> Unit
) : CellView<Comment>(context)
{
    @AutoViewBinding
    lateinit var binding : CellYourCommentBinding

    override fun layout() : Int = R.layout.cell_your_comment

    @ModelBinder
    fun setComment(comment : Comment)
    {
        binding.authorLabel.text=comment.author
        binding.createdAtLabel.text=comment.createdAt
        binding.messageLabel.text=comment.message

        binding.deleteButton.setOnClickListener {
            onDeleteListener(comment)
        }
    }
}
