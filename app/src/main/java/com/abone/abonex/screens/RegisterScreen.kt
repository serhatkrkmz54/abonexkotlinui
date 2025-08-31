package com.abone.abonex.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.R
import com.abone.abonex.domain.enums.Gender
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.RegisterViewModel
import com.abone.abonex.ui.theme.InputBorder
import com.abone.abonex.ui.theme.poppins
import com.abone.abonex.util.Resource
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isPasswordVisible by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val isButtonEnabled = remember(uiState) {
        uiState.firstName.isNotBlank() &&
                uiState.lastName.isNotBlank() &&
                uiState.email.isNotBlank() &&
                uiState.password.length >= 6 &&
                uiState.phoneNumber.isNotBlank() &&
                uiState.dateOfBirth != null &&
                uiState.gender.isNotBlank() &&
                uiState.registrationStatus !is Resource.Loading
    }

    LaunchedEffect(uiState.registrationStatus) {
        when (val status = uiState.registrationStatus) {
            is Resource.Success -> {
                navController.navigate(AppRoute.HOME_SCREEN) { popUpTo(0) }
            }
            is Resource.Error -> {
                status.message?.let {
                    scope.launch { snackbarHostState.showSnackbar(it) }
                }
            }
            else -> {}
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDateOfBirthChange(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("Tamam") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("İptal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Hesap Oluştur", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Geri", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_logo_800x),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                stringResource(R.string.register_first_text),
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CustomOutlinedTextField(value = uiState.firstName, onValueChange = viewModel::onFirstNameChange, placeholder = "Ad", leadingIcon = Icons.Default.Person)

            CustomOutlinedTextField(value = uiState.lastName, onValueChange = viewModel::onLastNameChange, placeholder = "Soyad", leadingIcon = Icons.Default.People)

            CustomOutlinedTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, placeholder = "Email", keyboardType = KeyboardType.Email, leadingIcon = Icons.Default.Email)

            CustomOutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = "Şifre",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "Şifreyi göster/gizle", tint = Color.White)
                    }
                }
            )

            CustomOutlinedTextField(value = uiState.phoneNumber, onValueChange = viewModel::onPhoneNumberChange, placeholder = "Telefon Numarası", keyboardType = KeyboardType.Phone, leadingIcon = Icons.Default.Phone)

            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            Box(modifier = Modifier.clickable { showDatePicker = true }) {
                CustomOutlinedTextField(
                    value = uiState.dateOfBirth?.format(dateFormatter) ?: "",
                    onValueChange = {},
                    placeholder = "Doğum Tarihi",
                    readOnly = true,
                    enabled = false,
                    leadingIcon = Icons.Default.CalendarToday
                )
            }

            GenderSelector(selectedGender = uiState.gender, onGenderSelect = viewModel::onGenderChange)

            Button(
                onClick = { viewModel.registerUser() },
                modifier = Modifier.fillMaxWidth().height(55.dp).padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6759FF),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF33334A),
                    disabledContentColor = Color.Gray
                ),
                enabled = isButtonEnabled
            ) {
                if (uiState.registrationStatus is Resource.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Kayıt Ol", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.White, fontSize = 14.sp, fontFamily = poppins, fontWeight = FontWeight.ExtraLight) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        readOnly = readOnly,
        enabled = enabled,
        leadingIcon = {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = null, tint = Color.Gray)
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = InputBorder,
            unfocusedContainerColor = InputBorder,
            disabledContainerColor = InputBorder,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color(0xFF6759FF),
            unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(selectedGender: String, onGenderSelect: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedGenderDisplayName = Gender.entries.find { it.name == selectedGender }?.displayName ?: "Cinsiyet Seçiniz"

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedGenderDisplayName,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Cinsiyet") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E2C),
                unfocusedContainerColor = Color(0xFF1E1E2C),
                disabledContainerColor = Color(0xFF1C1C29),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color(0xFF6759FF),
                unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f)
            )
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(Color(0xFF1E1E2C))
        ) {
            Gender.entries.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender.displayName, color = Color.White) },
                    onClick = {
                        onGenderSelect(gender.name)
                        isExpanded = false
                    }
                )
            }
        }
    }
}