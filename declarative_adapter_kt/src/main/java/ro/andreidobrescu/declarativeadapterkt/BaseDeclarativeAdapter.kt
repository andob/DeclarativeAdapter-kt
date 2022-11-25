package ro.andreidobrescu.declarativeadapterkt

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
abstract class BaseDeclarativeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    val items : MutableList<Any> = mutableListOf<Any>()

    override fun getItemCount(): Int = items.size

    open fun setItems(items : List<Any>)
    {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun addItems(index : Int, items : List<Any>)
    {
        this.items.addAll(index, items)
        notifyDataSetChanged()
    }

    open fun addItems(items : List<Any>)
    {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun clear()
    {
        items.clear()
        notifyDataSetChanged()
    }
}
