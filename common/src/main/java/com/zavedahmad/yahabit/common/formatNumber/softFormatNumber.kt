package com.zavedahmad.yahabit.common.formatNumber

fun softFormatNumber(number :  Double) : String{
    val hasDecimal  = number %1  != 0.0
    return if (hasDecimal){number.toString()}else{(number.toInt()).toString()}

}