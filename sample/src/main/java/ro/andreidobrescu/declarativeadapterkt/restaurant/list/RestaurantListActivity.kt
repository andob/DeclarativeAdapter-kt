package ro.andreidobrescu.declarativeadapterkt.restaurant.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterkt.SimpleDeclarativeAdapter
import ro.andreidobrescu.declarativeadapterkt.model.Restaurant

class RestaurantListActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
        setSupportActionBar(toolbar)

        recyclerView.layoutManager=LinearLayoutManager(this)

        val adapter=SimpleDeclarativeAdapter { RestaurantCellView(it) }
        recyclerView.adapter=adapter
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
