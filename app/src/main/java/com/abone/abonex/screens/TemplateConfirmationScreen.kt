package com.abone.abonex.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abone.abonex.components.form.FormDropdownField
import com.abone.abonex.components.form.FormIconTextField
import com.abone.abonex.components.form.StyledCheckbox
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.TemplateConfirmViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateConfirmationScreen(
    navController: NavController,
    viewModel: TemplateConfirmViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var cardName by remember { mutableStateOf("") }
    var cardLastFour by remember { mutableStateOf("") }
    val reminderOptions = mapOf(
        "1 Gün Önce" to 1, "2 Gün Önce" to 2, "3 Gün Önce" to 3, "4 Gün Önce" to 4, "5 Gün Önce" to 5, "6 Gün Önce" to 6, "7 Gün Önce" to 7
    )
    var selectedReminder by remember { mutableStateOf("1 Gün Önce") }
    var firstPaymentMade by remember { mutableStateOf(true) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack(AppRoute.HOME_SCREEN, inclusive = false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aboneliği Onayla") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
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
            Spacer(Modifier.height(8.dp))
            Text("Seçilen Plan ID: ${state.planId}")
            Text("Lütfen aboneliğinizi başlatmak için kişisel bilgilerinizi girin.")

            DatePickerField(label = "Başlangıç Tarihi", selectedDate = startDate, onDateSelected = { startDate = it })
            DatePickerField(label = "Bitiş Tarihi (İsteğe Bağlı)", selectedDate = endDate, onDateSelected = { endDate = it })

            StyledCheckbox(
                checked = firstPaymentMade,
                onCheckedChange = { firstPaymentMade = it },
                text = "Bu aboneliğin ödemesini yaptım."
            )

            FormDropdownField(label = "Hatırlatma", leadingIcon = Icons.Default.Notifications, options = reminderOptions.keys.toList(), selectedOption = selectedReminder, onOptionSelected = { selectedReminder = it })
            FormIconTextField(value = cardName, onValueChange = { cardName = it }, label = "Kart Adı", leadingIcon = Icons.Default.CreditCard)
            FormIconTextField(
                value = cardLastFour,
                onValueChange = { newText ->
                    if (newText.length <= 4 && newText.all { it.isDigit() }) { cardLastFour = newText }
                },
                label = "Son 4 Hane",
                leadingIcon = Icons.Default.Password,
                keyboardType =  KeyboardType.Number
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    startDate?.let { sDate ->
                        viewModel.createSubscription(
                            startDate = sDate,
                            endDate = endDate,
                            cardName = cardName,
                            cardLastFour = cardLastFour,
                            notificationDays = reminderOptions[selectedReminder] ?: 5,
                            firstPaymentMade = firstPaymentMade
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !state.isLoading && startDate != null
            ) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp)) else Text("Aboneliği Başlat")
            }

            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top=8.dp))
            }
        }
    }
}