package com.shivang.sql_lite_database

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shivang.sql_lite_database.ui.components.DatabaseScreen
import com.shivang.sql_lite_database.ui.components.SqlLiteDbHelper
import com.shivang.sql_lite_database.ui.theme.SQL_Lite_DatabaseTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val db by lazy {
        SqlLiteDbHelper(this, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SQL_Lite_DatabaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var readData by remember { mutableStateOf<List<Pair<Int, String>>>(emptyList()) }
                    LaunchedEffect(Unit) { readData = readData() }
                    DatabaseScreen(
                        modifier = Modifier.padding(innerPadding),
                        fetchedData = readData,
                        onDataCreated = {
                            addNewData(
                                name = it,
                                age = Math.random().roundToInt().coerceIn(20..50).toString()
                            )
                            readData = readData()
                        },
                        onReadData = {
                            readData = readData()
                        },
                        updateDataInDatabase = { id, name ->
                            updateData(id, name)
                            readData = readData()
                        },
                        deleteDataFromDatabase = {
                            deleteData(it)
                            readData = readData()
                        }
                    )
                }
            }
        }
    }

    private fun addNewData(name: String, age: String) {
        db.addName(name, age)
    }

    @SuppressLint("Range")
    private fun readData(): List<Pair<Int,String>> {
        val cursor = db.getName()
        val personList = mutableListOf<Pair<Int, String>>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(SqlLiteDbHelper.ID_COL))
                val name = cursor.getString(cursor.getColumnIndex(SqlLiteDbHelper.NAME_COl))
                personList.add(Pair(id, name))
            } while (cursor.moveToNext())
        }

        cursor?.close()

        return personList
    }

    private fun updateData(id: Int, name: String) {
        db.updateData(id = id, name = name)
    }

    private fun deleteData(id: Int) {
        db.deleteData(id)
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}