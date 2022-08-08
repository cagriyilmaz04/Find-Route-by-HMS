package com.example.hmscorekits.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.DirectionRequestModel
import com.example.hmscorekits.R
import com.example.hmscorekits.RequestPermission
import com.example.hmscorekits.data.model.Coordinate
import com.example.hmscorekits.data.model.Route
import com.example.hmscorekits.data.model.RouteChoice
import com.example.hmscorekits.databinding.FragmentHomeBinding
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    private val TAG = "HomeFragment"

    private val viewModel: HomeViewModel by viewModels()
    private val widthPixels = Resources.getSystem().displayMetrics.widthPixels
    private val heightPixels = Resources.getSystem().displayMetrics.heightPixels
    private lateinit var huaweiMap: HuaweiMap
    private lateinit var marker: Marker
    private lateinit var cameraUpdate: CameraUpdate
    private lateinit var cameraPosition: CameraPosition
    private val MAP_BUNDLE_KEY = "MapBundleKey"

    // For location
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient


    companion object {
        var choice = -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        MapsInitializer.initialize(requireContext())
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        requestLocationPermissions()


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
        }

        binding.huaweiMapView.getMapAsync(this)
        binding.huaweiMapView.onCreate(mapViewBundle)


        viewModel.aaa = 7



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.DrivingLayout.setOnClickListener {
            choice = 0
            if (viewModel.originCoordinate == null) {
                Toast.makeText(requireContext(), "originCoordinate is empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (viewModel.destinationCoordinate == null) {
                Toast.makeText(
                    requireContext(),
                    "destinationCoordinate is empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            getDirectionRoutes(
                viewModel.originCoordinate!!,
                viewModel.destinationCoordinate!!,
                RouteChoice.DRIVING
            )
        }

        binding.BicyclingLayout.setOnClickListener {
            if (viewModel.originCoordinate == null) {
                Toast.makeText(requireContext(), "originCoordinate is empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (viewModel.destinationCoordinate == null) {
                Toast.makeText(
                    requireContext(),
                    "destinationCoordinate is empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            choice = 1
            getDirectionRoutes(
                viewModel.originCoordinate!!,
                viewModel.destinationCoordinate!!,
                RouteChoice.CYCLING
            )
        }

        binding.WalkingLayout.setOnClickListener {
            if (viewModel.originCoordinate == null) {
                Toast.makeText(requireContext(), "originCoordinate is empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (viewModel.destinationCoordinate == null) {
                Toast.makeText(
                    requireContext(),
                    "destinationCoordinate is empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            choice = 2
            getDirectionRoutes(
                viewModel.originCoordinate!!,
                viewModel.destinationCoordinate!!,
                RouteChoice.WALKING
            )
        }


    }

    fun getDirectionRoutes(
        originCoordinate: Coordinate,
        destinationCoordinate: Coordinate,
        routeChoice: RouteChoice
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val mockOriginCoordinate = Coordinate(-4.66529, 54.216608)
            val mockDestinationCoordinate = Coordinate(-4.66552, 54.2166)
            val mockDirectionRequestModel =
                DirectionRequestModel(mockOriginCoordinate, mockDestinationCoordinate)
            val directionRequest = DirectionRequestModel(originCoordinate, destinationCoordinate)
            when (routeChoice) {
                RouteChoice.DRIVING -> {
                    // viewModel.destinationCoordinate=destinationCoordinate
                    val response = viewModel.getDrivingRoute(directionRequest)
                    Log.d("Deneme", "response Code: ${response.returnCode}")
                    Log.d("Deneme", "response Desc: ${response.returnDesc}")
                    Log.d("Deneme", "response:$response")

                    val route = response.routes[0]
                    withContext(Dispatchers.Main) {
                        binding.textViewCar.text =
                            "Duration: ${response.routes.get(0).paths.get(0).durationText} " + "\n Distance: ${
                                response.routes.get(0).paths.get(0).distanceText
                            }"
                        addRouteOnScreen(route)
                    }
                }
                RouteChoice.CYCLING -> {
                    val response = viewModel.getCyclingRoute(directionRequest)
                    Log.d("Deneme", "response Code: ${response.returnCode}")
                    Log.d("Deneme", "response Desc: ${response.returnDesc}")
                    Log.d("Deneme", "response:$response")
                    val route = response.routes[0]
                    withContext(Dispatchers.Main) {
                        binding.textViewBike.text =
                            "Duration: ${response.routes.get(0).paths.get(0).durationText} " + "\n Distance: ${
                                response.routes.get(0).paths.get(0).distanceText
                            }"
                        addRouteOnScreen(route)
                    }
                }
                RouteChoice.WALKING -> {
                    val response = viewModel.getWalkingRoute(directionRequest)
                    Log.d("Deneme", "response Code: ${response.returnCode}")
                    Log.d("Deneme", "response Desc: ${response.returnDesc}")
                    Log.d("Deneme", "response:$response")
                    val route = response.routes[0]
                    withContext(Dispatchers.Main) {
                        binding.textViewWalking.text =
                            "Duration: ${response.routes.get(0).paths.get(0).durationText} " + "\n Distance: ${
                                response.routes.get(0).paths.get(0).distanceText
                            }"
                        addRouteOnScreen(route)
                    }
                }

            }
        }
    }


    override fun onMapReady(map: HuaweiMap?) {
        //mapping

        huaweiMap = map!!

        //camera position settings

        viewModel.originCoordinateLive.observe(viewLifecycleOwner) { coordinate ->
            if (coordinate == null) return@observe
            addMarker(coordinate)
            cameraPosition = CameraPosition.builder()
                .target(
                    LatLng(
                        coordinate.lat,
                        coordinate.lng
                    )
                )
                .zoom(18f)
                .bearing(2.0f)
                .tilt(2.5f).build()
            cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            huaweiMap.moveCamera(cameraUpdate)

            cameraPosition = CameraPosition.builder()
                .target(
                    LatLng(
                        coordinate.lat,
                        coordinate.lng
                    )
                )
                .zoom(18f)
                .bearing(2.0f)
                .tilt(2.5f).build()
            cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            huaweiMap.moveCamera(cameraUpdate)

            var savedMarked: Marker? = null
            var polyLine: Polyline? = null
            huaweiMap.setOnMapLongClickListener { latlng ->
                clearTextViews()

                if (savedMarked != null) {
                    savedMarked?.remove()
                    polyLine?.remove()
                }
                val destinationCoordinate = Coordinate(latlng.longitude, latlng.latitude)
                viewModel.setAllDestinationCoordinate(destinationCoordinate)
                val marker = huaweiMap.addMarker(
                    MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker()) //default marker
                        .title("Second Marker").position(LatLng(latlng.latitude, latlng.longitude))
                )
                savedMarked = marker
                /*
                if (savedMarked != null) {
                    polyLine = huaweiMap.addPolyline(
                        PolylineOptions().add(
                            latlng,
                            PermissionFragment.Location!!
                        )
                    ).apply {
                        width = 3F
                        color = R.color.blue
                    }
                }
                */
            }
        }


    }

    private fun clearTextViews() {
        binding.textViewCar.text = ""
        binding.textViewBike.text = ""
        binding.textViewWalking.text = ""
    }

    private fun addRouteOnScreen(route: Route) {
        val path = route.paths[0]
        val polylineOptions = PolylineOptions()
        polylineOptions.add(path.startLocation.toLatLng())
        path.steps.forEach { step ->
            step.polyline.forEach { polyline ->
                polylineOptions.add(polyline.toLatLng())
            }
        }
        polylineOptions.apply {
            add(path.endLocation.toLatLng())
            color(Color.GREEN)
            width(6F)
        }

        huaweiMap.apply {
            clear()
            addPolyline(polylineOptions)
            setPadding(0, -(heightPixels * 0.3).toInt(), 0, (heightPixels * 0.3).toInt())
            moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds(

                        route.bounds.southwest.toLatLang(),
                        route.bounds.northeast.toLatLang()
                    ),
                    widthPixels,
                    heightPixels, 0
                )
            )
            addMarker(
                MarkerOptions()
                    .position(path.startLocation.toLatLng())
                    .draggable(false)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.start_location
                            )?.toBitmap()
                        )
                    )
                    .title(path.startAddress)
            )
            addMarker(
                MarkerOptions()
                    .position(path.endLocation.toLatLng())
                    .draggable(false)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.end_location
                            )?.toBitmap()
                        )
                    )
                    .title(path.endAddress)
            )
        }
    }

    fun com.huawei.hms.site.api.model.Coordinate.toLatLang(): LatLng {
        return LatLng(this.lat, this.lng)

    }

    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i("Deneme", "android sdk <= 28 Q")
            val strings = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {


            }
            requestPermissions(strings, 1)
        } else {
            val strings = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            )
            // Dynamically apply for required permissions if the API level is greater than 28. The android.permission.ACCESS_BACKGROUND_LOCATION permission is required.
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {


            }
            requestPermissions(strings, 2)

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {


        if (requestCode == 1) {

            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                getSettingClient()
                getLastLocation()

            } else {


            }
        }
        if (requestCode == 2) {
            //  Toast.makeText(this,grantResults[0].toString()+" "+grantResults[1].toString(),Toast.LENGTH_LONG).show()
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {

                getLastLocation()

            } else {

            }
        }
        if (requestCode == 3) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                || grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {


                RequestPermission.requestBackgroundPermission(requireContext())
            } else {

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getSettingClient() {
        var settingsClient = LocationServices.getSettingsClient(requireContext())
        val builder = LocationSettingsRequest.Builder()
        mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
// Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
            // Define the listener for success in calling the API for checking device location settings.
            .addOnSuccessListener(OnSuccessListener { locationSettingsResponse ->
                val locationSettingsStates = locationSettingsResponse.locationSettingsStates
                val stringBuilder = StringBuilder()
                // Checks whether the location function is enabled.
                stringBuilder.append("isLocationUsable=")
                    .append(locationSettingsStates.isLocationUsable)
                // Checks whether HMS Core (APK) is available.
                stringBuilder.append(",\nisHMSLocationUsable=")
                    .append(locationSettingsStates.isHMSLocationUsable)
                Log.i(TAG, "checkLocationSetting onComplete:$stringBuilder")
            })
            // Define callback for failure in checking the device location settings.
            .addOnFailureListener(OnFailureListener { e ->
                Log.i(
                    TAG,
                    "checkLocationSetting onFailure:" + e.localizedMessage
                )
            })
    }

    private fun getLastLocation() {
        try {
            val lastLocation =
                mFusedLocationProviderClient.lastLocation
            lastLocation.addOnSuccessListener(OnSuccessListener { location ->
                if (location == null) {
                    Toast.makeText(
                        requireContext(),
                        "Error when getting current location",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "getLastLocation: Failed")
                    //  LocationLog.i(TAG, "getLastLocation onSuccess location is null")
                    return@OnSuccessListener
                }

                val currentCoordinate = Coordinate(location.longitude, location.latitude)
                viewModel.setAllOriginCoordinate(currentCoordinate)
                // addMarker(currentCoordinate)

                return@OnSuccessListener
            }).addOnFailureListener { e: Exception ->
                Toast.makeText(requireContext(), e.localizedMessage.toString(), Toast.LENGTH_LONG)
                    .show()

            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage.toString(), Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun addMarker(coordinate: Coordinate) {
        huaweiMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker()) //default marker
                .title("First Marker") // maker title
                .position(
                    LatLng(
                        coordinate.lat,
                        coordinate.lng
                    )
                ) //marker position
        )
    }

}