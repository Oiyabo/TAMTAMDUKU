package com.example.tamtamduku.features.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.tamtamduku.R

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onToLogin: () -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var emailOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val pleaseCompleteDataMsg = stringResource(R.string.please_complete_data)

    val primaryColor = MaterialTheme.colorScheme.primary
    val bgColor = MaterialTheme.colorScheme.background
    val onBgColor = MaterialTheme.colorScheme.onBackground
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outline

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo placeholder
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = "Logo",
                    tint = primaryColor,
                    modifier = Modifier.size(64.dp)
                )
                
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = onBgColor, fontWeight = FontWeight.Normal)) { append(stringResource(R.string.voca)) }
                    },
                    fontSize = 20.sp,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.create_new_account),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = onBgColor
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.register_to_start),
                    style = TextStyle(color = onBgColor, fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Nama Lengkap Input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.full_name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = onBgColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        placeholder = { Text(stringResource(R.string.enter_full_name), color = onSurfaceColor.copy(alpha = 0.5f), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = outlineColor,
                            unfocusedBorderColor = outlineColor,
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Email Input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.email_or_phone),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = onBgColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = emailOrPhone,
                        onValueChange = { emailOrPhone = it },
                        placeholder = { Text(stringResource(R.string.email_example), color = onSurfaceColor.copy(alpha = 0.5f), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = outlineColor,
                            unfocusedBorderColor = outlineColor,
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Password Input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.password),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = onBgColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text(stringResource(R.string.enter_password_placeholder), color = onSurfaceColor.copy(alpha = 0.5f), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = outlineColor,
                            unfocusedBorderColor = outlineColor,
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                
                // Confirm Password Input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.confirm_password),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = onBgColor,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = konfirmasiPassword,
                        onValueChange = { konfirmasiPassword = it },
                        placeholder = { Text(stringResource(R.string.enter_password_placeholder), color = onSurfaceColor.copy(alpha = 0.5f), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = outlineColor,
                            unfocusedBorderColor = outlineColor,
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor
                        ),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (nama.isNotEmpty() && emailOrPhone.isNotEmpty() && password.isNotEmpty() && password == konfirmasiPassword) {
                            val email = if (emailOrPhone.contains("@")) emailOrPhone else ""
                            val phone = if (!emailOrPhone.contains("@")) emailOrPhone else ""
                            viewModel.register(nama, email, phone, onRegisterSuccess)
                        } else {
                            Toast.makeText(context, pleaseCompleteDataMsg, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Text(
                        text = stringResource(R.string.join),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.already_have_account), fontSize = 14.sp, color = onBgColor, fontWeight = FontWeight.Bold)
                    Text(
                        text = stringResource(R.string.login),
                        modifier = Modifier.clickable { onToLogin() },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                }
            }
        }
    }
}
