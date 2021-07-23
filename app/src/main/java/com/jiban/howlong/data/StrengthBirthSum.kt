package com.jiban.howlong.data

data class StrengthBirthSum(
    var myBirthSum: Int,
    var yourBirthSum: Int,
    var totalBirthSum: Int
) {
    constructor() : this(0, 0, 0)
}