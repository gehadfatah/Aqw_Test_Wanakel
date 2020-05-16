package com.example.weanaklie.presentation.main.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.weanaklie.R
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class DetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var location = LatLng(24.085110, 32.901220)

    private val args: DetailFragmentArgs by navArgs()
    var suggest = SuggestResponse()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        if (location.latitude == 0.0) return
        mMap.addMarker(MarkerOptions().position(location).title(suggest.name))
            .setIcon(bitmapDescriprFromVector(activity!!, R.drawable.ic_place_24px_green))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
        setOnPoiLIstner(mMap)
    }

    private fun bitmapDescriprFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    private fun setOnPoiLIstner(mMap: GoogleMap) {

        mMap.setOnPoiClickListener { poi ->
            val poimarker = mMap.addMarker(
                MarkerOptions().position(poi.latLng).title(poi.name).snippet(
                    String.format(
                        Locale.getDefault(),
                        "Lat:%1$.5f, Long:%2$.5f", poi.latLng.latitude, poi.latLng.longitude
                    )
                )
            )
            poimarker.showInfoWindow()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  (activity?.supportFragmentManager?.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)

       /* val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)

        suggest = args.suggestDetail ?: SuggestResponse()
        if (suggest.lat.isEmpty()) return
        location = LatLng(
            suggest.lat.toDouble(),
            suggest.lon.toDouble()
        )
        view.apply {

        }
    }
}