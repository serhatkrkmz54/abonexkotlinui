package com.abone.abonex.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.abone.abonex.domain.model.Plan
import com.abone.abonex.domain.model.Template
import com.abone.abonex.navigation.AppRoute
import com.abone.abonex.ui.features.AllSubscriptionsViewModel
import kotlinx.coroutines.launch
import com.abone.abonex.R
import com.abone.abonex.di.NetworkModule.BASE_URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllSubscriptionsScreen(
    navController: NavController,
    viewModel: AllSubscriptionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showPlanSheet by remember { mutableStateOf(false) }
    var selectedTemplate by remember { mutableStateOf<Template?>(null) }
    val scope = rememberCoroutineScope()

    if (showPlanSheet && selectedTemplate != null) {
        ModalBottomSheet(
            onDismissRequest = { showPlanSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            PlanSelectionSheetContent(
                template = selectedTemplate!!,
                onPlanSelected = { planId ->
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showPlanSheet = false
                            navController.navigate("${AppRoute.TEMPLATE_CONFIRM_SCREEN}/$planId")
                        }
                    }
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hazır Abonelik Seç", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Abonelik ara...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Hata: ${state.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            CustomSubscriptionItem(
                                onClick = { navController.navigate(AppRoute.ADD_SUBSCRIPTION_SCREEN) }
                            )
                        }
                        items(state.displayedTemplates) { template ->
                            TemplateSubscriptionItem(
                                template = template,
                                onAddClick = {
                                    selectedTemplate = template
                                    showPlanSheet = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomSubscriptionItem(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F2E))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Manuel Abonelik Oluştur", Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.SemiBold)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White)
        }
    }
}

@Composable
private fun TemplateSubscriptionItem(template: Template, onAddClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F2E))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = if (template.logoUrl != null) BASE_URL+template.logoUrl else null,
                contentDescription = template.name,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.default_brand),
                error = painterResource(id = R.drawable.ic_error_image)
                )
            Spacer(Modifier.width(16.dp))
            Text(template.name, Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Planları Gör", tint = Color.White)
            }
        }
    }
}

@Composable
private fun PlanSelectionSheetContent(template: Template, onPlanSelected: (planId: Long) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(model = template.logoUrl, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "${template.name} Planları", style = MaterialTheme.typography.titleLarge)
        }
        HorizontalDivider()
        LazyColumn {
            items(template.plans) { plan ->
                PlanSelectItem(plan = plan, onClick = { onPlanSelected(plan.id) })
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun PlanSelectItem(plan: Plan, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(plan.name, style = MaterialTheme.typography.bodyLarge)
        Text(
            "${String.format("%.2f", plan.amount)} ${getCurrencySymbol(plan.currency)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun getCurrencySymbol(currency: String): String {
    return when (currency.uppercase()) {
        "TRY" -> "₺"; "USD" -> "$"; "EUR" -> "€"; else -> currency
    }
}