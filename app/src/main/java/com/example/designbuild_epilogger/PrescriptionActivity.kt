package com.example.designbuild_epilogger

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme


class PrescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourProjectTheme {
                PrescriptionActivityScreen()
            }
        }
    }
}

@Composable
fun PrescriptionActivityScreen() {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current

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
                .padding(bottom = 20.dp)
        )
        Text(
            text = "Group 8",
            fontSize = 20.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier
                .padding(bottom = 40.dp)
        )

        Text(
            text = "Prescriptions",
            fontSize = 39.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier
                .padding(bottom = 20.dp)
        )


        Text(
            text = "Your prescriptions will be displayed here.",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp),

            )

        Text(
            text = "Back to Dashboard",
            fontSize = 27.sp,
            color = Color(0xFF2b4a84),
            fontFamily = customFont,
            modifier = Modifier
                .padding(top = 300.dp)
                .clickable {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPrescriptionActivityScreen() {
    YourProjectTheme {
        PrescriptionActivityScreen()
    }
}