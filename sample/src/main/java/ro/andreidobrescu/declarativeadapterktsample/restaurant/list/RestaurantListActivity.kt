package ro.andreidobrescu.declarativeadapterktsample.restaurant.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ro.andreidobrescu.declarativeadapterkt.SimpleDeclarativeAdapter
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.ActivityRestaurantListBinding
import ro.andreidobrescu.declarativeadapterktsample.model.Restaurant
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

class RestaurantListActivity : AppCompatActivity()
{
    @AutoViewBinding
    lateinit var binding : ActivityRestaurantListBinding

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        ReflectiveViewBindingFieldSetter.setup(this)
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //by default, if you build the app for debug, the exception gets logged to logcat and as a toast
        //to replace the default exception logger, use this
//        DeclarativeAdapter.exceptionLogger = object : DeclarativeAdapter.ExceptionLogger {
//            override fun <MODEL> log(cellView : CellView<MODEL>, model : MODEL, exception : Throwable)
//            {
//                if (BuildConfig.DEBUG) exception.printStackTrace()
//                LogExceptionToServerRequest(exception).execute()
//            }
//        }

        val adapter = SimpleDeclarativeAdapter { RestaurantCellView(it) }
        binding.recyclerView.adapter = adapter
        adapter.setItems(provideRestaurants())
    }

    private fun provideRestaurants() : List<Restaurant> = listOf(
        Restaurant(
            name = "Caru cu Bere",
            location = "Bucharest",
            rating = 5,
            image = "https://www.alux.com/wp-content/uploads/2014/08/The-Best-Restaurants-In-New-York-City-Le-Bernardin1.jpg"
        ),
        Restaurant(
            name = "Casa Boema",
            location = "Cluj",
            rating = 4,
            image = "https://www.alux.com/wp-content/uploads/2014/08/The-Best-Restaurants-In-New-York-City-Le-Bernardin1.jpg"
        ),
        Restaurant(
            name = "Menza",
            location = "Budapest",
            rating = 3,
            image = "https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.traveloutthere.com%2Ffiles%2Fphoto_gallery%2F457x306%2Fbudapest-liszt-01.jpg&f=1"
        ),
        Restaurant(
            name = "Yakiniku",
            location = "Tokyo",
            rating = 5,
            image = "https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fjapan-tourist-guide.com%2Fhorumon-yakiniku.jpg&f=1"
        ),
        Restaurant(
            name = "Le Bernadin",
            location = "New York",
            rating = 5,
            image = "https://www.alux.com/wp-content/uploads/2014/08/The-Best-Restaurants-In-New-York-City-Le-Bernardin1.jpg"
        )
    )
}
