package com.abone.abonex.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.components.form.FormDropdownField
import com.abone.abonex.components.form.FormIconTextField
import com.abone.abonex.data.remote.dto.subs.CreateSubscriptionRequest
import com.abone.abonex.ui.features.AddSubscriptionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(
    navController: NavController,
    viewModel: AddSubscriptionViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var cardLastFour by remember { mutableStateOf("") }

    val cycleOptions = listOf("Aylık", "Yıllık")
    var selectedCycle by remember { mutableStateOf(cycleOptions[0]) }

    val currencyOptions = listOf("TRY", "USD", "EUR")
    var selectedCurrency by remember { mutableStateOf(currencyOptions[0]) }

    val reminderOptions = mapOf(
        "1 Gün Önce" to 1, "2 Gün Önce" to 2, "3 Gün Önce" to 3, "4 Gün Önce" to 4, "5 Gün Önce" to 5
    )
    var selectedReminder by remember { mutableStateOf("1 Gün Önce") }

    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manuel Abonelik Ekle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            FormIconTextField(value = name, onValueChange = { name = it }, label = "Abonelik Adı", leadingIcon = Icons.AutoMirrored.Filled.Notes)
            FormIconTextField(value = amount, onValueChange = { amount = it }, label = "Tutar", leadingIcon = Icons.Default.AttachMoney, keyboardType = KeyboardType.Number)
            FormDropdownField(label = "Para Birimi", leadingIcon = Icons.Default.CurrencyLira, options = currencyOptions, selectedOption = selectedCurrency, onOptionSelected = { selectedCurrency = it })
            FormDropdownField(label = "Ödeme Periyodu", leadingIcon = Icons.Default.Loop, options = cycleOptions, selectedOption = selectedCycle, onOptionSelected = { selectedCycle = it })

            DatePickerField(label = "Başlangıç Tarihi", selectedDate = startDate, onDateSelected = { startDate = it })
            DatePickerField(label = "Bitiş Tarihi (İsteğe Bağlı)", selectedDate = endDate, onDateSelected = { endDate = it })

            FormDropdownField(label = "Hatırlatma", leadingIcon = Icons.Default.Notifications, options = reminderOptions.keys.toList(), selectedOption = selectedReminder, onOptionSelected = { selectedReminder = it })

            FormIconTextField(value = cardName, onValueChange = { cardName = it }, label = "Kart Adı", leadingIcon = Icons.Default.CreditCard)
            FormIconTextField(value = cardLastFour, onValueChange = {
                newText ->
                if (newText.length <= 4 && newText.all { it.isDigit() }) {
                    cardLastFour = newText
                }
            }, label = "Son 4 Hane", leadingIcon = Icons.Default.Password, keyboardType = KeyboardType.Number)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val billingCycleToSend = if (selectedCycle == "Aylık") "MONTHLY" else "YEARLY"
                    val request = CreateSubscriptionRequest(
                        subscriptionName = name,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        currency = selectedCurrency,
                        billingCycle = billingCycleToSend,
                        startDate = startDate.toString(),
                        endDate = endDate?.toString(),
                        cardName = cardName.takeIf { it.isNotBlank() },
                        cardLastFourDigits = cardLastFour.takeIf { it.isNotBlank() },
                        notificationDaysBefore = reminderOptions[selectedReminder] ?: 5
                    )
                    viewModel.createSubscription(request)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !state.isLoading && name.isNotBlank() && amount.isNotBlank() && startDate != null
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Kaydet")
                }
            }
            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    selectedDate?.let {
        calendar.set(it.year, it.monthValue - 1, it.dayOfMonth)
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    LaunchedEffect(selectedDate) {
        selectedDate?.let {
            datePickerDialog.updateDate(it.year, it.monthValue - 1, it.dayOfMonth)
        }
    }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val dateText = selectedDate?.format(formatter) ?: ""
    val customBorderColor = Color(0xFF232330)

    Box(
        modifier = Modifier.clickable { datePickerDialog.show() }
    ) {
        OutlinedTextField(
            value = dateText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = null) },
            shape = RoundedCornerShape(16.dp),

            enabled = false,

            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = customBorderColor,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLeadingIconColor = Color.Gray,
                disabledLabelColor = Color.Gray,

                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = customBorderColor
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}