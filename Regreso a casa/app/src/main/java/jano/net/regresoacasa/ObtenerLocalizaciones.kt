package jano.net.regresoacasa

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("MissingPermission")
fun getCurrentLocation(fusedLocationProviderClient: FusedLocationProviderClient, onLocationReceived: (android.location.Location) -> Unit) {
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        location?.let { onLocationReceived(it) }
    }
}

fun getLocationAndRoute(startLat: Double,
                        startLon: Double,
                        address: String,
                        onRouteReceived: (List<List<Double>>) -> Unit) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openrouteservice.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(OpenRouteServiceAPI::class.java)

    service.geocode(address,"APIKEY").enqueue(object : retrofit2.Callback<GeocodingResponse> {
        override fun onResponse(call: Call<GeocodingResponse>, response: retrofit2.Response<GeocodingResponse>) {
            if (response.isSuccessful) {
                Log.d("RUTA", "Respuesta exitosa: ${response.body()}")
                val coordinates = response.body()?.features?.firstOrNull()?.geometry?.coordinates
                coordinates?.let { destination ->

                    val start = "$startLon,$startLat"
                    val end = "${destination[0]},${destination[1]}"
                    service.getRoute("APIKEY",start, end).enqueue(object : retrofit2.Callback<RouteResponse> {
                        override fun onResponse(call: Call<RouteResponse>, response: retrofit2.Response<RouteResponse>) {
                            val routeCoordinates = response.body()?.features?.firstOrNull()?.geometry?.coordinates
                            routeCoordinates?.let {
                                onRouteReceived(it)
                            }
                        }

                        override fun onFailure(call: Call<RouteResponse>, t: Throwable) {

                        }
                    })
                }
            } else {
                Log.e("RUTA", "Error en la respuesta: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
        }
    })
}
