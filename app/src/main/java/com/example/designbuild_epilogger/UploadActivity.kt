package com.example.designbuild_epilogger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import com.example.designbuild_epilogger.R

class UploadActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourProjectTheme {
                UploadActivityScreen()
            }
        }
    }
}

@Composable
fun UploadActivityScreen() {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

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
                .padding(bottom = 50.dp)
        )

        Text(
            text = "EpiUpload",
            fontSize = 39.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier
                .padding(bottom = 20.dp)
        )

        BasicTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .background(Color.LightGray)
                .padding(10.dp),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            decorationBox = { innerTextField ->
                if (description.isEmpty()) {
                    Text("Describe symptoms or the area of concern, including details such as duration, any changes in condition, and other relevant information.")
                }
                innerTextField()
            }
        )

        Button(
            onClick = { imagePickerLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Choose picture",
                fontSize = 29.sp,
                color = Color.White,
                fontFamily = customFont
            )
        }

        selectedImageUri?.let {
            Text(
                text = "Image selected: $it",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                // Handle upload picture functionality
                // You can use the selectedImageUri and description to upload the image
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Upload picture",
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

@Preview(showBackground = true)
@Composable
fun PreviewUploadActivityScreen() {
    YourProjectTheme {
        UploadActivityScreen()
    }
}