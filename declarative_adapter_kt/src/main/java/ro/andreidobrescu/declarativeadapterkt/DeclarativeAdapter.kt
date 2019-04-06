package ro.andreidobrescu.declarativeadapterkt

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import ro.andreidobrescu.declarativeadapterkt.model.CellType
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import kotlin.reflect.KClass

class DeclarativeAdapter : BaseDeclarativeAdapter()
{
    val cellTypes : MutableList<CellType<*>> by lazy { mutableListOf<CellType<*>>() }

    override fun getItemViewType(position: Int): Int
    {
        val item=items[position]

        for ((i, cellType) in cellTypes.withIndex())
        {
            if (cellType.check(position, item))
                return i
        }

        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val view=cellTypes[viewType].viewCreator?.invoke(parent.context)

        view?.layoutParams=RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

        return object : RecyclerView.ViewHolder(view!!) {}
    }

    fun <MODEL : Any> whenInstanceOf(clazz : KClass<MODEL>, use : (Context) -> (CellView<MODEL>)) : DeclarativeAdapter
    {
        val type=CellType<MODEL>()
        type.modelClass=clazz
        type.viewCreator=use
        cellTypes.add(type)
        return this
    }

    fun <MODEL : Any> whenInstanceOf(clazz : KClass<MODEL>, and : ((Int, MODEL) -> (Boolean)), use : (Context) -> (CellView<MODEL>)) : DeclarativeAdapter
    {
        val type=CellType<MODEL>()
        type.modelClass=clazz
        type.typeChecker=and
        type.viewCreator=use
        cellTypes.add(type)
        return this
    }
}