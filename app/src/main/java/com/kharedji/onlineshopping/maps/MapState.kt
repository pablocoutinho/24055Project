package com.kharedji.onlineshopping.maps


import android.location.Location

import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.clustering.ClusterItem

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.collections.MarkerManager

data class MapState(
    val lastKnownLocation: Location?,
    val clusterItems: List<ZoneClusterItem>,
)



data class ZoneClusterItem(
    val id: String,
    private val title: String,
    private val snippet: String,
    val polygonOptions: PolygonOptions
) : ClusterItem {

    override fun getSnippet() = snippet

    override fun getTitle() = title

    override fun getPosition() = polygonOptions.points.getCenterOfPolygon().center
}



class ZoneClusterManager(
    context: Context,
    googleMap: GoogleMap,
): ClusterManager<ZoneClusterItem>(context, googleMap, MarkerManager(googleMap))