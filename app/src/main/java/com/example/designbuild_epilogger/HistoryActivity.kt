package com.example.designbuild_epilogger

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HistoryActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference("users/${auth.currentUser?.uid}/uploads")
        setContent {
            YourProjectTheme {
                HistoryActivityScreen(databaseReference)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatLocalDateTime(localDateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")
    return localDateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateForQuery(date: String): String {
    val inputFormat = DateTimeFormatter.ofPattern("dd-MM-yy")
    val outputFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
    val parsedDate = LocalDate.parse(date, inputFormat)
    return parsedDate.format(outputFormat)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryActivityScreen( databaseReference: DatabaseReference) {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current
    var startDate by remember { mutableStateOf(formatLocalDateTime(LocalDateTime.now())) }
    var endDate by remember { mutableStateOf(formatLocalDateTime(LocalDateTime.now())) }
    var fetchingLogs by remember { mutableStateOf(false) }
    var uploadList by remember { mutableStateOf<List<UploadActivity.UploadData>>(emptyList()) }

    val calendar = Calendar.getInstance()

    val startDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                startDate = String.format(Locale.ENGLISH,"%02d-%02d-%02d", dayOfMonth, month + 1, year % 100)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val endDatePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                endDate = String.format(Locale.ENGLISH,"%02d-%02d-%02d", dayOfMonth, month + 1, year % 100)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

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

        if (!fetchingLogs) {
            // Date pickers and fetch logs button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                // Start date picker
                Text(
                    text = "Start Date: $startDate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { startDatePickerDialog.show() }
                )

                // End date picker
                Text(
                    text = "End Date: $endDate",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { endDatePickerDialog.show() }
                )

                Button(
                    onClick = {
                        val startDateFormatted = formatDateForQuery(startDate)
                        val endDateFormatted = formatDateForQuery(endDate)
                        fetchLogs(startDateFormatted, endDateFormatted, databaseReference) { logs ->
                            uploadList = logs.sortedByDescending { it.date } // Sort by descending date
                            fetchingLogs = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Fetch Logs",
                        fontSize = 29.sp,
                        color = Color.White,
                        fontFamily = customFont
                    )
                }
            }
        } else {
            // Display fetched logs
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (uploadList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(uploadList) { uploadData ->
                            UploadItem(uploadData)
                        }
                    }
                } else {
                    Text(
                        text = "No logs found for the specified date range",
                        fontSize = 20.sp,
                        color = Color.Gray,
                        fontFamily = customFont,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        fetchingLogs = false
                        uploadList = emptyList()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Go Back",
                        fontSize = 29.sp,
                        color = Color.White,
                        fontFamily = customFont
                    )
                }
            }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UploadItem(uploadData: UploadActivity.UploadData) {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
    ) {
        Text(
            text = "Date: ${LocalDate.parse(uploadData.date, DateTimeFormatter.BASIC_ISO_DATE).format(dateFormatter)}",
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = customFont,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        Text(
            text = "Comment: ${uploadData.comment}",
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = customFont,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Display image using Coil
        Image(
            painter = rememberAsyncImagePainter(uploadData.downloadUrl),
            contentDescription = "Uploaded Image",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .aspectRatio(16f/9f),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun fetchLogs(startDateFormatted: String, endDateFormatted: String, databaseReference: DatabaseReference, callback: (List<UploadActivity.UploadData>) -> Unit) {
    val logsList = mutableListOf<UploadActivity.UploadData>()
    val dateRef = databaseReference.orderByChild("date").startAt(startDateFormatted).endAt(endDateFormatted)

    dateRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            for (logSnapshot in snapshot.children.reversed()) {
                val log = logSnapshot.getValue(UploadActivity.UploadData::class.java)
                log?.let {
                    logsList.add(it)
                }
            }
            callback(logsList)
        }

        override fun onCancelled(error: DatabaseError) {
           Log.e(TAG, "error", error.toException())
        }
    })
}
