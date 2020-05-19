package com.example.weanaklie.presentation.main.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.android.friendycar.domain.common.RequestErrorException
import com.android.friendycar.presentation.common.getPlaceName
import com.example.weanaklie.R
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.example.weanaklie.presentation.common.setSlider
import com.example.weanaklie.presentation.common.showSnackbar
import com.example.weanaklie.presentation.common.toggleVisibility
import com.example.weanaklie.presentation.main.wakilnieHome.SuggestDetailViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.slider_detail_layout.*
import java.util.*


class DetailFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var location = LatLng(24.085110, 32.901220)
    var userlocation: Location? = null
    private lateinit var suggestDetailViewModel: SuggestDetailViewModel

    private val args: DetailFragmentArgs by navArgs()
    var suggest = SuggestResponse()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle()
        super.onStop()
    }

    private fun observeViewModel() {
        suggestDetailViewModel = ViewModelProvider(this).get(SuggestDetailViewModel::class.java)

        activity?.run {
            suggestDetailViewModel.suggestResponse.observe(
                this,
                suggestResponseDetailsObserver
            )

            suggestDetailViewModel.error.observe(this, errorObserver)
            suggestDetailViewModel.isLoading.observe(this, loadingObserver)
        }

    }

    private val loadingObserver: Observer<Boolean> = Observer {
        progress_bar?.toggleVisibility(it)
        suggestAnotherTv?.toggleVisibility(!progress_bar.isVisible)
    }
    private val errorObserver: Observer<Throwable> = Observer {
        showMessage(it)
    }
    private val suggestResponseDetailsObserver: Observer<SuggestResponse> = Observer {

        if (it.error != "no") {
            showMessage(RequestErrorException(it.error))
        } else {
            suggest = it
            cheveronImgFrame.performClick()
            setSuggestDetails()
        }
    }

    private fun showMessage(error: Throwable) {

        when (error) {
            is RequestErrorException -> if (!error.message.isNullOrEmpty()) {

                view?.showSnackbar(error.message.toString())

            }

        }
        view?.showSnackbar(error.message.toString())

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
        observeViewModel()
        setlistnerViews()
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync(this)

        suggest = args.suggestDetail ?: SuggestResponse()
        userlocation = args.location
        if (suggest.lat.isEmpty()) return
        location = LatLng(
            suggest.lat.toDouble(),
            suggest.lon.toDouble()
        )
        setSuggestDetails()

        view.apply {

        }
    }

    private fun setSuggestDetails() {
        ratingPlace.text = suggest.rating
        if (suggest.name.getPlaceName().isNotEmpty())
            placeName.text = suggest.name.getPlaceName()
        else placeName.text = suggest.name

        catPlace.text = suggest.cat

        link.text = suggest.link
        isOpen.text = suggest.open

        slider.setSlider(suggest.image, activity!!, custom_indicator)

    }

    fun openGoogleMaps() {
        if (userlocation != null) {

            val uri =
                "http://maps.google.com/maps?saddr=" + userlocation?.latitude.toString() + "," + userlocation?.longitude.toString() + "&daddr=" + suggest.lat.toString() + "," + suggest.lon
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }
    }

    private fun setlistnerViews() {

        openBtn.setOnClickListener {
            openGoogleMaps()
        }

        openBtn2.setOnClickListener {
            openGoogleMaps()

        }
        cheveronImgFrame.setOnClickListener {
            buttonDetailLin.toggleVisibility(!buttonDetailLin.isVisible)


            if (!buttonDetailLin.isVisible) {
                cheveronImg.background =
                    ContextCompat.getDrawable(context!!, R.drawable.chevron_down_24)
                sliderLayout.toggleVisibility(false)
                infoLin.toggleVisibility(false)
            } else {
                sliderLayout.toggleVisibility(sliderLayout.isVisible)
                infoLin.toggleVisibility(infoLin.isVisible)
                cheveronImg.background =
                    ContextCompat.getDrawable(context!!, R.drawable.chevron_up_24)

            }


        }
        suggestAnotherBtn.setOnClickListener {
            if (location != null)
                suggestDetailViewModel.getRequestDetails("${location?.longitude},${location?.latitude}")

        }
        link.setOnClickListener {
            openWebPage(suggest.link)
        }
        moreInfoFrame.setOnClickListener {
            sliderLayout.toggleVisibility(false)
            infoLin.toggleVisibility(!infoLin.isVisible)
        }
        imagesFrame.setOnClickListener {
            infoLin.toggleVisibility(false)
            sliderLayout.toggleVisibility(!sliderLayout.isVisible)
        }
        share.setOnClickListener {
            ShareClicked()
        }

    }

    fun ShareClicked() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, suggest.link)
        sendIntent.type = "text/plain"
        Intent.createChooser(sendIntent, "Share via")
        startActivity(sendIntent)
    }

    fun openWebPage(url: String?) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivity(intent)
        }
    }
}