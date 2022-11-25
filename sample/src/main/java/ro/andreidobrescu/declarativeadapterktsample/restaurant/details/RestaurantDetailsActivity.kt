package ro.andreidobrescu.declarativeadapterktsample.restaurant.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.michaelflisar.bundlebuilder.Arg
import com.michaelflisar.bundlebuilder.BundleBuilder
import ro.andreidobrescu.declarativeadapterkt.sticky_headers.DeclarativeAdapterWithStickyHeaders
import ro.andreidobrescu.declarativeadapterktsample.model.*
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells.CommentCellView
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells.CommentsHeaderCellView
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells.ReceipeCellView
import ro.andreidobrescu.declarativeadapterktsample.restaurant.details.cells.YourCommentCellView
import ro.andreidobrescu.declarativeadapterktsample.restaurant.list.RestaurantCellView
import ro.andreidobrescu.declarativeadapterktsample.R
import ro.andreidobrescu.declarativeadapterktsample.databinding.ActivityRestaurantDetailsBinding
import ro.andreidobrescu.viewbinding_compat.AutoViewBinding
import ro.andreidobrescu.viewbinding_compat.ReflectiveViewBindingFieldSetter

@BundleBuilder
class RestaurantDetailsActivity : AppCompatActivity()
{
    @Arg
    lateinit var restaurant : Restaurant

    @AutoViewBinding
    lateinit var binding : ActivityRestaurantDetailsBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)
        RestaurantDetailsActivityBundleBuilder.inject(intent.extras, this)
        ReflectiveViewBindingFieldSetter.setup(this)
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.layoutManager=LinearLayoutManager(this)

        val adapter=DeclarativeAdapterWithStickyHeaders(binding.recyclerView)

        adapter.whenInstanceOf(Restaurant::class.java,
                    use = { RestaurantCellView(it) })
                .whenInstanceOf(Receipe::class.java,
                    use = { ReceipeCellView(it) })
                .whenInstanceOf(CommentsStickyHeader::class.java,
                    use = { CommentsHeaderCellView(it) })
                .whenInstanceOf(Comment::class.java,
                    and = { comment -> comment.createdBy==User.loggedInUserId },
                    use = { context ->
                        YourCommentCellView(context,
                            onDeleteListener = { comment ->
                                adapter.items.remove(comment)
                                adapter.notifyDataSetChanged()
                            })
                    })
                .whenInstanceOf(Comment::class.java,
                    and = { comment -> comment.createdBy!=User.loggedInUserId },
                    use = { context -> CommentCellView(context) })

        val restaurantDetails=provideRestaurantDetails()
        val items=mutableListOf<Any>()
        items.add(restaurantDetails.restaurant)
        items.addAll(restaurantDetails.receipes)

        if (restaurantDetails.comments.isNotEmpty())
        {
            items.add(CommentsStickyHeader())
            items.addAll(restaurantDetails.comments)
        }

        binding.recyclerView.adapter=adapter
        adapter.setItems(items)
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