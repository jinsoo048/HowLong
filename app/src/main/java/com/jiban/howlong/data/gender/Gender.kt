package com.jiban.howlong.data.gender

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gender_table")
data class Gender(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "strength") val strength: String
) {
    constructor() : this(0, "", "")
}



