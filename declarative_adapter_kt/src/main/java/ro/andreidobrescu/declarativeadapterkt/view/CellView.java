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
        init(layout());
    }

    public CellView(Context context, int layout)
    {
        super(context);
        init(layout);
    }

    public CellView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(layout());
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(layout());
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(layout());
    }

    public void init()
    {
        init(layout());
    }

    public void init(int layout)
    {
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layout, this, true);
    }

    public abstract int layout();
    public abstract void setData(MODEL data);

    public boolean isSticky()
    {
        return false;
    }
}
