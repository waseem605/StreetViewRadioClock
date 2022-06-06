package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import com.liveearth.streetview.navigation.map.worldradio.StreetViewGlobe.StreetViewGeneralGlobeRadioActivity
import android.widget.TextView
import androidx.cardview.widget.CardView
import gov.nasa.worldwind.layer.RenderableLayer
import android.os.Bundle
import com.liveearth.streetview.navigation.map.worldradio.R
import android.widget.FrameLayout
import gov.nasa.worldwind.render.Renderable
import gov.nasa.worldwind.WorldWind
import android.graphics.Typeface
import android.os.AsyncTask
import android.util.Log
import android.view.*
import gov.nasa.worldwind.geom.Offset
import gov.nasa.worldwind.util.WWUtil
import gov.nasa.worldwind.BasicWorldWindowController
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.hilt.CountryNameModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.shape.*
import gov.nasa.worldwind.util.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class StreetViewMainGlobeViewActivity : StreetViewGeneralGlobeRadioActivity() {
    // A component for displaying the status of this activity
    protected var statusText: TextView? = null
    protected var country_name: TextView? = null
    protected var textWorldRadio: ConstraintLayout? = null

    // protected Button more;
    protected var progressBarCard: CardView? = null
    var countryFlag: ImageView?=null
    var countryNameTx: TextView?=null
       protected var radioLayout: ConstraintLayout? = null
       protected var mainCard: CardView? = null
       protected var infoLayout: ConstraintLayout? = null
       protected var countryInfoLt: ConstraintLayout? = null
       protected var mBannerAdContainer: ConstraintLayout? = null
       protected var mHeadingBack: ConstraintLayout? = null
       protected var mBannerAdLinear: LinearLayout? = null
    var message = ""
    protected var shapesLayer = RenderableLayer("Shapes")
        private lateinit var mPreferenceManagerClass: PreferenceManagerClass

    @Inject
    @Named("countryNameListJson")
    lateinit var mCountryNameList :ArrayList<CountryNameModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_paths_polygons_labels);
        setAboutBoxTitle("About the " + this.resources.getText(R.string.app_name))
        setAboutBoxText(
            """
    Demonstrates place names rendered as labels, world highways rendered as paths and countries rendered as polygons with random interior colors. 
    
    The name of the object(s) are displayed when picked.
    """.trimIndent()
        )
        mPreferenceManagerClass = PreferenceManagerClass(this)

        country_name = findViewById(R.id.country_name)
        //more=findViewById(R.id.more);
        progressBarCard = findViewById(R.id.progressBarCard)
        radioLayout = findViewById<ConstraintLayout>(R.id.radioLayout)
        countryNameTx = findViewById<TextView>(R.id.country_nameText)
        textWorldRadio = findViewById<ConstraintLayout>(R.id.textWorldRadio)
        infoLayout = findViewById<ConstraintLayout>(R.id.infoLayout)
        countryInfoLt = findViewById<ConstraintLayout>(R.id.countryInfoLt)
         countryFlag = findViewById<ImageView>(R.id.countryFlag)
        mainCard = findViewById<CardView>(R.id.mainCard)

        mBannerAdContainer = findViewById<ConstraintLayout>(R.id.bannerID)
        mHeadingBack = findViewById<ConstraintLayout>(R.id.textWorldRadio)
        mBannerAdLinear = findViewById<LinearLayout>(R.id.adContainer)


        setThemeColor()
        initBannerAd()
        statusText = TextView(this)
        statusText!!.setTextColor(Color.YELLOW)
        val globeLayout = findViewById<View>(R.id.globe) as FrameLayout
        globeLayout.addView(statusText)

        // Override the WorldWindow's built-in navigation behavior by adding picking support.
        this.worldWindow.worldWindowController =
            PickController()
        this.worldWindow.layers.addLayer(shapesLayer)

        // Load the shapes into the renderable layer
        statusText!!.text = "Loading countries...."
        CreateRenderablesTask().execute()
    }

    protected inner class CreateRenderablesTask : AsyncTask<Void?, Renderable?, Void?>() {
        private var numCountriesCreated = 0
        private var numHighwaysCreated = 0
        private var numPlacesCreated = 0
        private val random = Random(22)
         override fun doInBackground(vararg params: Void?): Void? {
             loadCountriesFileData()
            // loadHighways();
            // loadPlaceNames();
            return null
        }

         override fun onProgressUpdate(vararg values: Renderable?) {
            super.onProgressUpdate(*values)
            val shape = values[0]
            statusText!!.text = "Added " + shape!!.displayName + " feature..."
            shapesLayer.addRenderable(shape)
            worldWindow.requestRedraw()
        }

        override fun onPostExecute(notUsed: Void?) {
            super.onPostExecute(notUsed)
            statusText!!.text = ""
            progressBarCard!!.visibility = View.GONE
            val camera = Camera()
            camera[37.0, 70.0, 20000000.0, WorldWind.ABSOLUTE, 0.0, 0.0] = 0.0 // No roll

            // Apply the new camera position
            val globe = wwd.globe
            wwd.navigator.setAsCamera(globe, camera)

            /* statusText.setText(String.format(Locale.US, "%,d places, %,d highways and %,d countries created",
                    this.numPlacesCreated,
                    this.numHighwaysCreated,
                    this.numCountriesCreated));*/worldWindow.requestRedraw()
        }

        private fun loadPlaceNames() {
            // Define the text attributes used for places
            val placeAttrs = TextAttributes()
                .setTypeface(Typeface.DEFAULT_BOLD) // Override the normal Typeface
                .setTextSize(28f) // default size is 24
                .setTextOffset(Offset.bottomRight()) // anchor the label's bottom-right corner at its position
            // Define the text attribute used for lakes
            val lakeAttrs = TextAttributes()
                .setTypeface(Typeface.create("serif", Typeface.BOLD_ITALIC))
                .setTextSize(32f) // default size is 24
                .setTextColor(
                    gov.nasa.worldwind.render.Color(
                        0f,
                        1f,
                        1f,
                        0.70f
                    )
                ) // cyan, with 7% opacity
                .setTextOffset(Offset.center()) // center the label over its position

            // Load the place names
            var reader: BufferedReader? = null
            try {
                val `in` = resources.openRawResource(R.raw.world_placenames)
                reader = BufferedReader(InputStreamReader(`in`))

                // Process the header in the first line of the CSV file ...
                var line = reader.readLine()
                val headers = Arrays.asList(*line.split(",".toRegex()).toTypedArray())
                val LAT = headers.indexOf("LAT")
                val LON = headers.indexOf("LON")
                val NAM = headers.indexOf("PLACE_NAME")

                // ... and process the remaining lines in the CSV
                while (reader.readLine().also { line = it } != null) {
                    val fields = line.split(",".toRegex()).toTypedArray()
                    val label = Label(
                        Position.fromDegrees(fields[LAT].toDouble(), fields[LON].toDouble(), 0.0),
                        fields[NAM],
                        if (fields[NAM].contains("Lake")) lakeAttrs else placeAttrs
                    )
                    label.displayName = label.text
                    // Add the Label object to the RenderableLayer on the UI Thread (see onProgressUpdate)
                    publishProgress(label)
                    numPlacesCreated++
                }
            } catch (e: IOException) {
                Logger.log(
                    Logger.ERROR,
                    "Exception attempting to read/parse world_placenames file."
                )
            } finally {
                WWUtil.closeSilently(reader)
            }
        }

        private fun loadHighways() {
            // Define the normal shape attributes
            val attrs = ShapeAttributes()
            attrs.outlineColor[1.0f, 1.0f, 0.0f] = 1.0f
            attrs.outlineWidth = 3f
            // Define the shape attributes used for highlighted "highways"
            val highlightAttrs = ShapeAttributes()
            highlightAttrs.outlineColor[1.0f, 0.0f, 0.0f] = 1.0f
            highlightAttrs.outlineWidth = 7f

            // Load the highways
            var reader: BufferedReader? = null
            try {
                val `in` = resources.openRawResource(R.raw.world_highways)
                reader = BufferedReader(InputStreamReader(`in`))

                // Process the header in the first line of the CSV file ...
                var line = reader.readLine()
                val headers = Arrays.asList(*line.split(",".toRegex()).toTypedArray())
                val WKT = headers.indexOf("WKT")
                val HWY = headers.indexOf("Highway")

                // ... and process the remaining lines in the CSV
                val WKT_START = "\"LINESTRING ("
                val WKT_END = ")\""
                while (reader.readLine().also { line = it } != null) {
                    // Extract the "well known text"  feature and the attributes
                    // e.g.: "LINESTRING (x.xxx y.yyy,x.xxx y.yyy)",text
                    val featureBegin = line.indexOf(WKT_START) + WKT_START.length
                    val featureEnd = line.indexOf(WKT_END, featureBegin)
                    val feature = line.substring(featureBegin, featureEnd)
                    val attributes = line.substring(featureEnd + WKT_END.length + 1)

                    // Buildup the Path. Coordinate tuples are separated by ",".
                    val positions: MutableList<Position> = ArrayList()
                    val tuples = feature.split(",".toRegex()).toTypedArray()
                    for (i in tuples.indices) {
                        // The XY tuple components a separated by a space
                        val xy = tuples[i].split(" ".toRegex()).toTypedArray()
                        positions.add(Position.fromDegrees(xy[1].toDouble(), xy[0].toDouble(), 0.0))
                    }
                    val path = Path(positions, attrs)
                    path.highlightAttributes = highlightAttrs
                    path.altitudeMode = WorldWind.CLAMP_TO_GROUND
                    path.pathType = WorldWind.LINEAR
                    path.isFollowTerrain =
                        true // essential for preventing long segments from intercepting ellipsoid.
                    path.displayName = attributes

                    // Add the Path object to the RenderableLayer on the UI Thread (see onProgressUpdate)
                    publishProgress(path)
                    numHighwaysCreated++
                }
            } catch (e: IOException) {
                Logger.log(Logger.ERROR, "Exception attempting to read/parse world_highways file.")
            } finally {
                WWUtil.closeSilently(reader)
            }
        }

        private fun loadCountriesFileData() {
            // Define the normal shape attributes
            val commonAttrs = ShapeAttributes()
            commonAttrs.interiorColor[1.0f, 1.0f, 0.0f] = 0.5f
            commonAttrs.outlineColor[0.0f, 0.0f, 0.0f] = 1.0f
            commonAttrs.outlineWidth = 3f

            // Define the shape attributes used for highlighted countries
            val highlightAttrs = ShapeAttributes()
            highlightAttrs.interiorColor[1.0f, 1.0f, 1.0f] = 0.5f
            highlightAttrs.outlineColor[1.0f, 1.0f, 1.0f] = 1.0f
            highlightAttrs.outlineWidth = 5f

            // Load the countries
            var reader: BufferedReader? = null
            try {
                val `in` = resources.openRawResource(R.raw.world_political_boundaries)
                reader = BufferedReader(InputStreamReader(`in`))

                // Process the header in the first line of the CSV file ...
                var line = reader.readLine()
                val headers = Arrays.asList(*line.split(",".toRegex()).toTypedArray())
                val GEOMETRY = headers.indexOf("WKT")
                val NAME = headers.indexOf("COUNTRY_NA")

                // ... and process the remaining lines in the CSV
                val WKT_START = "\"POLYGON ("
                val WKT_END = ")\""
                while (reader.readLine().also { line = it } != null) {
                    // Extract the "well known text" feature and the attributes
                    // e.g.: "POLYGON ((x.xxx y.yyy,x.xxx y.yyy), (x.xxx y.yyy,x.xxx y.yyy))",text,more text,...
                    val featureBegin = line.indexOf(WKT_START) + WKT_START.length
                    val featureEnd = line.indexOf(WKT_END, featureBegin) + WKT_END.length
                    val feature = line.substring(featureBegin, featureEnd)
                    val attributes = line.substring(featureEnd + 1)
                    val fields = attributes.split(",".toRegex()).toTypedArray()
                    val polygon = Polygon()
                    polygon.altitudeMode = WorldWind.CLAMP_TO_GROUND
                    polygon.pathType = WorldWind.LINEAR
                    polygon.isFollowTerrain =
                        true // essential for preventing long segments from intercepting ellipsoid.
                    polygon.displayName = fields[1]
                    polygon.attributes = ShapeAttributes(commonAttrs)
                    polygon.attributes.interiorColor = gov.nasa.worldwind.render.Color(
                        random.nextFloat(),
                        random.nextFloat(),
                        random.nextFloat(),
                        0.3f
                    )
                    polygon.highlightAttributes = highlightAttrs

                    // Process all the polygons within this feature by creating "boundaries" for each.
                    // Individual polygons are bounded by "(" and ")"
                    var polyStart = feature.indexOf("(")
                    while (polyStart >= 0) {
                        val polyEnd = feature.indexOf(")", polyStart)
                        val poly = feature.substring(polyStart + 1, polyEnd)

                        // Buildup the Polygon boundaries. Coordinate tuples are separated by ",".
                        val positions: MutableList<Position> = ArrayList()
                        val tuples = poly.split(",".toRegex()).toTypedArray()
                        for (i in tuples.indices) {
                            // The XY tuple components a separated by a space
                            val xy = tuples[i].split(" ".toRegex()).toTypedArray()
                            positions.add(
                                Position.fromDegrees(
                                    xy[1].toDouble(),
                                    xy[0].toDouble(),
                                    0.0
                                )
                            )
                        }
                        polygon.addBoundary(positions)

                        // Locate the next polygon in the feature
                        polyStart = feature.indexOf("(", polyEnd)
                    }

                    // Add the Polygon object to the RenderableLayer on the UI Thread (see onProgressUpdate).
                    publishProgress(polygon)
                    numCountriesCreated++
                }
            } catch (e: IOException) {
                Logger.log(Logger.ERROR, "Exception attempting to read/parse world_highways file.")
            } finally {
                WWUtil.closeSilently(reader)
            }
        }
    }

    inner class PickController : BasicWorldWindowController() {
        protected var pickedObjects = ArrayList<Any>() // last picked objects from onDown events

        protected var pickGestureDetector =
            GestureDetector(applicationContext, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(event: MotionEvent): Boolean {
                    pick(event) // Pick the object(s) at the tap location
                    return true
                }
            })

        /**
         * Delegates events to the pick handler or the native WorldWind navigation handlers.
         */
        override fun onTouchEvent(event: MotionEvent): Boolean {
            var consumed = super.onTouchEvent(event)
            if (!consumed) {
                consumed = pickGestureDetector.onTouchEvent(event)
            }
            return consumed
        }

        fun pick(event: MotionEvent) {
            val PICK_REGION_SIZE = 40 // pixels

            // Forget our last picked objects
            togglePickedObjectHighlights()
            pickedObjects.clear()

            // Perform a new pick at the screen x, y
            val pickList = worldWindow.pickShapesInRect(
                event.x - PICK_REGION_SIZE / 2,
                event.y - PICK_REGION_SIZE / 2,
                PICK_REGION_SIZE.toFloat(), PICK_REGION_SIZE.toFloat()
            )

            for (i in 0 until pickList.count()) {
                if (pickList.pickedObjectAt(i).isOnTop) {
                    pickedObjects.add(pickList.pickedObjectAt(i).userObject)
                }
            }
            togglePickedObjectHighlights()
        }

        fun togglePickedObjectHighlights() {
            for (pickedObject in pickedObjects) {
                if (pickedObject is Highlightable) {
                    val highlightable = pickedObject
                    highlightable.isHighlighted = !highlightable.isHighlighted
                    if (highlightable.isHighlighted) {
                        /* if (!message.isEmpty()) {
                            message += ", ";
                        }
                        message += ((Renderable) highlightable).getDisplayName();*/
                        message = (highlightable as Renderable).displayName
                    }
                }
            }
            if (!message.isEmpty()) {
                country_name!!.text = message
                // more.setVisibility(View.VISIBLE);
                countryInfoLt!!.visibility = View.VISIBLE
                infoLayout !!.visibility = View.GONE
                selectedCountryNameStreetView(message)

            } else {
                country_name!!.text = "Click on Globe to Select the Country"
            }
            this.worldWindow.requestRedraw()
        }
    }


    private fun selectedCountryNameStreetView(message: String) {
        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString,message)
    }

    private fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = assets.open("all_country_names.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        return inputString
    }

    private fun parseJsonStringToNewsList(jsonString: String, mCountryName: String) {
        var nameCountry:String?=null
        var isoCountry :String?=null
        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objinside = JSONObject(newsArray.get(i).toString())
            val name = objinside.getString("name")
             val iso = objinside.getString("iso")
            try {
                if (name.equals(mCountryName)){
                    Log.d("openViewData", "=============$iso======$name")
                    val flage="https://flagpedia.net/data/flags/normal/${iso}.png"
                    Glide.with(this).load(flage).into(countryFlag!!)
                    nameCountry = name
                    countryNameTx!!.text = name
                    isoCountry = iso
                    break
                }
            } catch (e: Exception) {
            }
        }

        radioLayout!!.setOnClickListener {
            nameCountry.let {
                val intent = Intent(this,StreetViewRadioChannelsActivity::class.java)
                intent.putExtra(ConstantsStreetView.Radio_Country_Name,it)
                intent.putExtra(ConstantsStreetView.Radio_Country_Code,isoCountry)
                startActivity(intent)
            }
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun initBannerAd() {
        LoadAdsStreetViewClock.loadEarthLiveMapBannerAdMob(
            mBannerAdLinear,
            mBannerAdContainer,
            this
        )
    }


    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        mainCard!!.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
        textWorldRadio!!.setBackgroundColor(Color.parseColor(backgroundColor))
        mHeadingBack!!.setBackgroundColor(Color.parseColor(backgroundColor))
        countryInfoLt!!.setBackgroundColor(Color.parseColor(backgroundSecondColor))

    }

}