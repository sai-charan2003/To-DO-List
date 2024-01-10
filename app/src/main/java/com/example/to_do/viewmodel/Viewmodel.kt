package com.example.to_do.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.database.categoryname
import com.example.to_do.database.categoryrepo
import com.example.to_do.database.todo
import com.example.to_do.database.tododatabase
import com.example.to_do.database.todorepo
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class viewmodel(context: Context):ViewModel() {
    private val repo:todorepo

    val alltodo:LiveData<List<todo>>
    val category:LiveData<List<categoryname>>
    lateinit var gettodobyid:LiveData<todo>
    private val categoryrepo:categoryrepo
    val todosByDate: MutableState<List<todo>> = mutableStateOf(emptyList())

    init {
        val dao=tododatabase.getDatabase(context).todoDAO()
        val categorydao=tododatabase.getDatabase(context).categorydao()
        repo= todorepo(dao)
        categoryrepo=categoryrepo(categorydao)
        alltodo=repo.alltodos
        category=categoryrepo.categorynames


    }
    fun insert(todo: todo)=viewModelScope.launch(Dispatchers.IO) {

        repo.insert(todo)
    }
    fun update(todo: todo)=viewModelScope.launch(Dispatchers.IO) {
        repo.update(todo)
    }
    fun update(categoryname: categoryname)=viewModelScope.launch(Dispatchers.IO){
        categoryrepo.update(categoryname)
    }
    fun gettodoById(id: Int): LiveData<todo> {
        return repo.getNoteById(id)
    }
    fun getfolderbyid(id:Int): LiveData<categoryname>{
        return categoryrepo.getfolderdatabyid(id)
    }
    fun delete(todo: todo)=viewModelScope.launch(Dispatchers.IO) {

        repo.delete(todo)
    }
    fun insertfolderdata(categoryname: categoryname)=viewModelScope.launch(Dispatchers.IO) {
        categoryrepo.insert(categoryname)
    }
    fun delete(categoryname: categoryname)=viewModelScope.launch(Dispatchers.IO) {
        categoryrepo.delete(categoryname)
    }
    fun getTodosByCategoryId(categoryId: Int): LiveData<List<todo>> {
        return repo.gettodofromcategory(categoryId)
    }

    fun getTodosByDate(selectedDate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val todos = repo.gettodosbydate(selectedDate)
            todosByDate.value = todos
        }
    }
    fun getCategoryNameById(categoryId: Int): LiveData<String> {
        Log.d("TAG", "getCategoryNameById: ${categoryrepo.getCategoryNameById(categoryId)}")
        return categoryrepo.getCategoryNameById(categoryId)
    }

}