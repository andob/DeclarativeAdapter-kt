package ro.andreidobrescu.declarativeadapterkt.sticky_headers

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ro.andreidobrescu.declarativeadapterkt.model.ModelBinder

class StickyHeaderItemDecoration
(
    private val stickyHeaderViewInstantiator : (Int) -> StickyHeaderView<*>?,
    private val stickyHeaderModelTypeProvider : (Int) -> Class<*>?
) : RecyclerView.ItemDecoration()
{
    override fun onDrawOver(canvas : Canvas, recyclerView : RecyclerView, state : RecyclerView.State)
    {
        super.onDrawOver(canvas, recyclerView, state)

        val topChild=recyclerView.getChildAt(0)?:return

        val topChildPosition=recyclerView.getChildAdapterPosition(topChild)
        if (topChildPosition==RecyclerView.NO_POSITION)
            return

        val stickyHeaderView=stickyHeaderViewInstantiator.invoke(topChildPosition)?:return
        val stickyHeaderModelType=stickyHeaderModelTypeProvider.invoke(topChildPosition)?:return

        fun List<Any>.findHeaderModel() : Any?
        {
            for (i in topChildPosition downTo 0)
                if (this[i]::class.java==stickyHeaderModelType)
                    return this[i]
            return null
        }

        val items=(recyclerView.adapter as DeclarativeAdapterWithStickyHeaders).items
        val headerModel=items.findHeaderModel()
        if (headerModel!=null)
        {
            val viewModelBinderMethod=stickyHeaderView::class.java.declaredMethods
                .find { method -> method.annotations.find { it is ModelBinder }!=null }

            if (viewModelBinderMethod!=null)
            {
                try { viewModelBinderMethod.invoke(stickyHeaderView, headerModel) }
                catch (ex : Exception) {}
            }
        }

        val recyclerViewWidthSpec=View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY)
        val recyclerViewHeightSpec=View.MeasureSpec.makeMeasureSpec(recyclerView.height, View.MeasureSpec.EXACTLY)
        val stickyHeaderViewWidthSpec=ViewGroup.getChildMeasureSpec(recyclerViewWidthSpec,
            recyclerView.paddingLeft+recyclerView.paddingRight, ViewGroup.LayoutParams.MATCH_PARENT)
        val stickyHeaderViewHeightSpec=ViewGroup.getChildMeasureSpec(recyclerViewHeightSpec,
            recyclerView.paddingTop+recyclerView.paddingBottom, ViewGroup.LayoutParams.WRAP_CONTENT)
        stickyHeaderView.measure(stickyHeaderViewWidthSpec, stickyHeaderViewHeightSpec)
        stickyHeaderView.layout(0, 0, stickyHeaderView.measuredWidth, stickyHeaderView.measuredHeight)

        val contactPoint=stickyHeaderView.bottom
        val childInContact=getChildInContact(recyclerView, contactPoint)?:return
        if (childInContact is StickyHeaderView<*>)
            moveStickyHeader(canvas, stickyHeaderView, childInContact)
        else drawStickyHeader(canvas, stickyHeaderView)
    }

    private fun drawStickyHeader(canvas : Canvas, stickyHeaderView : StickyHeaderView<*>)
    {
        canvas.save()
        canvas.translate(0.0f, 0.0f)
        stickyHeaderView.draw(canvas)
        canvas.restore()
    }

    private fun moveStickyHeader(canvas : Canvas,
                                 currentStickyHeaderView : StickyHeaderView<*>,
                                 nextStickyHeaderView : StickyHeaderView<*>)
    {
        canvas.save()
        canvas.translate(0.0f, (nextStickyHeaderView.top-currentStickyHeaderView.height).toFloat())
        currentStickyHeaderView.draw(canvas)
        canvas.restore()
    }

    private fun getChildInContact(recyclerView : RecyclerView, contactPoint : Int) : View?
    {
        for (i in 0 until recyclerView.childCount)
        {
            val child=recyclerView.getChildAt(i)
            if (child.top<=contactPoint&&contactPoint<child.bottom)
                return child
        }

        return null
    }
}
