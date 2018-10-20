package ro.andreidobrescu.declarativeadapterkt

import android.content.Context
import kotlin.reflect.KClass

class CellType<MODEL : Any>
{
    var viewCreator : ((Context) -> (CellView<MODEL>))? = null
    var typeChecker : ((Int, MODEL) -> (Boolean))? = null
    var modelClass  : KClass<MODEL>? = null

    fun check(index : Int, item : Any)  = try
    {
        if (item::class==modelClass)
        {
            if (typeChecker==null)
                true
            else typeChecker!!.invoke(index, item as MODEL)
        }
        else
        {
            false
        }
    }
    catch (e : Exception)
    {
        false
    }
}
