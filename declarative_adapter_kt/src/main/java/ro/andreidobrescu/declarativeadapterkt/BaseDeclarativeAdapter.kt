package ro.andreidobrescu.declarativeadapterkt

import android.support.v7.widget.RecyclerView

abstract class BaseDeclarativeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    val items : MutableList<Any> = mutableListOf<Any>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        try
        {
            val item=items[position]
            (holder.itemView as CellView<Any>).setData(item)
        }
        catch (ex : Exception)
        {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(items: List<Any>)
    {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItems(items : List<Any>, atIndex : Int)
    {
        this.items.addAll(atIndex, items)
        notifyDataSetChanged()
    }

    fun addItems(items : List<Any>)
    {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear()
    {
        items.clear()
        notifyDataSetChanged()
    }
}
