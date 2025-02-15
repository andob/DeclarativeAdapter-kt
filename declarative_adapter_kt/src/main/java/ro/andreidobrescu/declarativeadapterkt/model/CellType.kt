package ro.andreidobrescu.declarativeadapterkt.model

import android.content.Context
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import java.lang.reflect.Method

class CellType<MODEL : Any>
{
    var viewCreator : ((Context) -> CellView<MODEL>)? = null
    var modelClass : Class<MODEL>? = null
    var extraChecker : ((MODEL) -> Boolean)? = null
    var viewModelBinderMethod : Method? = null

     @Suppress("UNCHECKED_CAST")
     fun isModelApplicable(index : Int, item : Any, classComparer : (Class<*>, Class<*>) -> Boolean) : Boolean
    {
        try
        {
            if (classComparer(item::class.java, modelClass!!))
            {
                if (extraChecker == null)
                    return true
                return extraChecker!!.invoke(item as MODEL)
            }

            return false
        }
        catch (e : Exception)
        {
            return false
        }
    }
}
