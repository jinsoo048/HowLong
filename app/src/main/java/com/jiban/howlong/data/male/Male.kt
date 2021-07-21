package com.jiban.howlong.data.male

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "male_table")
data class Male(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "strong") val strong: String,
    @ColumnInfo(name = "weak") val weak: String
) {
    constructor() : this(0, 0, "", "")
}