package com.example.weanaklie.presentation.main.wakilnieHome

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.friendycar.domain.common.RequestErrorException

import com.example.weanaklie.R
import com.example.weanaklie.model.datamodel.SuggestResponse
import com.example.weanaklie.presentation.common.navigateActivity
import com.example.weanaklie.presentation.common.showSnackbar
import com.example.weanaklie.presentation.common.toggleVisibility
import com.example.weanaklie.presentation.main.home.HomeWakilneActivity
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home_first.*

class HomeFragment : Fragment() {
    val PERMISSION_ID = 42
    private lateinit var suggestDetailViewModel: SuggestDetailViewModel
    var currentlocation: Location? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        getLastLocation()
        observeViewModel()
        view.apply {
            suggestBtn.setOnClickListener {
                if (currentlocation != null) {
                    suggestDetailViewModel.getRequestDetails("${currentlocation?.longitude},${currentlocation?.latitude}")
                } else {
                    getLastLocation()
                }

            }

        }
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
        suggestTv?.toggleVisibility(!progress_bar.isVisible)
    }
    private val errorObserver: Observer<Throwable> = Observer {
        showMessage(it)
    }
    private val suggestResponseDetailsObserver: Observer<SuggestResponse> = Observer {
        if (it.error != "no") {
            showMessage(RequestErrorException(it.error))
        } else {
            val bundle = Bundle()
            bundle.putParcelable("suggest", it)
            bundle.putParcelable("location", currentlocation)
            activity?.navigateActivity(HomeWakilneActivity::class.java, bundle)
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

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        Toast.makeText(
                            activity,
                            "location ${location.latitude} ${location.longitude}",
                            Toast.LENGTH_LONG
                        ).show()
                        currentlocation = location

                    }
                }
            } else {
                Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            Toast.makeText(
                activity,
                "mLastLocation ${mLastLocation.latitude} ${mLastLocation.longitude}",
                Toast.LENGTH_LONG
            ).show()
            currentlocation = locationResult.lastLocation


        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}