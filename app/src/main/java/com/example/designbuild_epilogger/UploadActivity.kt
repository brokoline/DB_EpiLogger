package com.example.designbuild_epilogger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import kotlinx.coroutines.launch

class UploadActivity : ComponentActivity() {
    class UploadData(val comment: String = "",
                     val date: String = "",
                     val storagePath: String = "",
                     val downloadUrl: String = "") {

    }

    private lateinit var auth: FirebaseAuth
    //private var firebaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("comment")
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContent {
            YourProjectTheme {
                UploadActivityScreen(auth)
            }
        }
    }
}

@Composable
fun UploadActivityScreen(auth: FirebaseAuth) {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current
    var inProgress by remember { mutableStateOf(false) }
   // val currentUser = auth.currentUser
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val lifecycleScope = rememberCoroutineScope()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
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
            modifier = Modifier
                .padding(bottom = 15.dp)
        )

        Text(
            text = "Upload image",
            fontSize = 20.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier
                .padding(bottom = 20.dp)
        )

        selectedImageUri?.let {
            Text(
                text = "Preview",
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 22.dp)
                    .background(Color.Gray)
            )
        }
        Text(
            text ="Describe symptoms or the area of concern",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 6.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .padding(horizontal = 22.dp, vertical = 6.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            singleLine = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Choose picture",
                fontSize = 24.sp,
                color = Color.White,
                fontFamily = customFont
            )
        }

        Button(
            onClick = {
                if (!inProgress){
                    inProgress=true
                    lifecycleScope.launch{
                        if (selectedImageUri != null) {
                            uploadImage(auth, selectedImageUri!!, description, context)

                        } else {
                            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Upload picture",
                fontSize = 24.sp,
                color = Color.White,
                fontFamily = customFont
            )
        }

        Text(
            text = "Back to Dashboard",
            fontSize = 24.sp,
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


private fun uploadImage(auth: FirebaseAuth, selectedImageUri: Uri, description: String, context: android.content.Context) {
    val currentUser = auth.currentUser
    currentUser?.let { user ->
        val uid = user.uid
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val dateTimeFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateTime = dateTimeFormat.format(Date())
        val fileName = "$currentDateTime.jpg"
        val storagePath = "user/$uid/images/$fileName"
        val storageReference: StorageReference = FirebaseStorage.getInstance().getReference(storagePath)

        storageReference.putFile(selectedImageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    val databaseReference = FirebaseDatabase.getInstance().getReference("users/$uid/uploads")
                    val uploadId = databaseReference.push().key

                    val uploadData = UploadActivity.UploadData(
                        description,
                        currentDate,
                        storagePath,
                        uri.toString()
                    )

                    uploadId?.let {
                        databaseReference.child(it).setValue(uploadData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Image and comment uploaded successfully", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, DashboardActivity::class.java)
                                context.startActivity(intent)
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(context, "Failed to upload comment: ${exception.message}", Toast.LENGTH_SHORT).show()
                                Log.e("UploadError", "Failed to upload comment", exception)
                            }
                    } ?: run {
                        Toast.makeText(context, "Failed to generate database reference", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UploadError", "Failed to get download URL", exception)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("UploadError", "Image upload failed", exception)
            }
    }
}