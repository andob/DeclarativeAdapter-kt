package ro.andreidobrescu.viewbinding_compat

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import java.lang.reflect.Field
import java.lang.reflect.Modifier

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
        val viewToBind = 
            if (view is CellView<*>)
                view.getChildAt(0)
            else view

        val viewBindingFields = target::class.java.getAllFields()
            .filter { field -> field.annotations.find { it is AutoViewBinding }!=null }

        for (viewBindingField in viewBindingFields)
        {
            bind(target, viewBindingField, viewToBind)

            val alternativeBindingType = viewBindingField.getAnnotation(AutoViewBinding::class.java)
                ?.alternative?.java?.let { alt -> if (alt!=Void::class.java) alt else null }
            if (alternativeBindingType!=null)
                checkAlternativeBinding(viewBindingField.type, alternativeBindingType)
        }
    }

    private fun bind(target : Any, viewBindingField : Field, view : View)
    {
        viewBindingField.isAccessible = true

        val viewClass = View::class.java
        val viewBindingFactory = 
            viewBindingField.type.declaredMethods.find { method ->
                method.name=="bind" && method.parameterTypes.size==1 && 
                method.parameterTypes.first()==viewClass
            }?.let { bindMethod ->
                { view : View -> bindMethod.invoke(null, view)!! }
            }
                ?:viewBindingField.type.declaredConstructors.find { constructor ->
                    constructor.parameterTypes.size==1 && 
                    constructor.parameterTypes.first()==viewClass
                }?.let { constructor ->
                    { view : View -> constructor.newInstance(view)!! }
                }!!

        val viewBinding = viewBindingFactory.invoke(view)

        viewBindingField.set(target, viewBinding)

        //can be inside an async layout inflater
        Handler(Looper.getMainLooper()).post {
            view.getActivity().lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source : LifecycleOwner, event : Lifecycle.Event) {
                    if (event==Lifecycle.Event.ON_DESTROY)
                        viewBindingField.set(target, null)
                }
            })
        }
    }

    private fun checkAlternativeBinding(mainBindingClass : Class<*>, alternateBindingClass : Class<*>)
    {
        val mainBindingFields = mainBindingClass.declaredFields
            .filter { Modifier.isFinal(it.modifiers)&& Modifier.isPublic(it.modifiers) }

        val alternateBindingFields = alternateBindingClass.declaredFields
            .filter { Modifier.isFinal(it.modifiers)&& Modifier.isPublic(it.modifiers) }

        val missingFields = mainBindingFields.filter { mainBindingField ->
            alternateBindingFields.find { alternateBindingField ->
                mainBindingField.name==alternateBindingField.name
            }==null
        }

        if (missingFields.isNotEmpty())
        {
            throw RuntimeException(
                "Conflicting layouts: ${mainBindingClass.name} / ${alternateBindingClass.name}"
                +"\nThere are missing fields in the alternate layout: ${alternateBindingClass.name}\n"
                +missingFields.joinToString(separator = "\n"))
        }

        val conflictingFields = mainBindingFields.map { mainBindingField ->
            mainBindingField to alternateBindingFields.find { alternateBindingField ->
                mainBindingField.name==alternateBindingField.name
                && mainBindingField.type!=alternateBindingField.type
            }
        }.filter { (_, alt) -> alt!=null }

        if (conflictingFields.isNotEmpty())
        {
            throw RuntimeException(
                "Conflicting layouts: ${mainBindingClass.name} / ${alternateBindingClass.name}\n"+
                conflictingFields.map { (mainBindingField, alternateBindingField) ->
                    "${alternateBindingClass.name}.${alternateBindingField?.name} has type "+
                    "${alternateBindingField?.type} but it should be ${mainBindingField.type}!"
                }.joinToString(separator = "\n"))
        }
    }
}
