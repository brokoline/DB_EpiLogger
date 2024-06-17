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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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


class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourProjectTheme {
                RegisterActivityScreen()
            }
        }
    }
}

@Composable
fun RegisterActivityScreen() {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current // Get the current context

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

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
            text = "Register New User",
            fontSize = 35.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        BasicTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
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
                    Text("Email", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
                innerTextField()
            }
        )
        if (emailError) {
            Text(
                text = "Please enter a valid email",
                color = Color.Red,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
            )
        }

        BasicTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
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
        if (passwordError) {
            Text(
                text = "Please enter a valid password",
                color = Color.Red,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
            )
        }

        BasicTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = false
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
                if (confirmPassword.isEmpty()) {
                    Text("Confirm Password", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                }
                innerTextField()
            }
        )
        if (confirmPasswordError) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                var isValid = true
                if (email.isEmpty()) {
                    emailError = true
                    isValid = false
                }
                if (password.isEmpty()) {
                    passwordError = true
                    isValid = false
                }
                if (confirmPassword.isEmpty() || confirmPassword != password) {
                    confirmPasswordError = true
                    isValid = false
                }
                if (isValid) {
                    // Handle register
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1e3e7e))
        ) {
            Text(
                text = "Register",
                fontSize = 25.sp,
                color = Color.White,
                fontFamily = customFont
            )
        }

        Text(
            text = "Back to Login",
            fontSize = 24.sp,
            color = Color(0xFF2b4a84),
            fontFamily = customFont,
            modifier = Modifier
                .padding(top = 30.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterActivity() {
    YourProjectTheme {
        RegisterActivityScreen()
    }
}
