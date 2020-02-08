package ro.andreidobrescu.declarativeadapterkt

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

open class SimpleDeclarativeAdapter<MODEL>
(
    val viewCreator : (Context) -> (CellView<MODEL>)
) : BaseDeclarativeAdapter()
{
    private var binderMethod : Method? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val view=viewCreator(parent.context)
        view.layoutParams=RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        val viewHolder=object : RecyclerView.ViewHolder(view) {}

        if (binderMethod==null)
            binderMethod=view::class.java.declaredMethods.find { method ->
                method.annotations.filterIsInstance<ModelBinder>().isNotEmpty()
            }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if (binderMethod!=null)
        {
            val model=items[position]
            val cellView=holder.itemView as CellView<Any>

            try
            {
                binderMethod?.invoke(cellView, model)

                (cellView.context as? OnCellViewBindedListener)?.onCellViewBindedToModel(cellView, model)
            }
            catch (ex : Exception)
            {
                if (ex is InvocationTargetException)
                    DeclarativeAdapter.exceptionLogger.log(cellView, model, ex.targetException)
                else DeclarativeAdapter.exceptionLogger.log(cellView, model, ex)
            }
        }
    }
}
