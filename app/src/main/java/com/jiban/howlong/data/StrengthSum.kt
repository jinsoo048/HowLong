package com.jiban.howlong.data

data class StrengthSum(
    var mySum: Int,
    var yourSum: Int,
    var totalSum: Int
) {
    constructor() : this(0, 0, 0)
}