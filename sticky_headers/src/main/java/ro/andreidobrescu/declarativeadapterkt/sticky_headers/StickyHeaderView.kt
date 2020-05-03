package ro.andreidobrescu.declarativeadapterkt.sticky_headers

import android.content.Context
import android.util.AttributeSet
import ro.andreidobrescu.declarativeadapterkt.view.CellView

abstract class StickyHeaderView<MODEL> : CellView<MODEL>
{
    constructor(context : Context?) : super(context)
    constructor(context : Context?, attrs : AttributeSet?) : super(context, attrs)
}
