package ro.andreidobrescu.declarativeadapterkt.sticky_headers

import android.content.Context
import android.os.Handler
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ro.andreidobrescu.declarativeadapterkt.DeclarativeAdapter
import ro.andreidobrescu.declarativeadapterkt.stickyheaders.R
import ro.andreidobrescu.declarativeadapterkt.view.CellView

class AdapterWithStickyHeaders
(
    recyclerView : RecyclerView,
    val delegate : DeclarativeAdapter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    init
    {
        recyclerView.addItemDecoration(StickyHeaderItemDecoration(
            stickyHeaderViewInstantiator = instantiator@ { index ->
                val descriptors=getStickyHeaderDescriptors()
                val descriptor=descriptors.findLast { index>=it.index }
                return@instantiator descriptor?.stickyHeaderViewCreator
                    ?.invoke(recyclerView.context) as? StickyHeaderView<*>
            },
            stickyHeaderModelTypeProvider = provider@ { index ->
                val descriptors=getStickyHeaderDescriptors()
                val descriptor=descriptors.findLast { index>=it.index }
                return@provider descriptor?.stickyHeaderModelType
            }
        ))
    }

    private class StickyHeaderDescriptor
    (
        val stickyHeaderViewCreator : ((Context) -> (CellView<out Any>))?,
        val stickyHeaderModelType : Class<*>,
        val index : Int
    )

    private fun getStickyHeaderDescriptors() : List<StickyHeaderDescriptor>
    {
        return delegate.cellTypes
            .filter { cellType ->
                StickyHeaderModel::class.java.isAssignableFrom(cellType.modelClass as Class<*>)
            }
            .mapNotNull { cellType ->
                val modelWithIndex=delegate.items.withIndex().find { (index, item) ->
                    item::class.java==cellType.modelClass
                }?:return@mapNotNull null

                StickyHeaderDescriptor(
                    stickyHeaderViewCreator = cellType.viewCreator,
                    stickyHeaderModelType = modelWithIndex.value::class.java,
                    index = modelWithIndex.index
                )
            }
    }

    override fun getItemCount() = delegate.itemCount
    override fun getItemViewType(position : Int) = delegate.getItemViewType(position)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder
    {
        val viewHolder=delegate.onCreateViewHolder(parent, viewType)

        viewHolder.itemView.viewTreeObserver.addOnDrawListener(object : ViewTreeObserver.OnDrawListener
        {
            override fun onDraw()
            {
                if (viewHolder.itemView.context.resources.getBoolean(R.bool.isTablet)&&
                    viewHolder.itemView is StickyHeaderView<*>&&
                    viewHolder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams)
                {
                    val params=viewHolder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
                    params.isFullSpan=true
                    viewHolder.itemView.layoutParams=params
                }

                Handler().post {
                    viewHolder.itemView.viewTreeObserver.removeOnDrawListener(this)
                }
            }
        })

        return viewHolder
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position : Int)
    {
        delegate.onBindViewHolder(holder, position)
    }
}
