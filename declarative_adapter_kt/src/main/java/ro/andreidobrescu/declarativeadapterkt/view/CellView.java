package ro.andreidobrescu.declarativeadapterkt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public abstract class CellView<MODEL> extends RelativeLayout
{
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
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layout, this, true);
    }

    public abstract int layout();
}
