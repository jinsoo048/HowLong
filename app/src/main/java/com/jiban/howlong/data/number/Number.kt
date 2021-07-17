package com.jiban.howlong.data.number

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "number_table")
data class Number(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "strength") val strength: String
) {
    constructor() : this(0, "", "", "")
}