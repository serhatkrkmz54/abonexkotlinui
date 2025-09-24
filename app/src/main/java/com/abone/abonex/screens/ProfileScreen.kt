package com.abone.abonex.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.abone.abonex.R
import com.abone.abonex.data.remote.dto.UserDto
import com.abone.abonex.data.remote.dto.UserProfileUpdateRequest
import com.abone.abonex.domain.enums.Gender
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.ProfileViewModel
import com.abone.abonex.ui.theme.ProfileSectionBg
import com.abone.abonex.ui.theme.redRose
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(uiState.isDeactivated) {
        if (uiState.isDeactivated) {
            navController.navigate(AppRoute.LOGIN_SCREEN) {
                popUpTo(0)
            }
        }
    }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            navController.navigate(AppRoute.LOGIN_SCREEN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.user != null -> {
                    ProfileContent(
                        user = uiState.user!!,
                        navController = navController,
                        onLogout = { viewModel.logout() },
                        onSave = { updateRequest -> viewModel.updateUserProfile(updateRequest) },
                        onDeactivate = { viewModel.deactivateAccount() }
                    )
                }
                uiState.error != null -> {
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileContent(
    user: UserDto,
    onLogout: () -> Unit,
    onSave: (UserProfileUpdateRequest) -> Unit,
    onDeactivate: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isInEditMode by remember { mutableStateOf(false) }

    var firstName by remember(user.firstName) { mutableStateOf(user.firstName) }
    var lastName by remember(user.lastName) { mutableStateOf(user.lastName) }
    var email by remember(user.email) { mutableStateOf(user.email) }
    var phone by remember(user.phoneNumber) { mutableStateOf(user.phoneNumber ?: "") }
    var dateOfBirth by remember(user.dateOfBirth) { mutableStateOf(user.dateOfBirth ?: "") }
    var gender by remember(user.gender) { mutableStateOf(user.gender ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDeactivationDialog by remember { mutableStateOf(false) }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Date(millis)
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dateOfBirth = formatter.format(selectedDate)
                    }
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

    if (showDeactivationDialog) {
        AlertDialog(
            onDismissRequest = { showDeactivationDialog = false },
            title = { Text("Hesabı Pasif Hale Getir", fontFamily = redRose) },
            text = { Text("Bu işlem sonrası tekrar giriş yapana kadar hesabınız pasif kalacaktır. Emin misiniz?", fontFamily = redRose) },
            confirmButton = {
                TextButton(onClick = {
                    onDeactivate()
                    showDeactivationDialog = false
                }) {
                    Text("Evet, Onaylıyorum", color = colorResource(id = R.color.logout_bg))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeactivationDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }


    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_bg),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        AsyncImage(
                            model = selectedImageUri ?: user.profileImageUrl ?: R.drawable.profile_default,
                            contentDescription = "Profil Resmi",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        if (isInEditMode) {
                            IconButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .size(32.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Profil Resmini Değiştir", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${user.firstName} ${user.lastName}", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = redRose)
                    Spacer(modifier = Modifier.height(8.dp))
                    AccountStatus(enabled = user.enabled)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                ProfileSectionCard(
                    title = "Profil Bilgileri",
                    icon = Icons.Default.Person,
                    action = {
                        if (isInEditMode) {
                            Row {
                                IconButton(onClick = {
                                    isInEditMode = false
                                    firstName = user.firstName
                                    lastName = user.lastName
                                    email = user.email
                                    phone = user.phoneNumber ?: ""
                                    dateOfBirth = user.dateOfBirth ?: ""
                                    gender = user.gender ?: ""
                                    selectedImageUri = null
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "İptal Et", tint = colorResource(R.color.logout_bg))
                                }
                                IconButton(onClick = {
                                    val finalImageUrl = user.profileImageUrl
                                    val updateRequest = UserProfileUpdateRequest(
                                        firstName = firstName,
                                        lastName = lastName,
                                        email = email,
                                        dateOfBirth = dateOfBirth.ifBlank { null },
                                        gender = gender.ifBlank { null },
                                        phoneNumber = phone.ifBlank { null },
                                        profileImageUrl = finalImageUrl
                                    )
                                    onSave(updateRequest)
                                    isInEditMode = false
                                }) {
                                    Icon(Icons.Default.Save, contentDescription = "Kaydet", tint = Color.Green)
                                }
                            }
                        } else {
                            IconButton(onClick = { isInEditMode = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Düzenle", tint = Color.White)
                            }
                        }
                    }
                ) {
                    ProfileInfoField(value = firstName, onValueChange = { firstName = it }, label = "Adınız", icon = Icons.Default.Badge, enabled = isInEditMode)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileInfoField(value = lastName, onValueChange = { lastName = it }, label = "Soyadınız", icon = Icons.Default.Badge, enabled = isInEditMode)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileInfoField(value = email, onValueChange = { email = it }, label = "Email Adresiniz", icon = Icons.Default.Email, enabled = isInEditMode)
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileInfoField(value = phone, onValueChange = { phone = it }, label = "Telefon Numaranız", icon = Icons.Default.PhoneAndroid, enabled = isInEditMode)
                    Spacer(modifier = Modifier.height(16.dp))

                    if(isInEditMode) {
                        Box(modifier = Modifier.clickable { showDatePicker = true }) {
                            ProfileInfoField(value = dateOfBirth, onValueChange = {}, label = "Doğum Tarihi (yyyy-MM-dd)", icon = Icons.Default.Cake, enabled = false)
                        }
                    } else {
                        ProfileInfoField(value = formatDate(dateOfBirth), onValueChange = {}, label = "Doğum Tarihiniz", icon = Icons.Default.Cake, enabled = false)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isInEditMode) {
                        ProfileGenderSelector(
                            selectedGenderBackendName = gender,
                            onGenderSelect = { gender = it }
                        )
                    } else {
                        ProfileInfoField(value = formatGender(gender), onValueChange = {}, label = "Cinsiyet", icon = Icons.Default.Wc, enabled = false)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                ProfileSectionCard(
                    title = "Bildirim Ayarları",
                    icon = Icons.Default.Notifications
                ) {
                    var isNotificationsEnabled by remember { mutableStateOf(true) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Anlık Bildirimler",
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = redRose,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = isNotificationsEnabled,
                            onCheckedChange = { isNotificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorResource(id = R.color.logout_bg),
                                checkedTrackColor = colorResource(id = R.color.logout_bg).copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ProfileSectionCard(
                    title = "Hesap Yönetimi",
                    icon = Icons.Default.ManageAccounts
                ) {
                    OutlinedButton(
                        onClick = { showDeactivationDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(id = R.color.logout_bg)),
                        border = BorderStroke(1.dp, colorResource(id = R.color.logout_bg))
                    ) {
                        Text("Hesabı Pasif Hale Getir", fontFamily = redRose)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.logout_bg),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Çıkış Yap"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Çıkış Yap", fontFamily = redRose)
                    }
                }
            }
        }
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Geri",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileGenderSelector(
    selectedGenderBackendName: String,
    onGenderSelect: (String) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedGenderDisplayName = formatGender(selectedGenderBackendName)

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        OutlinedTextField(
            value = selectedGenderDisplayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Cinsiyet", fontFamily = redRose) },
            leadingIcon = { Icon(Icons.Default.Wc, contentDescription = null, tint = Color.Gray) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(fontFamily = redRose, color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.logout_bg),
                focusedIndicatorColor = colorResource(id = R.color.logout_bg),
                unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(ProfileSectionBg)
        ) {
            Gender.entries.forEach { genderEnum ->
                DropdownMenuItem(
                    text = { Text(genderEnum.displayName, color = Color.White, fontFamily = redRose) },
                    onClick = {
                        onGenderSelect(genderEnum.name)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ProfileSectionBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = redRose,
                modifier = Modifier.weight(1f)
            )
            if (action != null) {
                action()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
private fun ProfileInfoField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontFamily = redRose) },
        leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = Color.Gray) },
        readOnly = !enabled,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = redRose,
            color = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        colors = if (enabled) {
            TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.logout_bg),
                focusedIndicatorColor = colorResource(id = R.color.logout_bg)
            )
        } else {
            TextFieldDefaults.colors(
                disabledTextColor = Color.White,
                disabledLabelColor = Color.Gray,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                disabledLeadingIconColor = Color.Gray
            )
        },
        enabled = enabled
    )
}

@Composable
private fun AccountStatus(enabled: Boolean) {
    val (text, color, icon) = if (enabled) {
        Triple("Aktif", Color(0xFF4CAF50), Icons.Default.CheckCircle)
    } else {
        Triple("Pasif", Color(0xFFF44336), Icons.Default.CheckCircle)
    }

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Durum İkonu",
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

private fun calculateAge(birthDate: Date): Int {
    val dob = Calendar.getInstance()
    dob.time = birthDate
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age
}

private fun formatDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) return "-"
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return try {
        val date: Date = inputFormat.parse(dateString) ?: return dateString
        val age = calculateAge(date)
        "${outputFormat.format(date)} ($age yaşında)"
    } catch (e: Exception) {
        dateString
    }
}
private fun formatGender(backendName: String?): String {
    if (backendName.isNullOrBlank()) return "Belirtilmemiş"
    return Gender.entries.find { it.name.equals(backendName, ignoreCase = true) }?.displayName ?: "Belirtilmemiş"
}

