package ro.andreidobrescu.viewbinding_compat

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

internal fun AppCompatActivity.getContentView() =
    findViewById<ViewGroup>(android.R.id.content).getChildAt(0)

internal fun View.getActivity() : AppCompatActivity
{
    var lookupContext = context

    do
    {
        if (lookupContext is AppCompatActivity)
            return lookupContext

        if (lookupContext is android.view.ContextThemeWrapper
            && lookupContext.baseContext!=null)
            lookupContext = lookupContext.baseContext

        else if (lookupContext is androidx.appcompat.view.ContextThemeWrapper
                && lookupContext.baseContext!=null)
            lookupContext = lookupContext.baseContext
    }
    while (lookupContext !is AppCompatActivity)

    return lookupContext
}
