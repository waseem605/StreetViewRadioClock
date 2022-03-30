package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.graphics.Color
import com.liveearth.streetview.navigation.map.worldradio.globe.GeneralGlobeLiveEarthMapFmActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import gov.nasa.worldwind.layer.RenderableLayer
import android.os.Bundle
import com.liveearth.streetview.navigation.map.worldradio.R
import gov.nasa.worldwind.render.Renderable
import gov.nasa.worldwind.WorldWind
import android.graphics.Typeface
import android.os.AsyncTask
import android.util.Log
import gov.nasa.worldwind.geom.Offset
import gov.nasa.worldwind.util.WWUtil
import gov.nasa.worldwind.BasicWorldWindowController
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.jean.jcplayer.model.JcAudio
import com.example.jean.jcplayer.view.JcPlayerView
import com.liveearth.streetview.navigation.map.worldradio.globe.OneCountryChanelDialogs
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.FmLiveEarthMapFmInterface
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.RetrofitFMLiveEarthMapFm
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPostionCallBack
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.geom.Position
import gov.nasa.worldwind.shape.*
import gov.nasa.worldwind.util.Logger
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class PathsPolygonsLabelsLiveEarthMapFmActivity : GeneralGlobeLiveEarthMapFmActivity() {
    // A component for displaying the status of this activity
    protected var statusText: TextView? = null
    var TAG = "worldRadio"
    protected var country_name: TextView? = null
    protected var more: Button? = null
    protected var Info: Button? = null
    protected var progressBarCard: CardView? = null
    protected var infoLayout: ConstraintLayout? = null
    protected var radioLayout: ConstraintLayout? = null

    var oneCountriesFMModel = ArrayList<MainOneCountryFMModel>()
    var jcplayer:JcPlayerView?=null
    protected var countryNameRadio: TextView? = null
    var chanelFlag:ImageView?=null
    var countryFlag:ImageView?=null
    var selectChanel:CardView?=null
    var playlist: ArrayList<JcAudio> = ArrayList()
    private var oneCountryChanelDialogs: OneCountryChanelDialogs? = null
    var message = ""
    protected var shapesLayer = RenderableLayer("Shapes")
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
        country_name = findViewById(R.id.country_name)
        countryNameRadio = findViewById<TextView>(R.id.countryNameRadio)
        more = findViewById(R.id.more)
        Info = findViewById(R.id.Info)
        chanelFlag = findViewById<ImageView>(R.id.chanelFlag)
        jcplayer = findViewById<JcPlayerView>(R.id.jcplayer)
        selectChanel = findViewById<CardView>(R.id.selectChanel)
        countryFlag = findViewById<ImageView>(R.id.countryFlag)
        radioLayout = findViewById<ConstraintLayout>(R.id.radioLayout)
        infoLayout = findViewById<ConstraintLayout>(R.id.infoLayout)
        progressBarCard = findViewById(R.id.progressBarCard)

        statusText = TextView(this)
        statusText !!.setTextColor(Color.YELLOW)
        val globeLayout = findViewById<View>(R.id.globe) as FrameLayout
        globeLayout.addView(statusText)

        // Override the WorldWindow's built-in navigation behavior by adding picking support.
        this.worldWindow.worldWindowController = PickController()
        this.worldWindow.layers.addLayer(shapesLayer)
        statusText !!.text = "Loading countries...."
        CreateRenderablesTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    protected inner class CreateRenderablesTask : AsyncTask<Void?, Renderable?, Void?>() {
        private var numCountriesCreated = 0
        private var numHighwaysCreated = 0
        private var numPlacesCreated = 0
        private val random = Random(22)

        override fun doInBackground(vararg params: Void?): Void? {
            loadCountriesFile()
//            loadHighways();
//            loadPlaceNames();
            return null
        }
   /*     override fun doInBackground(vararg notUsed: Void): Void? {
            loadCountriesFile()
            // loadHighways();
            // loadPlaceNames();
            return null
        }*/

        override fun onProgressUpdate(vararg values: Renderable?) {
            super.onProgressUpdate(*values)
            val shape = values[0]
            statusText !!.text = "Added " + shape!!.displayName + " feature..."
            shapesLayer.addRenderable(shape)
            worldWindow.requestRedraw()
        }

        override fun onPostExecute(notUsed: Void?) {
            super.onPostExecute(notUsed)
            statusText !!.text = ""
            progressBarCard !!.visibility = View.GONE
            val camera = Camera()
            camera[37.0, 70.0, 20000000.0, WorldWind.ABSOLUTE, 0.0, 0.0] = 0.0 // No roll

            val globe = wwd.globe
            wwd.navigator.setAsCamera(globe, camera)

            worldWindow.requestRedraw()
        }

        private fun loadPlaceNames() {
            val placeAttrs =
                TextAttributes().setTypeface(Typeface.DEFAULT_BOLD) // Override the normal Typeface
                    .setTextSize(28f) // default size is 24
                    .setTextOffset(Offset.bottomRight()) // anchor the label's bottom-right corner at its position

            // Define the text attribute used for lakes
            val lakeAttrs =
                TextAttributes().setTypeface(Typeface.create("serif", Typeface.BOLD_ITALIC))
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

                    publishProgress(label)
                    numPlacesCreated ++
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

            val highlightAttrs = ShapeAttributes()
            highlightAttrs.outlineColor[1.0f, 0.0f, 0.0f] = 1.0f
            highlightAttrs.outlineWidth = 7f

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

                    publishProgress(path)
                    numHighwaysCreated ++
                }
            } catch (e: IOException) {
                Logger.log(Logger.ERROR, "Exception attempting to read/parse world_highways file.")
            } finally {
                WWUtil.closeSilently(reader)
            }
        }


        private fun loadCountriesFile() {
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

                        polyStart = feature.indexOf("(", polyEnd)
                    }

                    publishProgress(polygon)
                    numCountriesCreated ++
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

        override fun onTouchEvent(event: MotionEvent): Boolean {
            var consumed = super.onTouchEvent(event)
            if (! consumed) {
                consumed = pickGestureDetector.onTouchEvent(event)
            }
            return consumed
        }

        fun pick(event: MotionEvent) {
            val PICK_REGION_SIZE = 40 // pixels

            togglePickedObjectHighlights()
            pickedObjects.clear()

            // Perform a new pick at the screen x, y
            val pickList = worldWindow.pickShapesInRect(
                event.x - PICK_REGION_SIZE / 2,
                event.y - PICK_REGION_SIZE / 2,
                PICK_REGION_SIZE.toFloat(),
                PICK_REGION_SIZE.toFloat()
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
                    highlightable.isHighlighted = ! highlightable.isHighlighted
                    if (highlightable.isHighlighted) {
                        message = (highlightable as Renderable).displayName
                    }
                }
            }
            if (! message.isEmpty()) {
                country_name !!.text = message
                more !!.visibility = View.VISIBLE
                infoLayout !!.visibility = View.GONE
                radioLayout !!.visibility = View.VISIBLE
                Log.d("454545454545","===================++++++++++++++"+message)
                playRadioStreetView(message)

         /*       val dialog = RadioLiveEarthMapFmPlayerDialog(
                    this@PathsPolygonsLabelsLiveEarthMapFmActivity,
                    message
                )
                dialog.show()*/
            } else {
                more !!.visibility = View.GONE
                Info !!.visibility = View.GONE
                infoLayout !!.visibility = View.VISIBLE
                radioLayout !!.visibility = View.GONE
                country_name !!.text = "Click on Globe to Select the Country"
            }
            this.worldWindow.requestRedraw()
        }
    }

    private fun playRadioStreetView(message: String) {
        val dropDownTop:ImageView = findViewById<ImageView>(R.id.dropDownTop)
        dropDownTop.setOnClickListener {
            infoLayout !!.visibility = View.VISIBLE
            radioLayout !!.visibility = View.GONE
        }

        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString,message)

        selectChanel!!.setOnClickListener {
            chanelSelection()
        }
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
        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objinside = JSONObject(newsArray.get(i).toString())
            val name = objinside.getString("name")
            val iso = objinside.getString("iso")
            try {
                if (name.equals(mCountryName)){
                    val flage="https://flagpedia.net/data/flags/normal/${iso}.png"
                    Glide.with(this).load(flage).into(countryFlag!!)
                    countryNameRadio!!.text = name
                    break
                }
                //allCountryFMList.add(AllCountryFMLiveEarthMapFmModel(name, iso))
            } catch (e: Exception) {
            }
        }

        getAllCountryFMListFromApi(mCountryName)
    }

    private fun chanelSelection() {
        if (oneCountriesFMModel.size>0){
            oneCountryChanelDialogs= OneCountryChanelDialogs(this,oneCountriesFMModel,object :
                ChanelPostionCallBack {
                override fun onChanelClick(flage: String, nameCh: String, pos: Int) {
                    oneCountryChanelDialogs!!.dismiss()
                    if (flage == ""){
                        Glide.with(this@PathsPolygonsLabelsLiveEarthMapFmActivity).load(R.drawable.fm_navigation).into(chanelFlag!!)
                    }else{
                        Glide.with(this@PathsPolygonsLabelsLiveEarthMapFmActivity).load(flage).into(chanelFlag!!)
                    }
                    if (jcplayer!!.isPlaying) {
                        jcplayer!!.pause()
                        //Log.i("jcplayerzzz",": "+binding.jcplayer.currentAudio)
                    }
                    try {
                        try {
                            if (playlist[pos].path!=null && playlist[pos].path!="") {
                                jcplayer!!.playAudio(playlist[pos])
                            }
                        } catch (e: Exception) {
                        }
                        jcplayer!!.createNotification(R.drawable.fm_navigation)
                    } catch (e: Exception) {
                    }
                }
            })
            oneCountryChanelDialogs!!.show()
        }
    }

    private fun getAllCountryFMListFromApi(mCountryName: String) {
        //binding.progressBarPlayerCenter.visibility=View.VISIBLE
        val retrofitFM = RetrofitFMLiveEarthMapFm.getRetrofitFM(this)
        val apiInterface = retrofitFM.create(FmLiveEarthMapFmInterface::class.java)
        val callToApi = apiInterface.getOneCountryFM(mCountryName)
        callToApi.enqueue(object : Callback<List<MainOneCountryFMModel>> {
            override fun onFailure(call: Call<List<MainOneCountryFMModel>>, t: Throwable) {
                //binding.progressBarPlayerCenter.visibility=View.GONE
                selectChanel!!.visibility=View.GONE
                Toast.makeText(this@PathsPolygonsLabelsLiveEarthMapFmActivity, "Cannot get now.\nPlease Try again later.", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<List<MainOneCountryFMModel>>,
                response: Response<List<MainOneCountryFMModel>>
            ) {
                if (response.isSuccessful) {
                    oneCountriesFMModel.clear()
                    oneCountriesFMModel = response.body() as ArrayList
                    if (oneCountriesFMModel != null) {
                       // binding.progressBarPlayerCenter.visibility=View.GONE
                        selectChanel!!.visibility=View.VISIBLE
                        settingListofStation(oneCountriesFMModel)
                    }
                } else {
                    Toast.makeText(this@PathsPolygonsLabelsLiveEarthMapFmActivity, "Cannot get  now.\nPlease Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun settingListofStation(oneCountriesFMModel: ArrayList<MainOneCountryFMModel>) {
        if (oneCountriesFMModel.size>0) {
            if (oneCountriesFMModel[0].favicon == "") {
                Glide.with(this).load(R.drawable.fm_navigation).into(chanelFlag!!)
            } else {
                Glide.with(this).load(oneCountriesFMModel[0].favicon).into(chanelFlag!!)
            }

            if (jcplayer!!.isPlaying) {
                jcplayer!!.pause()
                Log.i("jcplayerzzz", ": " + jcplayer!!.currentAudio)
            }

            playlist.clear()
            if (oneCountriesFMModel.size > 0) {
                for (x in 0..oneCountriesFMModel.size - 1) {
                    playlist.add(JcAudio.createFromURL(oneCountriesFMModel[x].name, oneCountriesFMModel[x].urlResolved))
                }
            }
            try {
                if (playlist[0].path!=null && playlist[0].path!="") {
                    jcplayer!!.playAudio(playlist[0])
                }
            } catch (e: Exception) {
            }
            jcplayer!!.initPlaylist(playlist)
            jcplayer!!.createNotification(R.drawable.fm_navigation)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}