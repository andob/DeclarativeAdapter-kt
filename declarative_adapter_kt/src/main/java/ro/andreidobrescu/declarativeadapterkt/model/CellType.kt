package ro.andreidobrescu.declarativeadapterkt.model

import android.content.Context
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import java.lang.reflect.Method

class CellType<MODEL : Any>
{
    var viewCreator : ((Context) -> (CellView<MODEL>))? = null
    var modelClass : Class<MODEL>? = null
    var extraChecker : ((Int, MODEL) -> (Boolean))? = null
    var viewModelBinderMethod : Method? = null

    fun isModelApplicable(index : Int, item : Any) : Boolean
    {
        try
        {
            if (item::class==modelClass)
            {
                if (extraChecker==null)
                    return true
                return extraChecker!!.invoke(index, item as MODEL)
            }

            return false
        }
        catch (e : Exception)
        {
            return false
        }
    }
}
