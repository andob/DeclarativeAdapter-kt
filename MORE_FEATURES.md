# More features:

### Global cell view inflated / binded event listeners

The application class can subscribe to global cell view inflated / binded events:

```kotlin
class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        CellViewGlobalEvents.setOnCellViewInflatedListener { cellView ->
            ReflectiveViewBindingFieldSetter.setup(cellView)
        }
        
        CellViewGlobalEvents.setOnCellViewBindedListener { cellView, model -> 
            
        }
    }
}
```

[!!! For ViewBinding integration tutorial, please check out the "ViewBinding compatibility module" section below.](#viewbinding)

### 100% Java friendly API

Since I use this library in projects with huge Java codebases, the library API is Java-friendly. For instance:

```kotlin
val adapter = DeclarativeAdapter()
       .whenInstanceOf(Restaurant::class.java,
           and = { restaurant -> restaurant.name == "wat" },
           use = ::RestaurantCellView)
       .whenInstanceOf(Receipe::class.java, use = ::ReceipeCellView)
```

This looks nice in Kotlin, because we have named parameters. On Java, writing this would be very akward:

```java
new DeclarativeAdapter()
       .whenInstanceOf(Restaurant::class.java,
            (restaurant) -> restaurant.getName().equals("wat"),
            RestaurantCellView::new)
       .whenInstanceOf(Receipe::class.java, ReceipeCellView::new);
```

On Java, please use the Builders API:

```java
new DeclarativeAdapter()
       .whenInstanceOf(Restaurant::class.java)
       .and(restaurant -> restaurant.getName().equals("wat"))
       .use(RestaurantCellView::new)
       .whenInstanceOf(Receipe::class.java)
       .use(ReceipeCellView::new);
```

Defining a simple declarative adapter on Java:

```java
new SimpleDeclarativeAdapter(RestaurantCellView::new);
```

### List sticky headers module

This library supports sticky headers (cell views that sticks to the top of the RecyclerView if you scroll the list). This feature is bundled in a separate module:

```
allprojects {
    repositories {
        maven { url 'https://andob.io/repository/open_source' }
    }
}
```
```
dependencies {
    implementation 'ro.andob.declarativeadapter:adapter-kt:1.3.1.1'
    implementation 'ro.andob.declarativeadapter:sticky-headers:1.3.1.1'
}
```

Example: Model layer:

```kotlin
class YouTubeVideo { ... }
class CommentsStickyHeader : StickyHeaderModel()
class Comment { ... }
```

CellView layer:

```kotlin
class YouTubeVideoCellView : CellView { ... }
class CommentsStickyHeaderView : StickyHeaderView { ... }
class CommentCellView : CellView { ... }
//implement layout() and @ModelBinder methods
//the StickyHeaderView class extends CellView
```

Adapter definition:

```kotlin
val adapter = DeclarativeAdapterWithStickyHeaders()
    .whenInstanceOf(YouTubeVideo::class.java, use = ::YouTubeVideoCellView)
    .whenInstanceOf(CommentsStickyHeader::class.java, use = ::CommentsStickyHeaderView)
    .whenInstanceOf(Comment::class.java, use = ::CommentCellView)

val listItems = mutableListOf()
listItems.add(youTubeVideo)
listItems.add(CommentsStickyHeader())
listItems.addAll(youTubeVideoComments)

recyclerView.adapter = adapter        
```

### ViewBinding compatibility module <a name="viewbinding"></a>

If you are using the ``ViewBinding`` library, please import the ViewBinding compatibility module:

```
allprojects {
    repositories {
        maven { url 'https://andob.io/repository/open_source' }
    }
}
```
```
dependencies {
    implementation 'ro.andob.declarativeadapter:adapter-kt:1.3.1.1'
    implementation 'ro.andob.declarativeadapter:viewbinding-compat:1.3.1.1'
}
```

Define your CellViews using the same API (``layout()`` / ``@ModelBinder``). To link the view binding object, use the ``@AutoViewBinding`` annotation:

```kotlin
class RestaurantCellView : CellView<Restaurant>
{
    @AutoViewBinding
    lateinit var binding : CellRestaurantBinding
    override fun layout() : Int = R.layout.cell_restaurant
    
    constructor(context : Context?) : super(context)

    @ModelBinder
    fun setRestaurant(restaurant : Restaurant)
    {
        binding.nameLabel.text = restaurant.name
        binding.locationLabel.text = restaurant.location
    }
}
```

To configure the module, use this in your application class:

```kotlin
class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        CellViewGlobalEvents.setOnCellViewInflatedListener { cellView ->
            ReflectiveViewBindingFieldSetter.setup(cellView)
        }
    }
}
```

``ReflectiveViewBindingFieldSetter`` is a class that finds the fields annotated with ``AutoViewBinding`` inside an object and instantiates and assigns the view binding object to the field. Its API is similar to ButterKnife's (because ButterKnife is, unfortunately, deprecated, and having a similar API helped me ease the migration):

```kotlin
//from an activity, to initialize @AutoViewBinding fields:
ButterKnife.bind(activity) -> ReflectiveViewBindingFieldSetter.setup(activity)
//from a custom view, to initialize @AutoViewBinding fields:
ButterKnife.bind(view) -> ReflectiveViewBindingFieldSetter.setup(view)
//from an activity, to initialize @AutoViewBinding fields from the target object:
ButterKnife.bind(target, activity) -> ReflectiveViewBindingFieldSetter.setup(target, activity)
//from a view, to initialize @AutoViewBinding fields from the target object:
ButterKnife.bind(target, view) -> ReflectiveViewBindingFieldSetter.setup(target, view)
```

**This class can be used to ease the ButterKnife -> ViewBinding migration of anything, from Activities, Fragments, custom views to cell views defined using this library. For instance:**

```kotlin
abstract class BaseActivity : AppCompatActivity()
{
    abstract fun layout() : Int
    
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(layout())
        ReflectiveViewBindingFieldSetter.setup(this)
        //more logic
    }
    
    //more logic
}
```

```kotlin
class MainActivity : BaseActivity()
{
    @AutoViewBinding
    lateinit var binding : ActivityMainBinding
    
    override fun layout() = R.layout.activity_main
    
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding.someTextView = ""
    }
}
```

```kotlin
abstract class BaseFragment : Fragment()
{
    abstract fun layout() : Int
    
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup, savedInstanceState : Bundle) : View
    {
        val view = inflater.inflate(layout(), container, false)
        ReflectiveViewBindingFieldSetter.setup(this, view)
        //more logic
    }
    
    //more logic
}
```

```kotlin
class MainMenuFragment : BaseFragment()
{
    @AutoViewBinding
    lateinit var binding : FragmentMainMenuBinding
    
    override fun layout() = R.layout.fragment_main_menu
    
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup, savedInstanceState : Bundle) : View
    {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.someTextView = "";
        return view
    }
}
```
