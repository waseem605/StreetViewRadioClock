package com.liveearth.streetview.navigation.map.worldradio.globe

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.jean.jcplayer.model.JcAudio
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogRadioPlayerBinding
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.FmLiveEarthMapFmInterface
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.RetrofitFMLiveEarthMapFm
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPostionCallBack
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class RadioLiveEarthMapFmPlayerDialog(private var mContext: Context,private val mCountryName:String) :
    Dialog(mContext) {
    var countryNameSeleted:String=mCountryName
    var countryISOSeleted:String="us"
    var oneCountriesFMModel = ArrayList<MainOneCountryFMModel>()
    var playlist: ArrayList<JcAudio> = ArrayList()
    private var sharedPreferences: SharedPreferences? = null
    private var countryLiveEarthMapFmSelectionDialog: CountryLiveEarthMapFmSelectionDialog? = null
    private var oneCountryChanelDialogs: OneCountryChanelDialogs? = null

    private lateinit var binding:DialogRadioPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        binding = DialogRadioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString,mCountryName)
        //sharedPreferences = mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
        //setFlagAndName()

        binding.selectCountry.setOnClickListener {
            countrySelection(mCountryName)
        }
        binding.selectChanel.setOnClickListener {
            chanelSelection()
        }

        setOnCancelListener {
            dismissDialog()
        }
    }

    private fun chanelSelection() {
        if (oneCountriesFMModel.size>0){
            oneCountryChanelDialogs= OneCountryChanelDialogs(mContext,oneCountriesFMModel,object :
                ChanelPostionCallBack {
                override fun onChanelClick(flage: String, nameCh: String, pos: Int) {
                    oneCountryChanelDialogs!!.dismiss()
                    if (flage == ""){
                        Glide.with(mContext).load(R.drawable.fm_navigation).into(binding.chanelFlag)
                    }else{
                        Glide.with(mContext).load(flage).into(binding.chanelFlag)
                    }
                    if (binding.jcplayer.isPlaying) {
                        binding.jcplayer.pause()
                        Log.i("jcplayerzzz",": "+binding.jcplayer.currentAudio)
                    }
                    try {
                        try {
                            if (playlist[pos].path!=null && playlist[pos].path!="") {
                                binding.jcplayer.playAudio(playlist[pos])
                            }
                        } catch (e: Exception) {
                        }
                        binding.jcplayer.createNotification(R.drawable.fm_navigation)
                    } catch (e: Exception) {
                    }
                }
            })
            oneCountryChanelDialogs!!.show()
        }
    }

    private fun countrySelection(mCountryName: String) {


        Log.d("RadioProblem","================================dialog onClick==")
//        countryLiveEarthMapFmSelectionDialog=CountryLiveEarthMapFmSelectionDialog(mContext,object :
//             CountryNameInterface {
//            override fun onCountryName(name: String, iso: String) {
//                try {
//                    Log.d("RadioProblem","================================dialog interface==")
//                    countryLiveEarthMapFmSelectionDialog!!.dismiss()
//                } catch (e: Exception) {
//                }
//
//            }
//        })
//        countryLiveEarthMapFmSelectionDialog!!.show()

    }
    private fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = mContext.assets.open("all_country_names.json")
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
               // if (objinside.getString("name").equals(mCountryName)){
                if (name.equals(mCountryName)){
                    val flage="https://flagpedia.net/data/flags/normal/${iso}.png"
                    Glide.with(mContext).load(flage).into(binding.countryFlag)
                    binding.countryName.text = name
                    break
//                    val editor = sharedPreferences!!.edit()
//                    editor.putString("countryNameSeleted", mCountryName)
//                    editor.putString("countryISOSeleted", iso)
//                    editor.apply()
                    //setFlagAndName()
                }
                //allCountryFMList.add(AllCountryFMLiveEarthMapFmModel(name, iso))
            } catch (e: Exception) {
            }
        }

        getAllCountryFMListFromApi()
    }

    private fun setFlagAndName() {
        countryNameSeleted = sharedPreferences!!.getString("countryNameSeleted", "United States of America")!!
        countryISOSeleted = sharedPreferences!!.getString("countryISOSeleted", "us")!!

        val flage="https://flagpedia.net/data/flags/normal/${countryISOSeleted}.png"
        //Glide.with(mContext).load(flage).into(binding.countryFlag)
        //binding.countryName.text=countryNameSeleted.toString()
        getAllCountryFMListFromApi()
    }

    private fun getAllCountryFMListFromApi() {
        binding.progressBarPlayerCenter.visibility=View.VISIBLE
        val retrofitFM = RetrofitFMLiveEarthMapFm.getRetrofitFM(mContext)
        val apiInterface = retrofitFM.create(FmLiveEarthMapFmInterface::class.java)
        val callToApi = apiInterface.getOneCountryFM(mCountryName)
        callToApi.enqueue(object : Callback<List<MainOneCountryFMModel>> {
            override fun onFailure(call: Call<List<MainOneCountryFMModel>>, t: Throwable) {
                binding.progressBarPlayerCenter.visibility=View.GONE
                binding.selectChanel.visibility=View.GONE
                Toast.makeText(mContext, "Cannot get now.\nPlease Try again later.", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<List<MainOneCountryFMModel>>,
                response: Response<List<MainOneCountryFMModel>>
            ) {
                if (response.isSuccessful) {
                    oneCountriesFMModel.clear()
                    oneCountriesFMModel = response.body() as ArrayList
                    if (oneCountriesFMModel != null) {
                        binding.progressBarPlayerCenter.visibility=View.GONE
                        binding.selectChanel.visibility=View.VISIBLE
                        settingListofStation(oneCountriesFMModel)
                    }
                } else {
                    Toast.makeText(mContext, "Cannot get  now.\nPlease Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun settingListofStation(oneCountriesFMModel: ArrayList<MainOneCountryFMModel>) {
        if (oneCountriesFMModel.size>0) {
            if (oneCountriesFMModel[0].favicon == "") {
                Glide.with(mContext).load(R.drawable.fm_navigation).into(binding.chanelFlag)
            } else {
                Glide.with(mContext).load(oneCountriesFMModel[0].favicon).into(binding.chanelFlag)
            }

            if (binding.jcplayer.isPlaying) {
                binding.jcplayer.pause()
                Log.i("jcplayerzzz", ": " + binding.jcplayer.currentAudio)
            }

            playlist.clear()
            if (oneCountriesFMModel.size > 0) {
                for (x in 0..oneCountriesFMModel.size - 1) {
                    playlist.add(JcAudio.createFromURL(oneCountriesFMModel[x].name, oneCountriesFMModel[x].urlResolved))
                }
            }
            try {
                if (playlist[0].path!=null && playlist[0].path!="") {
                    binding.jcplayer.playAudio(playlist[0])
                }
            } catch (e: Exception) {
            }
            binding.jcplayer.initPlaylist(playlist)
            binding.jcplayer.createNotification(R.drawable.fm_navigation)
        }
    }

    fun dismissDialog() {
        try {
            dismiss()
        } catch (e: Exception) {
        }
    }
}