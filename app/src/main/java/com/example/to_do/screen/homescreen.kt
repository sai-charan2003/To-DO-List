package com.example.to_do.screen

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton

import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.to_do.R
import com.example.to_do.database.categoryname
import com.example.to_do.database.todo
import com.example.to_do.dateconverters.convertDateFormat
import com.example.to_do.dateconverters.convertDateFormatfororder
import com.example.to_do.dateconverters.stringToLocalDate
import com.example.to_do.ui.theme.TODOTheme
import com.example.to_do.viewmodel.viewmodel

import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.mrerror.singleRowCalendar.SingleRowCalendar
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homescreen() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var addcategory by remember {
        mutableStateOf(false)
    }

    var day by remember { mutableStateOf("") }

    var calenderview by remember {
        mutableStateOf(false)
    }
    var updateid by remember {
        mutableStateOf(0)
    }
    var showcompletedtasks by remember {
        mutableStateOf(false)
    }

    val radioOptions = listOf("High", "Medium", "Low", "none")
    val selectedValue = remember { mutableStateOf("") }

    var (selectedOption, onOptionSelected) = remember { mutableStateOf<String?>(radioOptions[3]) }
    var showalertbox by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember { FocusRequester() }
    var date: LocalDate?
    val calenderstate = rememberUseCaseState()
    var selectedate by remember {
        mutableStateOf<String?>("Due Date")
    }

    CalendarDialog(
        state = calenderstate,
        selection = CalendarSelection.Date {
            date = it
            selectedate = convertDateFormat(date.toString())

        }
    )
    var showstatusbox by remember {
        mutableStateOf(false)
    }
    var coroutine = rememberCoroutineScope()


    val statusOptions = listOf("New Task", "In Progress", "Completed")
    val statusselectedValue = remember { mutableStateOf("") }

    var (statusselectedOption, statusonOptionSelected) = remember { mutableStateOf<String?>(statusOptions[0]) }
    var todo by remember {
        mutableStateOf<String?>("")
    }
    var tododis by remember {
        mutableStateOf<String?>("")
    }
    var categoryid by remember {
        mutableStateOf<Int?>(null)
    }
    var categorydummy by remember {
        mutableStateOf(0)
    }
    var isselecteddummy by remember{
        mutableStateOf(false)
    }


    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewmodel = viewModel<viewmodel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewmodel(
                    context
                ) as T
            }
        }
    )
    var todosByDate: List<todo> by viewmodel.todosByDate
    var ischecked by remember {
        mutableStateOf(false)
    }
    var color by remember{
        mutableStateOf(Color.Gray)
    }
    var showcatergories by remember{
        mutableStateOf(false)
    }
    var isSelected by androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(false)
    }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var categoryname by remember {
        mutableStateOf("")
    }
    var editbottomsheet by remember {
        mutableStateOf(false)
    }
    val checkedStates = remember { mutableStateListOf<Boolean>() }

    var inputchipselected by remember { mutableStateOf(false) }
    val tododata by viewmodel.alltodo.observeAsState(emptyList())
    val categorydata by viewmodel.category.observeAsState(emptyList())
    Scaffold(topBar = {
        TopAppBar(title = { Text("Tasks") })
    },  bottomBar = { BottomAppBar(
        actions =
        {
            IconButton(onClick = { showcompletedtasks=true }) {
                Icon(imageVector = Icons.Outlined.DoneAll, contentDescription = null)

            }
            IconButton(onClick = { calenderview=true}) {
                Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null)

            }
            IconButton(onClick = { showcatergories=true}) {
                Icon(imageVector = Icons.Outlined.Category, contentDescription = null)

            }



        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showBottomSheet = true


            }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add task")

            }
        },
    )
    }) { it ->
        Column(modifier = Modifier.padding(it)) {
            LazyRow() {

                items(categorydata.size) {

                    val category = categorydata[it]

                    val isSelected = (category.category == selectedCategory)
                    InputChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                selectedCategory = ""
                                categoryid = 0
                                isselecteddummy=false

                            } else {
                                selectedCategory = category.category
                                categoryid = category.id
                                isselecteddummy=true

                            }
                        },
                        label = { Text(categorydata[it].category) },
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
            if (!isselecteddummy){


                LazyColumn() {
                    items(tododata.size) {

                        val dismissState = rememberDismissState()
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                            viewmodel.delete(tododata[it])
                        }
                        SwipeToDismiss(
                            state = dismissState,

                            modifier = Modifier
                                .padding(vertical = Dp(1f)),
                            directions = setOf(
                                DismissDirection.EndToStart
                            ),

                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.White
                                        else -> Color.Red
                                    }, label = ""
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Default.Delete

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                                    label = ""
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = Dp(20f)),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Delete Icon",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },

                            dismissContent = {
                                if (tododata[it].ischecked == "false") {

                                    ListItem({

                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            ischecked = tododata[it].ischecked != "false"
                                            if (tododata[it].priority == "High") {
                                                color = Color.Red

                                            } else if (tododata[it].priority == "Medium") {
                                                color = Color.Yellow
                                            } else if (tododata[it].priority == "Low") {
                                                color = Color.Green
                                            } else {
                                                color = Color.Gray
                                            }

                                            Checkbox(
                                                checked = ischecked,
                                                onCheckedChange = { bool ->
                                                    ischecked = bool;
                                                    viewmodel.update(
                                                        todo(
                                                            id = tododata[it].id,
                                                            todo = tododata[it].todo,
                                                            body = tododata[it].body,
                                                            status = "Completed",
                                                            priority = tododata[it].priority,
                                                            date = tododata[it].date,
                                                            ischecked = bool.toString(),
                                                            categoryid = tododata[it].categoryid,
                                                            categoryname = tododata[it].categoryname
                                                        )
                                                    )

                                                },
                                                colors = CheckboxDefaults.colors(
                                                    uncheckedColor = color,
                                                    checkedColor = color
                                                )
                                            )
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center
                                            ) {


                                                tododata[it].todo?.let { it1 ->
                                                    Text(
                                                        text = it1,
                                                        style = MaterialTheme.typography.titleMedium


                                                    )
                                                }
                                                if (tododata[it].body != "") {
                                                    tododata[it].body?.let { it1 ->
                                                        Text(
                                                            text = it1,
                                                            style = MaterialTheme.typography.titleSmall,

                                                            )
                                                    }

                                                }
                                            }


                                        }
                                        Column {
                                            Row {
                                                if (tododata[it].date == "Due Date") {

                                                } else {


                                                    AssistChip(onClick = {
                                                        editbottomsheet = true;
                                                        todo = tododata[it].todo;
                                                        updateid = tododata[it].id;
                                                        tododis = tododata[it].body;
                                                        selectedate = tododata[it].date;
                                                        selectedOption = tododata[it].priority
                                                        statusselectedOption = tododata[it].status
                                                        date= tododata[it].date?.let { it1 ->
                                                            stringToLocalDate(
                                                                it1
                                                            )
                                                        }
                                                    }, label = {
                                                        Row {
                                                            Icon(
                                                                imageVector = Icons.Outlined.DateRange,
                                                                contentDescription = null
                                                            )


                                                            tododata[it].date?.let { it1 ->
                                                                Text(
                                                                    it1,
                                                                    modifier = Modifier.align(
                                                                        Alignment.CenterVertically
                                                                    )
                                                                )
                                                            }

                                                        }
                                                    }, modifier = Modifier.padding(start = 30.dp))


                                                }
                                                if (tododata[it].categoryid != null) {
                                                    AssistChip(onClick = {
                                                        editbottomsheet = true;
                                                        todo = tododata[it].todo;
                                                        updateid = tododata[it].id;
                                                        tododis = tododata[it].body;
                                                        selectedate = tododata[it].date;
                                                        selectedOption = tododata[it].priority
                                                        statusselectedOption = tododata[it].status
                                                        date= tododata[it].date?.let { it1 ->
                                                            stringToLocalDate(
                                                                it1
                                                            )
                                                        }
                                                    }, label = {
                                                        Row {
                                                            Icon(
                                                                imageVector = Icons.Outlined.Category,
                                                                contentDescription = null
                                                            )


                                                            tododata[it].categoryname?.let { it1 ->
                                                                Text(
                                                                    it1,
                                                                    modifier = Modifier.align(
                                                                        Alignment.CenterVertically
                                                                    )
                                                                )
                                                            }

                                                        }
                                                    }, modifier = Modifier.padding(start = 10.dp))
                                                }
                                                if (tododata[it].status != "New") {
                                                    AssistChip(onClick = {
                                                        editbottomsheet = true;
                                                        todo = tododata[it].todo;
                                                        updateid = tododata[it].id;
                                                        tododis = tododata[it].body;
                                                        selectedate = tododata[it].date;
                                                        selectedOption = tododata[it].priority
                                                        statusselectedOption = tododata[it].status
                                                        date= tododata[it].date?.let { it1 ->
                                                            stringToLocalDate(
                                                                it1
                                                            )
                                                        }
                                                    }, label = {


                                                        tododata[it].status?.let { it1 ->
                                                            Text(
                                                                it1
                                                            )

                                                        }
                                                    }, modifier = Modifier.padding(start = 10.dp))


                                                }
                                            }

                                        }


                                    }, modifier = Modifier.clickable {
                                        editbottomsheet = true;
                                        todo = tododata[it].todo;
                                        updateid = tododata[it].id;
                                        tododis = tododata[it].body;
                                        selectedate = tododata[it].date;
                                        selectedOption = tododata[it].priority
                                        statusselectedOption = tododata[it].status
                                        date= tododata[it].date?.let { it1 -> stringToLocalDate(it1) }

                                    }

                                    )


                                }

                            }

                        )

                    }


                }
            }
            else{
                LazyColumn() {
                    items(tododata.size) {


                        val dismissState = rememberDismissState()
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                            viewmodel.delete(tododata[it])
                        }
                        SwipeToDismiss(
                            state = dismissState,

                            modifier = Modifier
                                .padding(vertical = Dp(1f)),
                            directions = setOf(
                                DismissDirection.EndToStart
                            ),

                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.White
                                        else -> Color.Red
                                    }, label = ""
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Default.Delete

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                                    label = ""
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = Dp(20f)),
                                    contentAlignment = alignment
                                ) {
                                    Icon(
                                        icon,
                                        contentDescription = "Delete Icon",
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },

                            dismissContent = {
                                if (tododata[it].ischecked == "false") {
                                    if (tododata[it].categoryname == selectedCategory){

                                        ListItem({

                                            Row(
                                                modifier = Modifier.fillMaxSize(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                ischecked = tododata[it].ischecked != "false"
                                                if (tododata[it].priority == "High") {
                                                    color = Color.Red

                                                } else if (tododata[it].priority == "Medium") {
                                                    color = Color.Yellow
                                                } else if (tododata[it].priority == "Low") {
                                                    color = Color.Green
                                                } else {
                                                    color = Color.Gray
                                                }

                                                Checkbox(
                                                    checked = ischecked,
                                                    onCheckedChange = { bool ->
                                                        ischecked = bool;
                                                        viewmodel.update(
                                                            todo(
                                                                id = tododata[it].id,
                                                                todo = tododata[it].todo,
                                                                body = tododata[it].body,
                                                                status = "Completed",
                                                                priority = tododata[it].priority,
                                                                date = tododata[it].date,
                                                                ischecked = bool.toString(),
                                                                categoryid = tododata[it].categoryid,
                                                                categoryname = selectedCategory
                                                            )
                                                        )

                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        uncheckedColor = color,
                                                        checkedColor = color
                                                    )
                                                )
                                                Column(
                                                    modifier = Modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.Center
                                                ) {


                                                    tododata[it].todo?.let { it1 ->
                                                        Text(
                                                            text = it1,
                                                            style = MaterialTheme.typography.titleMedium


                                                        )
                                                    }
                                                    if (tododata[it].body != "") {
                                                        tododata[it].body?.let { it1 ->
                                                            Text(
                                                                text = it1,
                                                                style = MaterialTheme.typography.titleSmall,

                                                                )
                                                        }

                                                    }
                                                }


                                            }
                                            Column {
                                                Row {
                                                    if (tododata[it].date == "Due Date") {

                                                    } else {


                                                        AssistChip(
                                                            onClick = {
                                                                editbottomsheet = true;
                                                                todo = tododata[it].todo;
                                                                updateid = tododata[it].id;
                                                                tododis = tododata[it].body;
                                                                selectedate = tododata[it].date;
                                                                selectedOption =
                                                                    tododata[it].priority
                                                                statusselectedOption =
                                                                    tododata[it].status
                                                            },
                                                            label = {
                                                                Row {
                                                                    Icon(
                                                                        imageVector = Icons.Outlined.DateRange,
                                                                        contentDescription = null
                                                                    )


                                                                    tododata[it].date?.let { it1 ->
                                                                        Text(
                                                                            it1,
                                                                            modifier = Modifier.align(
                                                                                Alignment.CenterVertically
                                                                            )
                                                                        )
                                                                    }

                                                                }
                                                            },
                                                            modifier = Modifier.padding(start = 30.dp)
                                                        )


                                                    }

                                                    if (tododata[it].status != "New") {
                                                        AssistChip(
                                                            onClick = {
                                                                editbottomsheet = true;
                                                                todo = tododata[it].todo;
                                                                updateid = tododata[it].id;
                                                                tododis = tododata[it].body;
                                                                selectedate = tododata[it].date;
                                                                selectedOption =
                                                                    tododata[it].priority
                                                                statusselectedOption =
                                                                    tododata[it].status
                                                            },
                                                            label = {


                                                                tododata[it].status?.let { it1 ->
                                                                    Text(
                                                                        it1
                                                                    )

                                                                }
                                                            },
                                                            modifier = Modifier.padding(start = 10.dp)
                                                        )


                                                    }
                                                }

                                            }


                                        }, modifier = Modifier.clickable {
                                            editbottomsheet = true;
                                            todo = tododata[it].todo;
                                            updateid = tododata[it].id;
                                            tododis = tododata[it].body;
                                            selectedate = tododata[it].date;
                                            selectedOption = tododata[it].priority
                                            statusselectedOption = tododata[it].status

                                        }

                                        )


                                    }
                                }

                            }

                        )
                    }


                }
            }
        }


    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            modifier = Modifier.padding(bottom = 10.dp)
        ) {



            todo?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = {
                        todo = it
                    },
                    placeholder = { Text("Task Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),


                    )
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            tododis?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = {
                        tododis = it
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, start = 5.dp, end = 5.dp),


                    )
            }
            Row {


                LazyRow() {
                    items(categorydata.size) {
                        val category = categorydata[it]

                        val isSelected = (category.category == selectedCategory)
                        InputChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedCategory = ""
                                    categoryid = 0
                                    isselecteddummy=false
                                } else {
                                    selectedCategory = category.category
                                    categoryid = category.id
                                    isselecteddummy=true

                                }
                            },
                            label = { Text(categorydata[it].category) },
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                FilledTonalIconButton(
                    onClick = { addcategory = true },
                    modifier = Modifier.padding(start = 10.dp)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_list_bulleted_add_24),
                        contentDescription = null
                    )


                }
            }
            Row {


                AssistChip(onClick = { calenderstate.show() }, label = {
                    Row {
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                        selectedate?.let {
                            Text(
                                text = it,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }, modifier = Modifier.padding(start = 10.dp))
                AssistChip(onClick = { showalertbox = true }, label = {
                    Row {
                        Icon(imageVector = Icons.Outlined.Flag, contentDescription = null)
                        if (selectedOption == "none")
                            Text(
                                text = "Priority",

                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        else {
                            Text(
                                text = "$selectedOption",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }, modifier = Modifier.padding(start = 10.dp))
                AssistChip(onClick = { showstatusbox = true }, label = {
                    Row {
                        Icon(imageVector = Icons.Outlined.AddTask, contentDescription = null)
                        statusselectedOption?.let { it1 ->
                            Text(
                                text = it1,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }, modifier = Modifier.padding(start = 10.dp))

            }
            Divider()
            Row {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    viewmodel.insert(
                        todo(
                            0,
                            todo,
                            tododis,
                            selectedate,
                            statusselectedOption,
                            selectedOption,
                            categoryid,
                            categoryname = selectedCategory,
                            ischecked = "false"
                        )
                    );
                    showBottomSheet=false;
                    isSelected=false
                    todo=""
                    tododis=""


                }) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = null)

                }
            }
        }
    }
    if (editbottomsheet) {
        var updatedtodotitle by remember {
            mutableStateOf<String?>(null)
        }
        var updatedtododis by remember {
            mutableStateOf<String?>(null)
        }

        ModalBottomSheet(
            onDismissRequest = { editbottomsheet = false },
            modifier = Modifier.padding(bottom = 10.dp)
        ) {

            todo?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = {
                        todo = it
                    },
                    placeholder = { Text("Task Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),


                    )
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            tododis?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = {
                        tododis = it
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    placeholder = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, start = 5.dp, end = 5.dp),


                    )
            }
            Row {


                LazyRow() {
                    items(categorydata.size) {
                        val category = categorydata[it]
                        val isSelected = (category.category == selectedCategory)
                        InputChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedCategory = ""
                                    categoryid = 0
                                } else {
                                    selectedCategory = category.category
                                    categoryid = category.id
                                }
                            },
                            label = { Text(categorydata[it].category) },
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
                FilledTonalIconButton(
                    onClick = { addcategory = true },
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_format_list_bulleted_add_24),
                        contentDescription = null
                    )


                }
            }
            Row {


                AssistChip(onClick = { calenderstate.show() }, label = {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = null
                        )
                        selectedate?.let {
                            Text(
                                text = it,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }, modifier = Modifier.padding(start = 10.dp))
                AssistChip(onClick = { showalertbox = true }, label = {
                    Row {
                        Icon(imageVector = Icons.Outlined.Flag, contentDescription = null)
                        if (selectedOption == "none")
                            Text(
                                text = "Priority",

                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        else {
                            Text(
                                text = "$selectedOption",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }, modifier = Modifier.padding(start = 10.dp))
                AssistChip(onClick = { showstatusbox = true }, label = {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.AddTask,
                            contentDescription = null
                        )
                        statusselectedOption?.let { it1 ->
                            Text(
                                text = it1,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }, modifier = Modifier.padding(start = 10.dp))

            }
            Divider()
            Row {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    Log.d("TAG", "homescreen: $todo")
                    val updatetodo= todo(updateid,
                        todo = todo,
                        body=tododis,
                        date=selectedate,
                        status=statusselectedOption,
                        priority=selectedOption,
                        categoryid=categoryid,
                        categoryname = selectedCategory,
                        ischecked="false")
                    Log.d("TAG", "homescreen: $updatetodo")
                    viewmodel.update(
                        updatetodo
                    )
                    todo=""
                    tododis=""
                    editbottomsheet=false

                }) {
                    Icon(imageVector = Icons.Filled.Send, contentDescription = null)

                }
            }
        }

    }
    if (showalertbox) {
        AlertDialog(onDismissRequest = { showalertbox = false },
            confirmButton = {
                TextButton(onClick = { showalertbox = false }) {
                    Text(text = "Confirm")

                }
            },
            dismissButton = {
                TextButton(onClick = {
                    selectedOption = radioOptions[3];onOptionSelected(
                    radioOptions[3]
                );showalertbox = false
                }) {
                    Text("Cancel")

                }
            },
            text = {
                Column(Modifier.selectableGroup()) {
                    radioOptions.take(3).forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null // null recommended for accessibility with screenreaders
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            })


    }
    if (addcategory) {
        AlertDialog(onDismissRequest = { addcategory = false }, confirmButton = {
            TextButton(onClick = {
                viewmodel.insertfolderdata(
                    categoryname(
                        0,
                        categoryname
                    )
                );
                categoryname=""
            }, enabled = (categoryname.isNotEmpty())) {
                Text(text = "Add")

            }
        }, text = {
            OutlinedTextField(value = categoryname, onValueChange = {
                categoryname = it
            })

        }, title = {
            Text("Add Category")
        })


    }
    if (showstatusbox) {
        AlertDialog(onDismissRequest = { showstatusbox = false },
            confirmButton = {
                TextButton(onClick = { showstatusbox = false }) {
                    Text(text = "Confirm")

                }
            },
            dismissButton = {
                TextButton(onClick = {
                    statusonOptionSelected(statusOptions[0]);showstatusbox = false
                }) {
                    Text("Cancel")

                }
            },
            text = {
                Column(Modifier.selectableGroup()) {
                    statusOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == statusselectedOption),
                                    onClick = { statusonOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == statusselectedOption),
                                onClick = null
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            })



    }
    if(calenderview){
        ModalBottomSheet(onDismissRequest = { calenderview=false },modifier= Modifier.fillMaxSize()) {

            SingleRowCalendar(
                onSelectedDayChange = {
                    day = it.toString()
                    day = convertDateFormatfororder(day)
                    viewmodel.getTodosByDate(day)

                },
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                selectedDayBackgroundColor = Color.Red,
                selectedDayTextColor = Color.White,
                dayNumTextColor = Color.Cyan,
                dayTextColor = MaterialTheme.colorScheme.onBackground,
                iconsTintColor = Color.Gray,
                headTextColor = Color.Green,
                headTextStyle = MaterialTheme.typography.bodyLarge,

                )
            LazyColumn(){
                items(todosByDate.size){
                    if(todosByDate[it].ischecked=="false"){
                        ListItem({

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if(todosByDate[it].ischecked=="true"){
                                    ischecked=true
                                }
                                else{
                                    ischecked=false
                                }
                                if(todosByDate[it].priority=="High"){
                                    color= Color.Red

                                }
                                else if(todosByDate[it].priority=="Medium"){
                                    color= Color.Yellow
                                }
                                else if(todosByDate[it].priority=="Low"){
                                    color= Color.Green
                                }
                                else {
                                    color= Color.Gray
                                }

                                Checkbox(
                                    checked = ischecked, onCheckedChange = {bool->
                                        ischecked = bool;
                                        viewmodel.update(todo(id=todosByDate[it].id,todo=todosByDate[it].todo,body=todosByDate[it].body,status=todosByDate[it].status, priority = todosByDate[it].priority,date=todosByDate[it].date, ischecked = bool.toString(), categoryid = todosByDate[it].categoryid, categoryname = selectedCategory))

                                    },colors = CheckboxDefaults.colors(uncheckedColor = color, checkedColor = color))
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {


                                    todosByDate[it].todo?.let { it1 ->
                                        Text(
                                            text = it1,
                                            style = MaterialTheme.typography.titleMedium


                                        )
                                    }
                                    if (todosByDate[it].body != "") {
                                        todosByDate[it].body?.let { it1 ->
                                            Text(
                                                text = it1,
                                                style = MaterialTheme.typography.titleSmall,

                                                )
                                        }

                                    }
                                }


                            }
                            Column {
                                Row {

                                    if (todosByDate[it].status != "New") {
                                        AssistChip(onClick = {
                                            editbottomsheet = true;
                                            todo = todosByDate[it].todo;
                                            tododis = todosByDate[it].body;
                                            selectedate = todosByDate[it].date;
                                            selectedOption = todosByDate[it].priority
                                            statusselectedOption = todosByDate[it].status
                                        }, label = {


                                            todosByDate[it].status?.let { it1 ->
                                                Text(
                                                    it1
                                                )

                                            }
                                        }, modifier = Modifier.padding(start = 10.dp))


                                    }
                                    if(todosByDate[it].categoryid!=null){
                                        if (todosByDate[it].status != "New") {
                                            AssistChip(onClick = {
                                                editbottomsheet = true;
                                                todo = todosByDate[it].todo;
                                                tododis = todosByDate[it].body;
                                                selectedate = todosByDate[it].date;
                                                selectedOption = todosByDate[it].priority
                                                statusselectedOption = todosByDate[it].status
                                            }, label = {


                                                todosByDate[it].categoryname?.let { it1 ->
                                                    Text(
                                                        it1
                                                    )

                                                }
                                            }, modifier = Modifier.padding(start = 10.dp))


                                        }
                                    }
                                }

                            }
                            Divider()

                        }, modifier = Modifier.clickable {
                            editbottomsheet = true;
                            todo = todosByDate[it].todo;
                            tododis = todosByDate[it].body;
                            selectedate = todosByDate[it].date;
                            selectedOption=todosByDate[it].priority
                            statusselectedOption=todosByDate[it].status

                        }
                        )

                    }

                }

            }




        }
    }
    if(showcatergories){
        ModalBottomSheet(onDismissRequest = { showcatergories=false },) {
            Column(modifier= Modifier.fillMaxWidth()){
                Text(text = "Categories list", textAlign = TextAlign.Center,modifier= Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.CenterHorizontally),style= MaterialTheme.typography.titleMedium)
                LazyColumn( ){
                    items(categorydata.size) {
                        Column (modifier= Modifier.fillMaxSize()){
                            Row {
                                Text(text = categorydata[it].category,modifier= Modifier.padding(start = 10.dp))
                                Spacer(Modifier.weight(1f))
                                IconButton(onClick = {
                                    viewmodel.delete(
                                        categoryname(
                                            categorydata[it].id,
                                            categorydata[it].category
                                        )
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = null
                                    )

                                }

                            }

                        }
                    }

                }
                FilledTonalButton(onClick = { addcategory=true },modifier= Modifier
                    .padding(top = 10.dp, bottom = 20.dp, end = 10.dp)
                    .align(Alignment.End),) {
                    Text(text = "Add Category", )

                }

            }
        }
    }
    if(showcompletedtasks){
        androidx.compose.material3.ModalBottomSheet(onDismissRequest = {showcompletedtasks=false}) {
            androidx.compose.foundation.lazy.LazyColumn(){
                items(tododata.size){
                    if(tododata[it].ischecked=="true"){
                        ListItem({

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if(tododata[it].ischecked=="false"){
                                    ischecked=false
                                }
                                else{
                                    ischecked=true
                                }
                                if(tododata[it].priority=="High"){
                                    color= Color.Red

                                }
                                else if(tododata[it].priority=="Medium"){
                                    color= Color.Yellow
                                }
                                else if(tododata[it].priority=="Low"){
                                    color= Color.Green
                                }
                                else {
                                    color= Color.Gray
                                }

                                Checkbox(
                                    checked = ischecked, onCheckedChange = {bool->
                                        ischecked = bool;
                                        viewmodel.update(todo(id=tododata[it].id,todo=tododata[it].todo,body=tododata[it].body,status=tododata[it].status, priority = tododata[it].priority,date=tododata[it].date, ischecked = bool.toString(), categoryid = tododata[it].categoryid, categoryname = selectedCategory))

                                    },colors = CheckboxDefaults.colors(uncheckedColor = color, checkedColor = color))
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center
                                ) {


                                    tododata[it].todo?.let { it1 ->
                                        Text(
                                            text = it1,
                                            style = MaterialTheme.typography.titleMedium


                                        )
                                    }
                                    if (tododata[it].body != "") {
                                        tododata[it].body?.let { it1 ->
                                            Text(
                                                text = it1,
                                                style = MaterialTheme.typography.titleSmall,

                                                )
                                        }

                                    }
                                }


                            }
                            Column {
                                Row {
                                    if (tododata[it].date == "Due Date") {

                                    } else {


                                        AssistChip(onClick = { editbottomsheet = true;
                                            todo = tododata[it].todo;
                                            tododis = tododata[it].body;
                                            selectedate = tododata[it].date;
                                            selectedOption=tododata[it].priority
                                            statusselectedOption=tododata[it].status }, label = {
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Outlined.DateRange,
                                                    contentDescription = null
                                                )


                                                tododata[it].date?.let { it1 ->
                                                    Text(
                                                        it1,
                                                        modifier = Modifier.align(Alignment.CenterVertically)
                                                    )
                                                }

                                            }
                                        }, modifier = Modifier.padding(start = 30.dp))

                                    }
                                    if (tododata[it].status != "New") {
                                        AssistChip(onClick = {
                                            editbottomsheet = true;
                                            todo = tododata[it].todo;
                                            tododis = tododata[it].body;
                                            selectedate = tododata[it].date;
                                            selectedOption = tododata[it].priority
                                            statusselectedOption = tododata[it].status
                                        }, label = {


                                            tododata[it].status?.let { it1 ->
                                                Text(
                                                    it1
                                                )

                                            }
                                        }, modifier = Modifier.padding(start = 10.dp))

                                        if (tododata[it].categoryid != null) {
                                            AssistChip(onClick = {
                                                editbottomsheet = true;
                                                todo = tododata[it].todo;
                                                tododis = tododata[it].body;
                                                selectedate = tododata[it].date;
                                                selectedOption = tododata[it].priority
                                                statusselectedOption = tododata[it].status
                                            }, label = {
                                                Log.d("TAG", "homescreen: ${tododata[it].categoryname}")


                                                tododata[it].categoryname?.let { it1 ->
                                                    Text(
                                                        it1
                                                    )

                                                }
                                            }, modifier = Modifier.padding(start = 10.dp))

                                        }
                                    }
                                }

                            }
                            Divider()

                        }, modifier = Modifier.clickable {
                            editbottomsheet = true;
                            todo = tododata[it].todo;
                            tododis = tododata[it].body;
                            selectedate = tododata[it].date;
                            selectedOption=tododata[it].priority
                            statusselectedOption=tododata[it].status

                        }
                        )

                    }
                }
            }

        }
    }


}