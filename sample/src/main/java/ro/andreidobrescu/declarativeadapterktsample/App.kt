package ro.andreidobrescu.declarativeadapterktsample

import android.app.Application
import ro.andreidobrescu.declarativeadapterkt.listeners.OnCellViewInflatedListener
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        CellView.onCellViewInflatedListener=OnCellViewInflatedListener { cellView ->
            ReflectiveViewBindingFieldSetter.setup(cellView)
        }
    }
}
