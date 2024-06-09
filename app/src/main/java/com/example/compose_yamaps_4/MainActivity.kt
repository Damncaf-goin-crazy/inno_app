package com.example.compose_yamaps_4

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import com.yandex.mapkit.geometry.LinearRing
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolygonMapObject
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setApiKey(savedInstanceState)
        MapKitFactory.initialize(this)
        setContent {
            MapScreen()
        }
    }

    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
        if (!haveApiKey) {
            MapKitFactory.setApiKey("2799c068-e03b-4ff4-908a-7802c28709e5")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey", true)
    }
}

@Composable
fun MapScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView?.let {
                        MapKitFactory.getInstance().onStart()
                        it.onStart()
                        moveToStartLocation(it)
                        setMarkerInStartLocation(it, context)
                        drawCustomPolygon(it)
                    }
                }

                Lifecycle.Event.ON_STOP -> {
                    mapView?.let {
                        it.onStop()
                        MapKitFactory.getInstance().onStop()
                    }
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                mapView = this
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

private fun moveToStartLocation(mapView: MapView) {
    val startLocation = Point(55.752085, 48.744618)
    val zoomValue = 16.5f
    mapView.map.move(
        CameraPosition(startLocation, zoomValue, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 2f),
        null
    )
}


private fun setMarkerInStartLocation(mapView: MapView, context: android.content.Context) {
    val marker = createBitmapFromVector(context, R.drawable.ic_pin_black_svg)
    val mapObjectCollection: MapObjectCollection = mapView.map.mapObjects
    val placemarkMapObject: PlacemarkMapObject = mapObjectCollection.addPlacemark(
        Point(55.752085, 48.744618),
        ImageProvider.fromBitmap(marker)
    )
    placemarkMapObject.opacity = 0.5f
    placemarkMapObject.addTapListener { _, _ ->
        Toast.makeText(context, "Иннополис", Toast.LENGTH_SHORT).show()
        true
    }
}

private fun createBitmapFromVector(context: android.content.Context, art: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(context, art) ?: return null
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

private fun drawCustomPolygon(mapView: MapView) {
    val coordinates = listOf(
        Point(55.762710, 48.687393),
        Point(55.766018, 48.707284),
        Point(55.769156, 48.710803),
        Point(55.767635, 48.714430),
        Point(55.765317, 48.713936),
        Point(55.763193, 48.717305),
        Point(55.763953, 48.727712),
        Point(55.765933, 48.732218),
        Point(55.769904, 48.735222),
        Point(55.766259, 48.736317),
        Point(55.766975, 48.742713),
        Point(55.764718, 48.747134),
        Point(55.764670, 48.741640),
        Point(55.759478, 48.741726),
        Point(55.758754, 48.738593),
        Point(55.763004, 48.737263),
        Point(55.762521, 48.730397),
        Point(55.760830, 48.730912),
        Point(55.760058, 48.728079),
        Point(55.752087, 48.739570),
        Point(55.748534, 48.736279),
        Point(55.748541, 48.733916),
        Point(55.754403, 48.733256),
        Point(55.759887, 48.724541),
        Point(55.750044, 48.722073),
        Point(55.725429, 48.748616),
        Point(55.730284, 48.765295),
        Point(55.733590, 48.761797),
        Point(55.735138, 48.756175),
        Point(55.731445, 48.756925),
        Point(55.730425, 48.754551),
        Point(55.734962, 48.741682),
        Point(55.738444, 48.757674),
        Point(55.742875, 48.752677),
        Point(55.744317, 48.754113),
        Point(55.744949, 48.762234),
        Point(55.748958, 48.766357),
        Point(55.751281, 48.764683),
        Point(55.752828, 48.757774),
        Point(55.758890, 48.764460),
        Point(55.765744, 48.765203),
        Point(55.762735, 48.759854),
        Point(55.762902, 48.755991),
        Point(55.769004, 48.757922),
        Point(55.773350, 48.767134),
        Point(55.775983, 48.759482),
        Point(55.775648, 48.752722),
        Point(55.776651, 48.745887),
        Point(55.778740, 48.745219),
        Point(55.779409, 48.742396),
        Point(55.776609, 48.742321),
        Point(55.776568, 48.717286),
        Point(55.780037, 48.714221),
        Point(55.776214, 48.707498),
        Point(55.778713, 48.693656),
        Point(55.771091, 48.686755),
        Point(55.769909, 48.689671),
        Point(55.771800, 48.699547),
        Point(55.770662, 48.704589),
        Point(55.766259, 48.697944),
        Point(55.765284, 48.688252),
        Point(55.762710, 48.687393)

    )
    drawBoundary(coordinates, mapView)
}

 fun drawBoundary(coordinates: List<Point>, mapView: MapView) {
    val mapObjects: MapObjectCollection = mapView.map.mapObjects.addCollection()
     // Create a large polygon that covers the entire map area
     val outerBoundaryCoordinates = listOf(
         Point(90.0, -180.0), // Top-left corner of the world
         Point(90.0, 180.0),  // Top-right corner of the world
         Point(-90.0, 180.0), // Bottom-right corner of the world
         Point(-90.0, -180.0) // Bottom-left corner of the world
     )

     val polygon = Polygon(LinearRing(outerBoundaryCoordinates), listOf(LinearRing(coordinates)))
    val polygonMapObject: PolygonMapObject = mapObjects.addPolygon(polygon)

    // Set fill color with transparency
    val fillColor = Color.argb(50, 255, 0, 0) // Red color with 100/255 transparency
    polygonMapObject.fillColor = fillColor

    // Set stroke color with transparency
    val strokeColor = Color.argb(100, 255, 0, 0)
    polygonMapObject.strokeColor = strokeColor

    // Set stroke width
    polygonMapObject.strokeWidth = 2.0f
}



