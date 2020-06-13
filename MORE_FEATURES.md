# More features:

### Global cell view inflated event listener

The application class can subscribe to a global cell view inflated event. This event is notified each time a cell view is inflated (after the inflation). This can be useful if you want to integrate boilerplate-removal libraries such as ButterKnife or ViewBinding.

```kotlin
class App : Application()
{
    override fun onCreate()
    {
        super.onCreate()

        CellView.onCellViewInflatedListener=OnCellViewInflatedListener { cellView ->
            ButterKnife.bind(cellView, cellView)
        }
    }
}
```

[!!! For ViewBinding integration tutorial, please check out the "ViewBinding compatibility module" section below.](#viewbinding)

### Context-scoped cell view binded event listener

The current activity context can subscribe to cell view bind events. These events are notified each time a cell view is binded to a model (after the bind). To subscribe to these events, all you have to do is let your activity implement the ``OnCellViewBindedListener`` interface.

Example: how can this be useful in a real project? Consider the following use case:

```kotlin
class CarCellView
(
    context : Context,
    val isPreviewModeEnabled : Boolean
) : CellView<Car>(context)
{
    override fun layout() = R.layout.cell_car
    
    @ModelBinder
    fun setCar(car : Car)
    {
        //lots of logic
        
        if (isPreviewModeEnabled)
            editButton.visibility=View.GONE
        else editButton.visibility=View.VISIBLE
    }
}
```

```kotlin
class PreviewCarsListActivity : BaseListActivity<Car>()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        recyclerView.adapter=SimpleDeclarativeAdapter { context -> 
            CarCellView(context, isPreviewModeEnabled = true)
        }
    }
}
```

```kotlin
class AdminCarsListActivity : BaseListActivity<Car>()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        recyclerView.adapter=SimpleDeclarativeAdapter { context -> 
            CarCellView(context, isPreviewModeEnabled = false)
        }
    }
}
```

So, there are lots of logic inside ``CarCellView`` and splitting it into two classes, ``PreviewCarCellView`` and ``EditableCarCellView`` would mean lots of code duplication. A solution would be to pass a ``isPreviewModeEnabled`` flag. But this flag adds verbosity and noise. A better solution would be to use Aspect Oriented Programming principles: on ``PreviewCarsListActivity`` implement the ``OnCellViewBindedListener`` and hide the edit button in the listener method. Thus, the listener method will behave like an aspect targeting the cell view:

```kotlin
class CarCellView : CellView<Car>
{
    constructor(context : Context) : super(context)
    
    override fun layout() = R.layout.cell_car
    
    @ModelBinder
    fun setCar(car : Car)
    {
        //lots of logic
        //editButton is visible by default
    }
}
```

```kotlin
class PreviewCarsListActivity : BaseListActivity<Car>(), OnCellViewBindedListener
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        recyclerView.adapter=SimpleDeclarativeAdapter { CarCellView(it) }
    }
    
    override fun onCellViewBindedToModel(cellView : CellView<*>, model : Any?)
    {
        if (cellView is CarCellView&&model is Car)
            cellView.editButton.visibility=View.GONE
    }
}
```

```kotlin
class AdminCarsListActivity : BaseListActivity<Car>()
{
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        recyclerView.adapter=SimpleDeclarativeAdapter { CarCellView(it) }
    }
}
```

### 100% Java friendly API

Since I use this library in projects with huge Java codebases, the library API is Java-friendly. For instance:

```kotlin
val adapter=DeclarativeAdapter()
       .whenInstanceOf(Restaurant::class.java,
           and = { index, restaurant -> index==0 },
           use = { RestaurantCellView(it) })
       .whenInstanceOf(Receipe::class.java,
           use = { ReceipeCellView(it) })
```

This looks nice in Kotlin, because we have named parameters. On Java, writing this would be very akward:

```java
new DeclarativeAdapter()
       .whenInstanceOf(Restaurant::class.java,
            (index, restaurant) -> index==0,
            (context) -> new RestaurantCellView(context))
       .whenInstanceOf(Receipe::class.java,
            (context) -> new ReceipeCellView(context));
```

On Java, please use the Builders API:

```java
new DeclarativeAdapter()
       .whenInstanceOf(Restaurant::class.java)
       .and((index, restaurant) -> index==0)
       .use(context -> new RestaurantCellView(context))
       .whenInstanceOf(Receipe::class.java)
       .use(context -> new ReceipeCellView(context));
```

Defining a simple declarative adapter on Java:

```java
new SimpleDeclarativeAdapter(context -> new RestaurantCellView(context));
```

### List sticky headers module

This library supports sticky headers (cell views that sticks to the top of the RecyclerView if you scroll the list). This feature is bundled in a separate module:

```
allprojects {
    repositories {
        maven { url 'http://maven.andob.info/repository/open_source' }
    }
}
```
```
dependencies {
    implementation 'ro.andob.declarativeadapter:adapter-kt:1.2.9.5'
    implementation 'ro.andob.declarativeadapter:sticky-headers:1.2.9.5'
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
val adapter=DeclarativeAdapterWithStickyHeaders()
    .whenInstanceOf(YouTubeVideo::class.java,
        use = { YouTubeVideoCellView(it) })
    .whenInstanceOf(CommentsStickyHeader::class.java,
        use = { CommentsStickyHeaderView(it) })
    .whenInstanceOf(Comment::class.java,
        use = { CommentCellView(it) })

val listItems=mutableListOf()
listItems.add(youTubeVideo)
listItems.add(CommentsStickyHeader())
listItems.addAll(youTubeVideoComments)

recyclerView.adapter=adapter        
```

### ViewBinding compatibility module <a name="viewbinding"></a>

If you are using the ``ViewBinding`` library, please import the ViewBinding compatibility module:

```
allprojects {
    repositories {
        maven { url 'http://maven.andob.info/repository/open_source' }
    }
}
```
```
dependencies {
    implementation 'ro.andob.declarativeadapter:adapter-kt:1.2.9.5'
    implementation 'ro.andob.declarativeadapter:viewbinding-compat:1.2.9.5'
}
```

Define your CellViews using the same API (``layout()`` / ``@ModelBinder``). To link the view binding object, use the ``@AutoViewBinding`` annotation:

```kotlin
class RestaurantCellView : CellView<Restaurant>
{
    @AutoViewBinding
    lateinit var binding : CellRestaurantBinding

    constructor(context : Context?) : super(context)

    override fun layout() : Int = R.layout.cell_restaurant

    @ModelBinder
    fun setRestaurant(restaurant : Restaurant)
    {
        binding.nameLabel.text=restaurant.name
        binding.locationLabel.text=restaurant.location
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

        CellView.onCellViewInflatedListener=OnCellViewInflatedListener { cellView ->
            ReflectiveViewBindingFieldSetter.setup(cellView)
        }
    }
}
```

``ReflectiveViewBindingFieldSetter`` is a class that finds the fields annotated with ``AutoViewBinding`` inside an object and instantiates and assigns the view binding object to the field. Its API is similar to ButterKnife's (because ButterKnife is, unfortunately, deprecated, and having a similar API will ease the migration):

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
        binding.someTextView=""
    }
}
```

```kotlin
abstract class BaseFragment : Fragment()
{
    abstract fun layout() : Int
    
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup, savedInstanceState : Bundle) : View
    {
        val view=inflater.inflate(layout(), container, false)
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
        val view=super.onCreateView(inflater, container, savedInstanceState)
        binding.someTextView="";
        return view
    }
}
```

Downside: The ``ReflectiveViewBindingFieldSetter`` class uses reflection magic in order to find and set fields annotated with ``@AutoViewBinding``. It also uses a cache in order to make the minimum reflection calls possible. The method annotated with ``@ModelBinder`` from the objects that inherit ``CellView`` is also invoked via reflection + a cache. If performance is still a problem IN 2020, reflection magic could be migrated to annotation processor / code generation magic. Pull requests are welcomed.