package com.shivang.sql_lite_database.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Preview
@Composable
fun DatabaseScreen(
    modifier: Modifier = Modifier,
    fetchedData: List<Pair<Int, String>>? = null,
    onDataCreated: (String) -> Unit = {},
    onReadData: () -> Unit = {},
    updateDataInDatabase: (Int, String) -> Unit = { _, _ -> },
    deleteDataFromDatabase: (Int) -> Unit = {}
) {
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        item {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Sql Lite Database Project",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        item {
            CreateData(
                modifier = fieldModifier,
                onDataCreated = onDataCreated
            )
        }

        item {
            ReadData(
                modifier = fieldModifier,
                fetchedData = fetchedData,
                onReadData = onReadData
            )
        }

        item {
            UpdateData(
                modifier = fieldModifier,
                fetchedData = fetchedData,
                updateDataInDatabase = updateDataInDatabase
            )
        }

        item {
            DeleteData(
                fetchedData = fetchedData,
                modifier = fieldModifier,
                deleteDataFromDatabase = deleteDataFromDatabase
            )
        }
    }
}


@Composable
fun CreateData(
    modifier: Modifier = Modifier,
    onDataCreated: (String) -> Unit
) {
    var createdDataText by remember { mutableStateOf("") }
    var onDataAdded by remember { mutableStateOf(false) }
    var isTextFieldEnabled by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .background(
                color = Color(0xFFCAD1FF),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Add data to SQL Lite Database",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                fontFamily = FontFamily.Default
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            value = createdDataText,
            onValueChange = {
                createdDataText = it
                isTextFieldEnabled = createdDataText.isNotEmpty()
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                enabled = isTextFieldEnabled,
                onClick = {
                    if (createdDataText.isNotEmpty()) {
                        onDataCreated(createdDataText)
                    }
                    onDataAdded = true
                }
            ) {
                Text(
                    text = "Add"
                )
            }
        }

        AnimatedVisibility(
            visible = onDataAdded
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Data added successfully",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }

    LaunchedEffect(onDataAdded) {
        if (onDataAdded) {
            delay(2000)
            onDataAdded = false
            createdDataText = ""
            isTextFieldEnabled = false
        }
    }
}


@Composable
fun ReadData(
    modifier: Modifier = Modifier,
    fetchedData: List<Pair<Int, String>>?,
    onReadData: () -> Unit
) {
    var showSavedData by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .background(
                color = Color(0xFFFFBAB7),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Read data from SQL Lite Database",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                fontFamily = FontFamily.Default
            )
        )

        Button(
            modifier = Modifier.padding(top = 6.dp),
            onClick = {
                showSavedData = showSavedData.not()
                if (showSavedData) {
                    onReadData()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF3A00)
            )
        ) {
            Text(
                text = if (showSavedData.not()) "Fetch Data" else "Hide Data"
            )
        }

        AnimatedContent(fetchedData) {
            when (it.isNullOrEmpty()) {
                true -> {
                    AnimatedVisibility(showSavedData) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "No data to render",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                fontFamily = FontFamily.Monospace,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                false -> {
                    AnimatedVisibility(showSavedData) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            fetchedData?.forEach {
                                Text(
                                    text = it.second,
                                    modifier = Modifier.padding(top = 8.dp, start = 4.dp),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        fontFamily = FontFamily.Serif
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UpdateData(
    fetchedData: List<Pair<Int, String>>?,
    modifier: Modifier = Modifier,
    updateDataInDatabase: (Int, String) -> Unit
) {
    var updateIconEnabled by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .background(
                color = Color(0xFF7BD3CE),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Update data in SQL Lite Database",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                fontFamily = FontFamily.Default
            )
        )

        fetchedData?.forEach { textData ->
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.Blue.copy(alpha = .2f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var currentDataText by remember(textData) { mutableStateOf(textData.second) }
                BasicTextField(
                    enabled = updateIconEnabled,
                    modifier = Modifier.weight(1f),
                    value = currentDataText,
                    onValueChange = {
                        currentDataText = it
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = FontFamily.Monospace
                    )
                )

                AnimatedContent(
                    targetState = updateIconEnabled,
                    label = ""
                ) {
                    when (it) {
                        true -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {
                                            updateDataInDatabase(textData.first, currentDataText)
                                            updateIconEnabled = false
                                        },
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "",
                                    tint = Color.Green
                                )

                                Icon(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(14.dp)
                                        .clickable {
                                            currentDataText = textData.second
                                            updateIconEnabled = false
                                        },
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "",
                                    tint = Color.Red
                                )
                            }
                        }

                        false -> {
                            Icon(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        updateIconEnabled = true
                                    },
                                imageVector = Icons.Default.Edit,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DeleteData(
    fetchedData: List<Pair<Int, String>>?,
    modifier: Modifier = Modifier,
    deleteDataFromDatabase: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xFFFFEE96),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Delete data from SQL Lite Database",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                fontFamily = FontFamily.Default
            )
        )

        fetchedData?.forEach {
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFEFFFFD),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it.second,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Icon(
                    modifier = Modifier
                        .size(14.dp)
                        .clickable {
                            deleteDataFromDatabase(it.first)
                        },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = Color.Red.copy(alpha = .8f)
                )
            }
        }
    }
}