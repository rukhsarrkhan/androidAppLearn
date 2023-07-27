package com.example.secondproject.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "gadget")
data class Gadget(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : String,
    @ColumnInfo(name = "author")
    val author : String,
    @ColumnInfo(name = "width")
    val width : Int,
    @ColumnInfo(name = "height")
    val height : Int,
    @ColumnInfo(name = "url")
    val url : String,
    @ColumnInfo(name = "download_url")
    val download_url : String,
)