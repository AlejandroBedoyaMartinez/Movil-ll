package jano.net.regresoacasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class GeocodingResponse(val features: List<Feature>)
data class Feature(val geometry: Geometry)
data class Geometry(val coordinates: List<Double>)

interface OpenRouteServiceAPI {
    @GET("geocode/search")
    fun geocode(
        @Query("text") address: String,
        @Query("api_key") apiKey: String
    ): Call<GeocodingResponse>


    @GET("v2/directions/driving-car")
    fun getRoute(
        @Header("Authorization") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Call<RouteResponse>

}

data class RouteResponse(val features: List<RouteFeature>)
data class RouteFeature(val geometry: RouteGeometry)
data class RouteGeometry(val coordinates: List<List<Double>>)
