package com.example.to_do.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "todo",foreignKeys = [ForeignKey(entity = categoryname::class, parentColumns = ["id"], childColumns = ["categoryid"],onDelete = ForeignKey.CASCADE)])

data class todo(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val todo:String?,
    val body:String?,
    val date:String?,
    val status:String?,
    val priority:String?,
    val categoryid: Int?,
    val ischecked:String?,
    val categoryname:String?



)
@Entity(tableName="category")

data class categoryname(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val category:String,

    )