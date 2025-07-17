// Android Ride Hailing Widget
// Created by Umer
package org.umer.cmpplayground

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RideHailingWidgetProvider : AppWidgetProvider() {
    
    companion object {
        const val ACTION_REFRESH = "org.umer.cmpplayground.ACTION_REFRESH"
    }
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update each widget instance
        for (appWidgetId in appWidgetIds) {
            updateRideWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        if (intent.action == ACTION_REFRESH) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                context.packageName.let { 
                    android.content.ComponentName(it, "$it.RideHailingWidgetProvider")
                }
            )
            
            // Refresh all widgets
            for (appWidgetId in appWidgetIds) {
                updateRideWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
    
    private fun updateRideWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.ride_widget_layout)
        
        // Set up refresh button click listener
        val refreshIntent = Intent(context, RideHailingWidgetProvider::class.java).apply {
            action = ACTION_REFRESH
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context, 0, refreshIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh_btn, refreshPendingIntent)
        
        // Show loading initially
        views.setTextViewText(R.id.widget_status, "Loading rides...")
        views.setTextViewText(R.id.widget_ride_info, "")
        
        // Update widget with loading state
        appWidgetManager.updateAppWidget(appWidgetId, views)
        
        // Fetch data in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = RideHailingRepository()
                val locationHelper = AndroidLocationHelper(context)
                
                // Get current location or use default (New York)
                val currentLocation = try {
                    locationHelper.getCurrentLocation()
                } catch (e: Exception) {
                    // Fallback to New York coordinates
                    LocationData(40.7128, -74.0060, cityName = "New York")
                }
                
                val response = repository.fetchAvailableRides(
                    currentLocation.latitude, 
                    currentLocation.longitude
                )
                
                withContext(Dispatchers.Main) {
                    println("Widget response: success=${response?.success}, rides=${response?.available_rides?.size}")
                    println("Widget location: ${currentLocation.cityName} (${currentLocation.latitude}, ${currentLocation.longitude})")
                    
                    if (response?.success == true && response.available_rides.isNotEmpty()) {
                        val nearestRide = response.available_rides.minByOrNull { it.eta_minutes }
                        
                        nearestRide?.let { ride ->
                            views.setTextViewText(R.id.widget_status, "🚗 Nearest Ride")
                            views.setTextViewText(
                                R.id.widget_ride_info,
                                "${ride.driver_name}\n${ride.vehicle_model}\n${ride.eta_minutes} min • $${String.format("%.2f", ride.fare_estimate.total)}\n📍 ${currentLocation.cityName}"
                            )
                            views.setTextViewText(R.id.widget_rating, "⭐ ${ride.rating}")
                        }
                    } else {
                        views.setTextViewText(R.id.widget_status, "No rides available")
                        views.setTextViewText(R.id.widget_ride_info, "📍 ${currentLocation.cityName}\nPlease try again later")
                    }
                    
                    // Update widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    views.setTextViewText(R.id.widget_status, "Error loading rides")
                    views.setTextViewText(R.id.widget_ride_info, "Tap to retry")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }
        }
    }
    
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }
    
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
} 