package ro.andreidobrescu.viewbinding_compat

import java.lang.reflect.Field

fun Class<*>.getAllFields() : List<Field>
{
    if (isSdkClass())
        return listOf()

    val fields = declaredFields.toMutableList()
    if (superclass!=null)
        fields.addAll(superclass!!.getAllFields())
    return fields
}

fun Class<*>.isSdkClass() : Boolean
{
    return name.startsWith("java.") || 
            name.startsWith("javax.") || 
            name.startsWith("android.") || 
            name.startsWith("androidx.")
}
