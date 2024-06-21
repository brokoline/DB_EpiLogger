package com.example.designbuild_epilogger

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.designbuild_epilogger.ui.theme.YourProjectTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            YourProjectTheme {
                MainActivityScreen(auth)  { navigateToDashboard() }
            }
        }
    }
    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }
}

@Composable
fun MainActivityScreen(auth: FirebaseAuth, onLoginSuccess: () -> Unit) {
    val customFont = FontFamily(Font(R.font.alfa_slab_one_regular))
    val context = LocalContext.current
    val lifecycleScope = rememberCoroutineScope()
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
            text = "Group 8",
            fontSize = 20.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier
                .padding(bottom = 40.dp)
        )

        Text(
            text = "Welcome",
            fontSize = 44.sp,
            color = Color(0xFF1e3e7e),
            fontFamily = customFont,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                loginError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 6.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            label = { Text("E-mail", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                loginError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 6.dp),
            visualTransformation = PasswordVisualTransformation(),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            label = { Text("Password", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            singleLine = true
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
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    lifecycleScope.launch {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                                    onLoginSuccess()

                                } else {
                                    loginError = true
                                }
                            }
                    }
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




