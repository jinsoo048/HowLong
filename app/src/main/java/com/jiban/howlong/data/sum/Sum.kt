package com.jiban.howlong.data.sum

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sum_table")
data class Sum(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "psum_range_Start") val psum_range_Start: String,
) {
    constructor() : this(0, "", "")
}
