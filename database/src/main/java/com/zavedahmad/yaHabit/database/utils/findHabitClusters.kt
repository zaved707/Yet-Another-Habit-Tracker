package com.zavedahmad.yaHabit.database.utils

import com.zavedahmad.yaHabit.database.entities.HabitCompletionEntity
import java.time.LocalDate

/**
 * Identifies clusters of habit completions within a specified time span.
 *
 * This function takes a list of habit completion entries, a time span in days,
 * and a minimum number of occurrences. It then scans the habit entries to find
 * periods (clusters) where the habit was completed at least `minOccurrences`
 * times within the given `timeSpanDays`.
 *
 * @param habitEntries A list of [HabitCompletionEntity] objects, representing when a habit was completed.
 *                     It's assumed that these entries might not be sorted by date.
 * @param cycleLength The duration in days to consider for a cluster. For example, if 7,
 *                     the function looks for clusters within any 7-day window.
 * @param minOccurrences The minimum number of habit completions required within the `timeSpanDays`
 *                       to qualify as a cluster.
 * @return A list of [Triple] objects. Each triple represents a found cluster and contains:
 *         - The start date of the cluster ([LocalDate]).
 *         - The end date of the cluster ([LocalDate]), calculated as `startDate + timeSpanDays - 1`.
 *         - The count of habit completions within that cluster (Int).
 *         The returned list contains all such clusters found in the input `habitEntries`.
 */
// this gives clusters for last date + cycle amounto of days

fun findHabitClusters(
    habitEntries: List<HabitCompletionEntity>,
    cycleLength: Int,
    minOccurrences:Double
): List<Triple<LocalDate, LocalDate, Double>> {
    val results = mutableListOf<Triple<LocalDate, LocalDate, Double>>()
    val sortedHabitEntries = habitEntries.sortedBy{
        it.completionDate
    }

    var i = 0
    while (i < sortedHabitEntries.size) {
        val startDate = sortedHabitEntries[i].completionDate

        val endDate = startDate.plusDays(cycleLength-1.toLong())
        var count = 0.0

        var j = i

        while (j < sortedHabitEntries.size) {
            if (sortedHabitEntries[j].completionDate <= endDate) {
                count += sortedHabitEntries[j].repetitionsOnThisDay
                j += 1
            } else {
                break
            }
        }
        if(count >= minOccurrences){
            results.add(Triple(startDate, endDate, count))
        }
        i+=1
    }
    return results
}
//it processes the data from findHabitsClusters function and changes then to a list of dates
fun processDateTriples(tripleList: List<Triple<LocalDate, LocalDate, Double>> ): List<LocalDate>{

    val results = mutableListOf<LocalDate>()
    for (triple in tripleList){
        var currentDate = triple.first
        while(currentDate <= triple.second){
            results.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
    }
    return results.distinct().sorted()
}