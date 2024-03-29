package ro.andreidobrescu.declarativeadapterkt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import ro.andreidobrescu.declarativeadapterkt.listeners.CellViewGlobalEvents;

public abstract class CellView<MODEL> extends RelativeLayout
{
    public CellView(Context context)
    {
        super(context);
        inflateLayout();
    }

    public CellView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inflateLayout();
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        inflateLayout();
    }

    private void inflateLayout()
    {
        LayoutInflater.from(getContext()).inflate(layout(), this, true);

        if (CellViewGlobalEvents.getOnCellViewInflatedListener()!=null)
            CellViewGlobalEvents.getOnCellViewInflatedListener().onCellViewInflated(this);
    }

    public abstract int layout();
}
