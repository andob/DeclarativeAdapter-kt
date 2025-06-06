package ro.andreidobrescu.declarativeadapterkt

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Toast
import ro.andreidobrescu.declarativeadapterkt.listeners.CellViewGlobalEvents
import ro.andreidobrescu.declarativeadapterkt.listeners.OnCellViewInflatedListener
import ro.andreidobrescu.declarativeadapterkt.model.CellType
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder
import java.lang.reflect.InvocationTargetException

open class DeclarativeAdapter : BaseDeclarativeAdapter<Any>()
{
    interface ExceptionLogger
    {
        fun <MODEL> log(cellView : CellView<MODEL>, model : MODEL, exception : Throwable)
    }

    companion object
    {
        @JvmStatic
        var exceptionLogger = object : ExceptionLogger {
            override fun <MODEL> log(cellView : CellView<MODEL>, model : MODEL, exception : Throwable)
            {
                val context = cellView.context!!
                val appInfo = context.packageManager.getApplicationInfo(context.packageName, 0)
                val appIsDebuggable = appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
                if (appIsDebuggable)
                {
                    Toast.makeText(cellView.context!!, exception.message, Toast.LENGTH_SHORT).show()
                    exception.printStackTrace()
                }
            }
        }
    }

    protected val cellTypes = mutableListOf<CellType<*>>()

    override fun getItemViewType(position : Int) : Int
    {
        val item = items[position]

        for ((index, cellType) in cellTypes.withIndex())
        {
            if (cellType.isModelApplicable(index = position, item = item,
                classComparer = { itemType, targetType -> itemType == targetType }))
                return index
        }

        for ((index, cellType) in cellTypes.withIndex())
        {
            if (cellType.isModelApplicable(index = position, item = item,
                classComparer = { itemType, targetType ->
                    itemType.superclass == targetType ||
                        targetType.isAssignableFrom(itemType)
                }))
                return index
        }

        throw RuntimeException("Invalid adapter configuration! Item: $item, item type: ${item::class.java.simpleName}")
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder
    {
        val cellType = cellTypes[viewType]
        val cellView = cellType.viewCreator!!.invoke(parent.context)
        cellView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        val viewHolder = object : RecyclerView.ViewHolder(cellView) {}

        if (cellType.viewModelBinderMethod == null)
        {
            cellType.viewModelBinderMethod = cellView::class.java.declaredMethods.find { method ->
                method.annotations.filterIsInstance<ModelBinder>().isNotEmpty()
            }
        }

        (cellView.context as? OnCellViewInflatedListener)?.onCellViewInflated(cellView)

        return viewHolder
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int)
    {
        val cellType = cellTypes[getItemViewType(position)]
        if (cellType.viewModelBinderMethod != null)
        {
            val model = items[position]
            val cellView = holder.itemView as CellView<Any>

            try
            {
                cellType.viewModelBinderMethod?.invoke(cellView, model)

                CellViewGlobalEvents.getOnCellViewBindedListener()
                    ?.onCellViewBindedToModel(cellView, model)
            }
            catch (ex : Exception)
            {
                if (ex is InvocationTargetException)
                    exceptionLogger.log(cellView, model, ex.targetException)
                else exceptionLogger.log(cellView, model, ex)
            }
        }
    }

    inline fun <reified MODEL : Any> whenInstanceOf() = whenInstanceOf(clazz = MODEL::class.java)
    fun <MODEL : Any> whenInstanceOf(clazz : Class<MODEL>) = Builder(adapter = this, modelType = clazz)

    class Builder<MODEL : Any>(val adapter : DeclarativeAdapter, val modelType : Class<MODEL>)
    {
        fun use(factory : (Context) -> CellView<MODEL>) =
            adapter.whenInstanceOf(modelType, use = factory)

        fun and(predicate : (MODEL) -> Boolean) = Builder2(adapter = adapter, modelType = modelType, predicate = predicate)

        class Builder2<MODEL : Any>(val adapter : DeclarativeAdapter, val modelType : Class<MODEL>, val predicate : (MODEL) -> Boolean)
        {
            fun use(factory : (Context) -> CellView<MODEL>) =
                adapter.whenInstanceOf(modelType, and = predicate, use = factory)
        }
    }

    fun <MODEL : Any> whenInstanceOf(clazz : Class<MODEL>, use : (Context) -> CellView<MODEL>) : DeclarativeAdapter
    {
        val type = CellType<MODEL>()
        type.modelClass = clazz
        type.viewCreator = use
        cellTypes.add(type)
        return this
    }

    fun <MODEL : Any> whenInstanceOf(clazz : Class<MODEL>, and : (MODEL) -> Boolean, use : (Context) -> CellView<MODEL>) : DeclarativeAdapter
    {
        val type = CellType<MODEL>()
        type.modelClass = clazz
        type.extraChecker = and
        type.viewCreator = use
        cellTypes.add(type)
        return this
    }
}