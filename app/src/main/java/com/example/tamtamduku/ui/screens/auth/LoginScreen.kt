package com.example.tamtamduku.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tamtamduku.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.loginError) {
        uiState.loginError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)) { append("V") }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)) { append("O") }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)) { append("CA") }
                        },
                        fontSize = 44.sp,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Masuk sebagai Pencari Jasa",
                        style = TextStyle(color = MaterialTheme.colorScheme.outline, fontSize = 15.sp)
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Alamat Email", color = MaterialTheme.colorScheme.outline) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon", tint = MaterialTheme.colorScheme.outline) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Kata Sandi", color = MaterialTheme.colorScheme.outline) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon", tint = MaterialTheme.colorScheme.outline) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Text(
                                    text = if (isPasswordVisible) "HIDE" else "SHOW",
                                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = "Lupa Password?",
                            modifier = Modifier.clickable { },
                            style = TextStyle(color = MaterialTheme.colorScheme.outline, fontSize = 13.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = { viewModel.login(email, password, onLoginSuccess) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Masuk Sekarang",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Belum punya akun? ", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                        Text(
                            text = "Daftar di sini",
                            modifier = Modifier.clickable { onToRegister() },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
