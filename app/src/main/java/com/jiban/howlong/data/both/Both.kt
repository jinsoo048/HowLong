package com.jiban.howlong.data.both

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "both_table")
data class Both(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "strong") val strong: String,
    @ColumnInfo(name = "weak") val weak: String,
    @ColumnInfo(name = "nameWeak") val nameWeak: String,
    @ColumnInfo(name = "nameStrong") val nameStrong: String,
    @ColumnInfo(name = "birthWeak") val birthWeak: String,
    @ColumnInfo(name = "birthStrong") val birthStrong: String,
    @ColumnInfo(name = "relationship") val relationship: String
) {
    constructor() : this(0, 0, "", "", "", "", "", "", "")
}