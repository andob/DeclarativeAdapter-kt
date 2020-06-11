package ro.andreidobrescu.viewbinding_compat

import android.app.Activity
import android.view.View
import android.view.ViewGroup

object ReflectiveViewBindingFieldSetter
{
    fun setup(view : View)
    {
        if (view is ViewGroup)
            setup(target = view, view = view.getChildAt(0))
        else setup(target = view, view = view)
    }

    private fun Activity.getContentView() =
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)

    fun setup(activity : Activity)
    {
        setup(target = activity, view = activity.getContentView())
    }

    fun setup(target : Any, activity : Activity)
    {
        setup(target = target, view = activity.getContentView())
    }

    //setup method cache. target class -> (target, view) -> viewBindingField.set(target, viewBinding)
    private val setupCache = mutableMapOf<Class<*>, (Any, View) -> (Unit)>()

    //similar to ButterKnife.bind(target, view). Goodbye, BK! :(
    fun setup(target : Any, view : View)
    {
        if (setupCache.size==1000)
            setupCache.clear()

        val targetClass=target::class.java
        if (setupCache.containsKey(targetClass))
        {
            val cachedMethod=setupCache[targetClass]!!
            cachedMethod.invoke(target, view)
        }
        else
        {

            val viewBindingFields=targetClass.getAllFields()
                .filter { field ->
                    field.annotations.find { fieldAnnotation ->
                        fieldAnnotation is AutoViewBinding
                    }!=null
                }

            for (viewBindingField in viewBindingFields)
            {
                viewBindingField.isAccessible=true

                val bindingMethod=viewBindingField.type
                    .declaredMethods.find { method ->
                        method.name=="bind"&&method.parameterTypes.size==1&&
                        method.parameterTypes.first()==View::class.java
                    }!!

                setupCache[targetClass]={ target, view ->
                    val viewBinding=bindingMethod.invoke(null, view)

                    viewBindingField.set(target, viewBinding)
                }

                setupCache[targetClass]!!.invoke(target, view)
            }
        }
    }
}
