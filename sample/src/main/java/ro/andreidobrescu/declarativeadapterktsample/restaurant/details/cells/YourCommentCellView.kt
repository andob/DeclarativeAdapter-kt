package ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells

import android.content.Context
import android.util.AttributeSet
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.CellYourCommentBinding
import ro.andreidobrescu.declarativeadapterktsample.model.Comment
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding

class YourCommentCellView : CellView<Comment>
{
    @AutoViewBinding
    lateinit var binding : CellYourCommentBinding
    override fun layout() : Int = R.layout.cell_your_comment

    private var onDeleteListener : ((Comment) -> Unit)? = null

    constructor(context : Context) : super(context)
    constructor(context : Context, attrs : AttributeSet) : super(context, attrs)
    constructor(context : Context, onDeleteListener : (Comment) -> Unit) : super(context) { this.onDeleteListener=onDeleteListener }

    @ModelBinder
    fun setComment(comment : Comment)
    {
        binding.authorLabel.text=comment.author
        binding.createdAtLabel.text=comment.createdAt
        binding.messageLabel.text=comment.message

        binding.deleteButton.setOnClickListener {
            onDeleteListener?.invoke(comment)
        }
    }
}
