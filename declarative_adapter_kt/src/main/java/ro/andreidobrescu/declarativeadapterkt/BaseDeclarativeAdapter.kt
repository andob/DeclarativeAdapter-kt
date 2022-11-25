package ro.andreidobrescu.declarativeadapterkt

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
abstract class BaseDeclarativeAdapter<MODEL> : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    val items : MutableList<MODEL> = mutableListOf<MODEL>()

    override fun getItemCount(): Int = items.size

    open fun setItems(items : List<MODEL>)
    {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    open fun addItems(index : Int, items : List<MODEL>)
    {
        this.items.addAll(index, items)
        notifyDataSetChanged()
    }

    open fun addItems(items : List<MODEL>)
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
