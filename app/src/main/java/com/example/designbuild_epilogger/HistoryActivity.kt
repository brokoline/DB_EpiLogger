package com.example.designbuild_epilogger

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import com.example.designbuild_epilogger.R
import java.util.*

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourProjectTheme {
                HistoryActivityScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryActivityScreen() {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            startDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val endDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "EpiLogger",
            fontSize = 66.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier.padding(bottom = 50.dp)
        )

        Text(
            text = "History",
            fontSize = 44.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        Text(
            text = "Choose dates",
            fontSize = 26.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont
        )

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .clickable { startDatePickerDialog.show() },
            readOnly = true,
            label = { Text("Start Date", fontFamily = customFont, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color(0xFF1e3e7e),
                unfocusedBorderColor = Color(0xFF1e3e7e)
            )
        )

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .clickable { endDatePickerDialog.show() },
            readOnly = true,
            label = { Text("End Date", fontFamily = customFont, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Transparent,
                focusedBorderColor = Color(0xFF1e3e7e),
                unfocusedBorderColor = Color(0xFF1e3e7e)
            )
        )

        Button(
            onClick = { /* Handle fetch logs */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Fetch Logs",
                fontSize = 29.sp,
                color = Color.White,
                fontFamily = customFont
            )
        }

        Text(
            text = "Back to Dashboard",
            fontSize = 27.sp,
            color = Color(0xFF2b4a84),
            fontFamily = customFont,
            modifier = Modifier
                .padding(top = 30.dp)
                .clickable {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryActivityScreen() {
    YourProjectTheme {
        HistoryActivityScreen()
    }
}
