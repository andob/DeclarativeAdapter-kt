package ro.andreidobrescu.declarativeadapterktsample

import android.app.Application
import ro.andreidobrescu.declarativeadapterkt.listeners.CellViewGlobalEvents
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        CellViewGlobalEvents.setOnCellViewInflatedListener { cellView ->
            ReflectiveViewBindingFieldSetter.setup(cellView)
        }

        CellViewGlobalEvents.setOnCellViewBindedListener { cellView, model ->

        }
    }
}
