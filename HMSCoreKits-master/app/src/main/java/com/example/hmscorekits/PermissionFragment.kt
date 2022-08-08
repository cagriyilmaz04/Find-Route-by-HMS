package com.example.hmscorekits

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hmscorekits.databinding.FragmentPermissionBinding
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.LocationSettingsRequest
import com.huawei.hms.maps.model.LatLng


class PermissionFragment : Fragment() {
    private lateinit var _binding: FragmentPermissionBinding
    private val binding get() = _binding

    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {

            requestLocationPermissions()
            // val Intent=Intent(this,MapActivity::class.java)
            // startActivity(Intent)
        }

        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        requestLocationPermissions()


        return binding.root
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
                Log.i(TAG, "checkLocationSetting onFailure:" + e.localizedMessage)
            })
    }

    private fun getLastLocation() {
        try {
            val lastLocation =
                mFusedLocationProviderClient.lastLocation
            lastLocation.addOnSuccessListener(OnSuccessListener { location ->
                if (location == null) {
                    //  LocationLog.i(TAG, "getLastLocation onSuccess location is null")


                    return@OnSuccessListener
                }
                Location = LatLng(location.latitude, location.longitude)


                return@OnSuccessListener
            }).addOnFailureListener { e: Exception ->
                Toast.makeText(requireContext(), e.localizedMessage.toString(), Toast.LENGTH_LONG)
                    .show()

            }
            findNavController().navigate(R.id.action_permissionFragment_to_locationFragment)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage.toString(), Toast.LENGTH_LONG)
                .show()
        }

    }


    companion object {
        private const val TAG = "LocationKitActivity"
        private const val REQUEST_CODE = 11102021
        var Location: LatLng? = null

    }

}