package ro.andreidobrescu.declarativeadapterkt.internal;

import android.content.Context;
import android.util.AttributeSet;

import kotlin.Unit;

public abstract class CustomView extends CellView<Unit>
{
    public CustomView(Context context)
    {
        super(context);
    }

    public CustomView(Context context, int layout)
    {
        super(context, layout);
    }

    public CustomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
}
