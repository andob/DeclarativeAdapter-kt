package ro.andreidobrescu.declarativeadapterkt.restaurant.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.michaelflisar.bundlebuilder.Arg
import com.michaelflisar.bundlebuilder.BundleBuilder
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import ro.andreidobrescu.declarativeadapterkt.BaseDeclarativeAdapter
import ro.andreidobrescu.declarativeadapterkt.DeclarativeAdapter
import ro.andreidobrescu.declarativeadapterkt.model.*
import ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells.CommentCellView
import ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells.CommentsHeaderCellView
import ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells.ReceipeCellView
import ro.andreidobrescu.declarativeadapterkt.restaurant.details.cells.YourCommentCellView
import ro.andreidobrescu.declarativeadapterkt.restaurant.list.RestaurantCellView
import ro.andreidobrescu.declarativeadapterktsample.R

@BundleBuilder
class RestaurantDetailsActivity : AppCompatActivity()
{
    @Arg
    lateinit var restaurant : Restaurant

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        RestaurantDetailsActivityBundleBuilder.inject(intent.extras, this)
        setSupportActionBar(toolbar)
        
        recyclerView.layoutManager=LinearLayoutManager(this)
        
        val adapter=DeclarativeAdapter()
        
        adapter.whenInstanceOf(Restaurant::class,
                    use = { RestaurantCellView(it) })
                .whenInstanceOf(Receipe::class,
                    use = { ReceipeCellView(it) })
                .whenInstanceOf(CommentsHeader::class,
                    use = { CommentsHeaderCellView(it) })
                .whenInstanceOf(Comment::class,
                    and = { index, comment -> comment.createdBy==User.loggedInUserId },
                    use = { YourCommentCellView(it, onDeleteListener = { deleteComment(it) }) })
                .whenInstanceOf(Comment::class,
                    and = { index, comment -> comment.createdBy!=User.loggedInUserId },
                    use = { CommentCellView(it) })
        
        val restaurantDetails=provideRestaurantDetails()
        val items=mutableListOf<Any>()
        items.add(restaurantDetails.restaurant)
        items.addAll(restaurantDetails.receipes)
        
        if (restaurantDetails.comments.isNotEmpty())
        {
            items.add(CommentsHeader())
            items.addAll(restaurantDetails.comments)
        }
        
        recyclerView.adapter=adapter
        adapter.setItems(items)
    }

    private fun deleteComment(comment : Comment)
    {
        val adapter=recyclerView.adapter as BaseDeclarativeAdapter
        adapter.items.remove(comment)
        adapter.notifyDataSetChanged()
    }

    private fun provideRestaurantDetails() : RestaurantDetails = RestaurantDetails(
        restaurant = restaurant,
        receipes = listOf(
            Receipe(
                name = "Cabage rolls",
                image = "https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2F1.bp.blogspot.com%2F-EgckJIc_93k%2FVSGWy5_u87I%2FAAAAAAAAJh4%2FzfEKqiqN_iM%2Fs1600%2FCabbage%252BRolls-001-2.jpg&f=1"
            ),
            Receipe(
                name = "Pizza",
                image = "https://proxy.duckduckgo.com/iu/?u=http%3A%2F%2Fincrediblemos.com%2Fwp-content%2Fuploads%2F2013%2F01%2Fpizza-slice.jpg&f=1"
            ),
            Receipe(
                name = "Pasta",
                image = "https://proxy.duckduckgo.com/iur/?f=1&image_host=http%3A%2F%2Fwww.seriouseats.com%2Frecipes%2Fassets_c%2F2016%2F08%2F20160827-cherry-tomato-pasta-13-thumb-1500xauto-433876.jpg&u=https://www.seriouseats.com/recipes/assets_c/2016/08/20160827-cherry-tomato-pasta-13-thumb-1500xauto-433876.jpg"
            )
        ),
        comments = listOf(
            Comment(
                message = "Great!",
                author = "Andrei",
                createdAt = "20.10.2018 14:30",
                createdBy = User.loggedInUserId
            ),
            Comment(
                message = "Comment 2",
                author = "Another user",
                createdAt = "20.10.2018 14:20",
                createdBy = 2
            ),
            Comment(
                message = "Comment 3",
                author = "Another user",
                createdAt = "20.10.2018 14:10",
                createdBy = 2
            )
        )
    )
}