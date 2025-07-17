// iOS Ride Hailing Widget
// Created by Umer
package org.umer.cmpplayground

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import platform.WidgetKit.*

// iOS Widget Timeline Entry
data class RideWidgetEntry(
    val date: NSDate,
    val driverName: String,
    val vehicleModel: String,
    val etaMinutes: Int,
    val fareTotal: Double,
    val rating: Double
)

// iOS Widget Timeline Provider
class RideWidgetTimelineProvider {
    
    fun getSnapshot(completion: (RideWidgetEntry) -> Unit) {
        val entry = RideWidgetEntry(
            date = NSDate(),
            driverName = "Sarah Johnson",
            vehicleModel = "Toyota Camry",
            etaMinutes = 3,
            fareTotal = 9.00,
            rating = 4.8
        )
        completion(entry)
    }
    
    fun getTimeline(completion: (List<RideWidgetEntry>) -> Unit) {
        // In a real implementation, you would fetch from your API here
        val currentDate = NSDate()
        val entries = mutableListOf<RideWidgetEntry>()
        
        // Create entries for the next hour (updates every 30 minutes)
        for (hourOffset in 0..1) {
            val entryDate = currentDate.dateByAddingTimeInterval(hourOffset * 30.0 * 60.0)
            val entry = RideWidgetEntry(
                date = entryDate,
                driverName = "Sarah Johnson",
                vehicleModel = "Toyota Camry", 
                etaMinutes = 3 + hourOffset,
                fareTotal = 9.00,
                rating = 4.8
            )
            entries.add(entry)
        }
        
        completion(entries)
    }
}

// iOS Widget Configuration
object RideWidgetConfiguration {
    
    fun createWidget(): String {
        return """
        import SwiftUI
        import WidgetKit
        
        struct RideWidgetEntry: TimelineEntry {
            let date: Date
            let driverName: String
            let vehicleModel: String
            let etaMinutes: Int
            let fareTotal: Double
            let rating: Double
        }
        
        struct RideWidgetEntryView : View {
            var entry: RideWidgetEntry
            
            var body: some View {
                VStack(alignment: .leading, spacing: 4) {
                    HStack {
                        Text("🚗 Nearest Ride")
                            .font(.caption)
                            .foregroundColor(.white)
                        Spacer()
                        Text("⭐ \(String(format: "%.1f", entry.rating))")
                            .font(.caption)
                            .foregroundColor(.yellow)
                    }
                    
                    Text(entry.driverName)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.white)
                    
                    Text(entry.vehicleModel)
                        .font(.caption)
                        .foregroundColor(.white.opacity(0.8))
                    
                    HStack {
                        Text("\(entry.etaMinutes) min")
                            .font(.caption)
                            .foregroundColor(.white)
                        Spacer()
                        Text("$\(String(format: "%.2f", entry.fareTotal))")
                            .font(.caption)
                            .foregroundColor(.white)
                    }
                }
                .padding()
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [Color(red: 0.129, green: 0.588, blue: 0.953), Color(red: 0.129, green: 0.796, blue: 0.953)]),
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    )
                )
                .cornerRadius(16)
            }
        }
        
        struct RideWidget: Widget {
            let kind: String = "RideWidget"
            
            var body: some WidgetConfiguration {
                StaticConfiguration(kind: kind, provider: RideWidgetTimelineProvider()) { entry in
                    RideWidgetEntryView(entry: entry)
                }
                .configurationDisplayName("Ride Hailing")
                .description("Shows nearest available ride with ETA and fare")
                .supportedFamilies([.systemSmall, .systemMedium])
            }
        }
        """
    }
} 