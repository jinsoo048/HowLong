package com.jiban.howlong.data.score

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class Score(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "percentage") val percentage: String,
    @ColumnInfo(name = "range_Start") val range_Start: String,
    @ColumnInfo(name = "strength") val strength: String
) {
    constructor() : this(0, "", "", "")
}

