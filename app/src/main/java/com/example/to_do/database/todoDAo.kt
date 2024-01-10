package com.example.to_do.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao

interface todoDAo {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: todo)
    @Update
    suspend fun update(todo: todo)
    @Delete
    suspend fun delete(todo: todo)
    @Query("SELECT * FROM  todo ORDER BY ID ASC")
    fun getdata(): LiveData<List<todo>>
    @Query("SELECT * FROM todo WHERE ID = :id")
    fun getdatabyid(id:Int):LiveData<todo>
    @Query("SELECT * FROM todo WHERE categoryid = :categoryid")
    fun gettodofromcategory(categoryid: Int): LiveData<List<todo>>
    @Query("SELECT * FROM todo WHERE date = :selectedDate")
    fun getTodosByDate(selectedDate: String): List<todo>
}
@Dao

interface categorydao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category:categoryname)

    @Delete
    suspend fun delete(category:categoryname)
    @Query("SELECT * FROM  category ORDER BY id ASC")
    fun getcategorydata(): LiveData<List<categoryname>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getcategorybyid(id:Int):LiveData<categoryname>
    @Update
    suspend fun update(category:categoryname)
    @Query("SELECT category FROM category WHERE id = :categoryId")
    fun getCategoryNameById(categoryId: Int): LiveData<String>


}