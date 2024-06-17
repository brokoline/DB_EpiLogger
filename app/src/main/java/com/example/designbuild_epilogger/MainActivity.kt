package com.example.designbuild_epilogger

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import com.example.designbuild_epilogger.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourProjectTheme {
                MainActivityScreen()
            }
        }
    }
}

@Composable
fun MainActivityScreen() {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))

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

        Column(
            modifier = Modifier
                .background(Color.Gray)
                .padding(26.dp)
                .padding(top = 32.dp, bottom = 22.dp)
                .defaultMinSize(minHeight = 200.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome",
                fontSize = 44.sp,
                color = Color(0xFF1e3e7e),
                fontFamily = customFont
            )

            var email by remember { mutableStateOf("") }
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color.LightGray)
                    .padding(10.dp),
                decorationBox = { innerTextField ->
                    if (email.isEmpty()) {
                        Text("E-mail")
                    }
                    innerTextField()
                }
            )

            var password by remember { mutableStateOf("") }
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(Color.LightGray)
                    .padding(10.dp),
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (password.isEmpty()) {
                        Text("Password")
                    }
                    innerTextField()
                }
            )

            Button(
                onClick = { /* Handle login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
            ) {
                Text(
                    text = "Log In",
                    fontSize = 29.sp,
                    color = Color.White
                )
            }

            Text(
                text = "Create user",
                fontSize = 30.sp,
                color = Color(0xFF2b4a84),
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable { /* Handle create user */ }
            )
        }
    }
}
