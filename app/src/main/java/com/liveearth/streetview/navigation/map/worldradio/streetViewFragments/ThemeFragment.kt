package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
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
        setThemeColor()

        binding.toolbarLt.titleTx.text = "Theme"
        binding.toolbarLt.backLink.visibility = View.INVISIBLE

        return binding.root
    }

    private fun themeRecyclerView() {

        val arrayList:ArrayList<ColorModel> = ArrayList()
        arrayList.add(ColorModel("#237157","#CDE6DD"))
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
        arrayList.add(ColorModel("#139274","#A6D5CA"))
        arrayList.add(ColorModel("#234271","#D3DCEB"))
        arrayList.add(ColorModel("#12086E","#BEBAD9"))
        arrayList.add(ColorModel("#500423","#EBC5D4"))
        arrayList.add(ColorModel("#1D7182","#B8D6DC"))
        arrayList.add(ColorModel("#C4293D","#E6B1B8"))
        arrayList.add(ColorModel("#720D8A","#E2C2E9"))
        arrayList.add(ColorModel("#18B59F","#C3E6E2"))
        arrayList.add(ColorModel("#19598B","#ADC8DE"))
        arrayList.add(ColorModel("#D5326F","#D9ADBE"))
        arrayList.add(ColorModel("#ACB61E","#ECEED3"))
        arrayList.add(ColorModel("#C2BD2D","#E5E3BD"))
        arrayList.add(ColorModel("#AF0B84","#E8C4DE"))
        arrayList.add(ColorModel("#2E77CB","#B5C9DF"))
        arrayList.add(ColorModel("#A629C4","#E9BEF3"))
        arrayList.add(ColorModel("#9C0A8D","#D2A5CE"))
        arrayList.add(ColorModel("#1B9098","#C0E4E6"))
        arrayList.add(ColorModel("#DB622D","#EBD3C8"))
        arrayList.add(ColorModel("#B96708","#ECDDCB"))
        arrayList.add(ColorModel("#0AC473","#B2DBC9"))
        arrayList.add(ColorModel("#FCBA03","#EAD69F"))
        arrayList.add(ColorModel("#2C2B2B","#A7A5A5"))


        val mThemeAdapter = GridColorDialogAdapter(arrayList,requireContext(),object :ColorThemeCallBackListener{
            override fun onColorClick(colorModel: ColorModel) {
                mPreferenceManagerClass.putString(ConstantsStreetView.APP_COLOR,colorModel.color)
                mPreferenceManagerClass.putString(ConstantsStreetView.APP_COLOR_Second,colorModel.secondColor)
                ConstantsStreetView.APP_SELECTED_COLOR = colorModel.color
                ConstantsStreetView.APP_SELECTED_SECOND_COLOR = colorModel.secondColor
                Log.d(TAG, "onColorClick: ===========++++++++++++++")
                setThemeColor()
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

    private fun setThemeColor() {
        val backgroundColor =
            mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor =
            mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
    }
}