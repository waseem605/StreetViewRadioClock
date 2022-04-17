package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ColorThemeCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentThemeBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.GridColorDialogAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ColorModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass

class ThemeFragment : Fragment() {
    private lateinit var binding: FragmentThemeBinding
    private val TAG = "ThemeFragment"
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass

    private lateinit var callBack : ColorThemeCallBackListener

    companion object {
        fun newInstance() = ThemeFragment()

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBack = activity as ColorThemeCallBackListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeBinding.inflate(layoutInflater, container, false)

        mPreferenceManagerClass = PreferenceManagerClass(requireContext())
        themeRecyclerView()


        return binding.root
    }

    private fun themeRecyclerView() {

        val arrayList:ArrayList<ColorModel> = ArrayList()
        arrayList.add(ColorModel("#923613","#E8CCC1"))
        arrayList.add(ColorModel("#0D3C8B","#C4D2E9"))
        arrayList.add(ColorModel("#5F2DB5","#CABBE5"))
        arrayList.add(ColorModel("#0B6567","#AADADB"))
        arrayList.add(ColorModel("#756C19","#DCD8B0"))
        arrayList.add(ColorModel("#831D50","#E4CAD7"))
        arrayList.add(ColorModel("#692371","#E3C9E6"))
        arrayList.add(ColorModel("#1904D9","#D2D0E7"))
        arrayList.add(ColorModel("#237157","#CDE6DD"))
        arrayList.add(ColorModel("#A19C00","#DFDDB5"))

        val mThemeAdapter = GridColorDialogAdapter(arrayList,requireContext(),object :ColorThemeCallBackListener{
            override fun onColorClick(colorModel: ColorModel) {
                mPreferenceManagerClass.putString(ConstantsStreetView.APP_COLOR,colorModel.color)
                mPreferenceManagerClass.putString(ConstantsStreetView.APP_COLOR_Second,colorModel.secondColor)
                ConstantsStreetView.APP_SELECTED_COLOR = colorModel.color
                ConstantsStreetView.APP_SELECTED_SECOND_COLOR = colorModel.secondColor
                Log.d(TAG, "onColorClick: ===========++++++++++++++")

                callBack.onColorClick(colorModel)

            }

        })
        binding.colorRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = mThemeAdapter
        }

    }

    fun main(){
        Log.d(TAG, "main: ==main main main main==============")
    }

}