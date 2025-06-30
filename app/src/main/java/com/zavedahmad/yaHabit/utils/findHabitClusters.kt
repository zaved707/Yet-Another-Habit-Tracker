package com.zavedahmad.yaHabit.utils

import com.zavedahmad.yaHabit.roomDatabase.HabitCompletionEntity
import java.time.LocalDate

fun findHabitClusters(
    habitEntries: List<HabitCompletionEntity>,
    timeSpanDays: Int,
    minOccurrences: Int
): List<Triple<LocalDate, LocalDate, Int>> {
    val results = mutableListOf<Triple<LocalDate, LocalDate, Int>>()


    val sortedHabitEntries = habitEntries.sortedBy{
        it.completionDate
    }

    var i = 0
    while (i < sortedHabitEntries.size) {
        val startDate = sortedHabitEntries[i].completionDate

        val endDate = startDate.plusDays(timeSpanDays-1.toLong())
        var count = 0

        var j = i
        while (j < sortedHabitEntries.size) {
            if (sortedHabitEntries[j].completionDate <= endDate) {
                count += 1
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
fun processDateTriples(tripleList: List<Triple<LocalDate, LocalDate, Int>> ): List<LocalDate>{

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