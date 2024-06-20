package com.example.designbuild_epilogger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        databaseReference = FirebaseDatabase.getInstance().getReference("messages")
        setContent {
            YourProjectTheme {
                MessageActivityScreen(auth, databaseReference)
            }
        }
    }
}

@Composable
fun MessageActivityScreen(auth: FirebaseAuth, databaseReference: DatabaseReference) {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }

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
            modifier = Modifier
                .padding(bottom = 50.dp)
        )

        Text(
            text = "Write a message to your journal",
            fontSize = 20.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier
                .padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(horizontal = 32.dp, vertical = 16.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            ),
            label = { Text("Write your message here...", fontWeight = FontWeight.Bold, fontSize = 20.sp)  },
            maxLines = 6,
            singleLine = false
        )

        Button(
            onClick = {
                if (message.isNotEmpty()) {
                    uploadMessage(auth, databaseReference, message, context)
                } else {
                    Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Send",
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
                .padding(top = 40.dp)
                .clickable {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

private fun uploadMessage(auth: FirebaseAuth, databaseReference: DatabaseReference, message: String, context: android.content.Context) {
    val currentUser = auth.currentUser
    currentUser?.let { user ->
        val uid = user.uid
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val referenceNumber = (10000000..99999999).random().toString() // generating random reference rumber for now, should be changed later

        val messageData = MessageData(
            message,
            currentDate,
            referenceNumber
        )

        val userMessagesRef = databaseReference.child("users").child(uid).child("messages")
        val messageId = userMessagesRef.push().key
        messageId?.let {
            userMessagesRef.child(it).setValue(messageData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to upload message: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } ?: run {
            Toast.makeText(context, "Failed to generate database reference", Toast.LENGTH_SHORT).show()
        }
    }
}


data class MessageData(val message: String, val date: String, val referenceNumber: String)
