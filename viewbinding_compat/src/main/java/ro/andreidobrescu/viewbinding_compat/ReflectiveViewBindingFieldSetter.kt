package ro.andreidobrescu.viewbinding_compat

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import java.lang.reflect.Field
import java.lang.reflect.Method

//ButterKnife.bind(activity) -> ReflectiveViewBindingFieldSetter.setup(activity)
//ButterKnife.bind(view) -> ReflectiveViewBindingFieldSetter.setup(view)
//ButterKnife.bind(target, activity) -> ReflectiveViewBindingFieldSetter.setup(target, activity)
//ButterKnife.bind(target, view) -> ReflectiveViewBindingFieldSetter.setup(target, view)
object ReflectiveViewBindingFieldSetter
{
    fun setup(activity : AppCompatActivity)
    {
        setup(target = activity, view = activity.getContentView())
    }

    fun setup(view : View)
    {
        setup(target = view, view = view)
    }

    fun setup(target : Any, activity : AppCompatActivity)
    {
        setup(target = target, view = activity.getContentView())
    }

    fun setup(target : Any, view : View)
    {
        val viewToBind=
            if (view is CellView<*>)
                view.getChildAt(0)
            else view

        Implementation.get(target, viewToBind)
                .setup(target, viewToBind)
    }

    private class Implementation
    (
        private val autoBindings : List<AutoBinding>
    )
    {
        private class AutoBinding
        (
            private val viewBindingField : Field,
            private val viewBindingFactory : Method
        )
        {
            fun setup(target : Any, view : View)
            {
                val viewBinding=viewBindingFactory.invoke(null, view)

                viewBindingField.set(target, viewBinding)

                view.getActivity().lifecycle.addObserver(object : LifecycleEventObserver
                {
                    override fun onStateChanged(source : LifecycleOwner, event : Lifecycle.Event)
                    {
                        if (event==Lifecycle.Event.ON_DESTROY)
                            viewBindingField.set(target, null)
                    }
                })
            }
        }

        fun setup(target : Any, view : View)
        {
            for (autoBinding in autoBindings)
                autoBinding.setup(target, view)
        }

        companion object
        {
            @JvmStatic
            private val CACHE_SIZE = 100

            @JvmStatic
            private val cache = mutableMapOf<Class<*>, Implementation>()

            @JvmStatic
            fun get(target : Any, view : View) : Implementation
            {
                if (cache.size>=CACHE_SIZE)
                    cache.clear()

                val targetClass=target::class.java
                if (cache.containsKey(targetClass))
                    return cache[targetClass]!!

                val autoBindings=mutableListOf<AutoBinding>()

                val viewBindingFields=targetClass.getAllFields()
                    .filter { field ->
                        field.annotations.find { fieldAnnotation ->
                            fieldAnnotation is AutoViewBinding
                        }!=null
                    }

                for (viewBindingField in viewBindingFields)
                {
                    viewBindingField.isAccessible=true

                    val viewBindingFactory=viewBindingField.type
                        .declaredMethods.find { method ->
                            method.name=="bind"&&method.parameterTypes.size==1&&
                            method.parameterTypes.first()==View::class.java
                        }!!

                    autoBindings.add(AutoBinding(
                        viewBindingField = viewBindingField,
                        viewBindingFactory = viewBindingFactory))
                }

                val implementation=Implementation(autoBindings)
                cache[targetClass]=implementation
                return implementation
            }
        }
    }
}
