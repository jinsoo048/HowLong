package com.jiban.howlong.data.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_table")
data class Character(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "character") val character: String,
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "strength") val strength: String
) {
    constructor() : this(0, "", "", "")
}