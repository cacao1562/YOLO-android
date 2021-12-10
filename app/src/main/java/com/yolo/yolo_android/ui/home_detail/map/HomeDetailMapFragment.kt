package com.yolo.yolo_android.ui.home_detail.map

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.yolo.yolo_android.R
import com.yolo.yolo_android.base.BindingFragment
import com.yolo.yolo_android.databinding.FragmentHomeDetailMapBinding
import com.yolo.yolo_android.model.HomeDetailResult
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import android.widget.FrameLayout
import com.yolo.yolo_android.model.Document

@AndroidEntryPoint
class HomeDetailMapFragment: BindingFragment<FragmentHomeDetailMapBinding>(R.layout.fragment_home_detail_map) {

    companion object {

        const val KEY_MAP_CODE = "KEY_MAP_CODE"
        const val KEY_MAP_DATA = "KEY_MAP_DATA"

        fun newInstance(code: String, data: HomeDetailResult): HomeDetailMapFragment {
            val fragment = HomeDetailMapFragment()
            fragment.apply {
                arguments = bundleOf(KEY_MAP_CODE to code, KEY_MAP_DATA to data)
            }
            return fragment
        }
    }

    private val viewModel: HomeDetailMapViewModel by viewModels()

    private var mMapView: MapView? = null

    interface OnMapTouchListener {
        fun onTouch()
    }

    private var mTouchListener: OnMapTouchListener? = null
    fun setTouchListener(listener: OnMapTouchListener) {
        mTouchListener = listener
    }

    private var mCode: String? = ""
    private var mData: HomeDetailResult? = null
    private var markerData: List<Document>? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        mCode = arguments?.getString(KEY_MAP_CODE)
        mData = arguments?.getParcelable<HomeDetailResult>(KEY_MAP_DATA)

        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                markerData = data
                data.forEach {
                    addMarker(it.place_name, it.y.toDouble(), it.x.toDouble())
                }
            }
        }

        (binding.root as ViewGroup).addView(TouchableWrapper(context = requireActivity(), listener = mTouchListener),
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT) )

    }

    override fun onResume() {
        super.onResume()
        mMapView = MapView(activity)
        val v = binding.rootHomeDetailMap as ViewGroup
        v.addView(mMapView)
        mMapView?.setPOIItemEventListener(listener)
        mMapView?.setZoomLevel(4, true)

        if (markerData != null) {
            addCenterPoint()
            markerData?.forEach {
                addMarker(it.place_name, it.y.toDouble(), it.x.toDouble())
            }
        }else {
            mData?.let {
                addCenterPoint()
                if (it.latitude != null && it.latitude > 0.0 && it.longitude != null && it.longitude > 0.0) {
                    viewModel.getCategory(mCode!!, it.longitude, it.latitude)
                }
            }
        }
    }

    private fun addCenterPoint() {
        mData?.let {
            if (it.latitude != null && it.latitude > 0.0 && it.longitude != null && it.longitude > 0.0) {
                mMapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude), true)
                addMarker(it.title, it.latitude, it.longitude)
            }
        }
    }

    override fun onPause() {
        binding.rootHomeDetailMap.removeView(mMapView)
        super.onPause()
    }
    override fun onStop() {
        binding.rootHomeDetailMap.removeView(mMapView)
        super.onStop()
    }

    private fun addMarker(title: String?, latitude: Double, longitude: Double) {
        val marker = MapPOIItem()
        marker.itemName = title ?: ""
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mMapView?.addPOIItem(marker)
    }

    private val listener = object : MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) = Unit

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) = Unit

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {
            val doc = markerData?.firstOrNull { it.place_name == p1?.itemName }
            val url = "kakaomap://place?id=${doc?.id}"
//            val url = "kakaomap://search?q=맛집&p=${latitude},${longitude}"

            try {
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
                    startActivity(it)
                }
            }catch (e: ActivityNotFoundException) {
                Intent(Intent.ACTION_VIEW).also { intent ->
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("market://details?id=net.daum.android.map")
                    startActivity(intent)
                }
            }
        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) = Unit
    }
}


class TouchableWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private val listener: HomeDetailMapFragment.OnMapTouchListener?
) : FrameLayout(context, attrs, defStyleAttr) {
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> listener?.onTouch()
            MotionEvent.ACTION_UP -> listener?.onTouch()
        }
        return super.dispatchTouchEvent(event)
    }
}