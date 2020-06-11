package ro.andreidobrescu.declarativeadapterkt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import ro.andreidobrescu.declarativeadapterkt.listeners.OnCellViewInflatedListener;

public abstract class CellView<MODEL> extends RelativeLayout
{
    public static OnCellViewInflatedListener onCellViewInflatedListener;

    public CellView(Context context)
    {
        super(context);
        inflateLayout(layout());
    }

    public CellView(Context context, int layout)
    {
        super(context);
        inflateLayout(layout);
    }

    public CellView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inflateLayout(layout());
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        inflateLayout(layout());
    }

    private void inflateLayout()
    {
        inflateLayout(layout());
    }

    private void inflateLayout(int layout)
    {
        LayoutInflater.from(getContext()).inflate(layout, this, true);

        if (onCellViewInflatedListener!=null)
            onCellViewInflatedListener.onCellViewInflated(this);
    }

    public abstract int layout();
}
