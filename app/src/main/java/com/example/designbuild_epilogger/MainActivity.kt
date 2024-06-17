package com.example.designbuild_epilogger

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    val context = LocalContext.current // adding this line to get the context

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

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
            text = "Welcome",
            fontSize = 44.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        BasicTextField(
            value = email,
            onValueChange = {
                email = it
                loginError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .background(Color.LightGray)
                .padding(12.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = { innerTextField ->
                if (email.isEmpty()) {
                    Text("E-mail", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
                innerTextField()
            }
        )

        BasicTextField(
            value = password,
            onValueChange = {
                password = it
                loginError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .background(Color.LightGray)
                .padding(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = { innerTextField ->
                if (password.isEmpty()) {
                    Text("Password", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
                innerTextField()
            }
        )

        if (loginError) {
            Text(
                text = "Invalid username or password",
                color = Color.Red,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                if (email == "test" && password == "test") {
                    val intent = Intent(context, DashboardActivity::class.java)
                    context.startActivity(intent)
                } else {
                    loginError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Log In",
                fontSize = 29.sp,
                fontFamily = customFont,
                color = Color.White
            )
        }

        Text(
            text = "Create user",
            fontSize = 35.sp,
            fontFamily = customFont,
            color = Color(0xFF2b4a84),
            modifier = Modifier
                .padding(top = 30.dp)
                .clickable {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainActivityScreen() {
    YourProjectTheme {
        MainActivityScreen()
    }
}
