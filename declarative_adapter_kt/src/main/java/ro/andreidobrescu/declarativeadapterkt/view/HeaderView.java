package ro.andreidobrescu.declarativeadapterkt.view;

import android.content.Context;
import android.util.AttributeSet;

public abstract class HeaderView<MODEL> extends CellView<MODEL>
{
    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, int layout) {
        super(context, layout);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean isSticky()
    {
        return true;
    }
}
