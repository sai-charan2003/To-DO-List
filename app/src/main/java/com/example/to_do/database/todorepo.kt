package com.example.to_do.database

import androidx.lifecycle.LiveData


class todorepo(private val tododao:todoDAo) {

    val alltodos: LiveData<List<todo>> = tododao.getdata()


    suspend fun insert(todo: todo) {
        tododao.insert(todo)
    }

    suspend fun update(todo: todo) {
        tododao.update(todo)
    }

    fun getNoteById(id: Int): LiveData<todo> {
        return tododao.getdatabyid(id)
    }

    suspend fun delete(todo: todo) {
        tododao.delete(todo)
    }

    fun gettodofromcategory(categoryid: Int): LiveData<List<todo>> {
        return tododao.gettodofromcategory(categoryid)
    }
    suspend fun gettodosbydate(date: String): List<todo> {
        return tododao.getTodosByDate(date)
    }
}

class categoryrepo(private val categorydao: categorydao) {
    val categorynames: LiveData<List<categoryname>> = categorydao.getcategorydata()
    lateinit var categorynamesbyid:LiveData<categoryname>

    suspend fun insert(categoryname: categoryname){
        categorydao.insert(categoryname)
    }
    suspend fun update(categoryname: categoryname){
        categorydao.update(categoryname)
    }

    fun getfolderdatabyid(id: Int): LiveData<categoryname> {
        return categorydao.getcategorybyid(id)
    }
    suspend fun delete(categoryname: categoryname){
        categorydao.delete(categoryname)
    }
    fun getCategoryNameById(categoryId: Int): LiveData<String> {
        return categorydao.getCategoryNameById(categoryId)
    }
}