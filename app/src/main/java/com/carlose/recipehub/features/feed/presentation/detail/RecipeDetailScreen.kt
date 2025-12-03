package com.carlose.recipehub.features.feed.presentation.detail

// ... imports existentes ...
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.carlose.recipehub.core.model.MealType
import com.carlose.recipehub.core.network.CommentResponseDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    onBackClick: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val recipeDetail by viewModel.recipeDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val commentText by viewModel.commentText.collectAsState()
    val isSendingComment by viewModel.isSendingComment.collectAsState()

    // Estados del Planificador
    val showPlannerDialog by viewModel.showPlannerDialog.collectAsState()
    val isAddingToPlan by viewModel.isAddingToPlan.collectAsState()

    if (showPlannerDialog) {
        AddToPlannerDialog(
            onDismiss = viewModel::closePlannerDialog,
            onConfirm = { date, type -> viewModel.addToPlan(date, type) },
            isLoading = isAddingToPlan
        )
    }

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (recipeDetail != null) {
            val detail = recipeDetail!!
            val recipe = detail.recipe

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = recipe.imageUrl ?: "",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

                    // Botón Atrás
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(top = 48.dp, start = 16.dp)
                            .align(Alignment.TopStart)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }

                    // --- NUEVO: Botón Planificador ---
                    IconButton(
                        onClick = viewModel::openPlannerDialog,
                        modifier = Modifier
                            .padding(top = 48.dp, end = 16.dp)
                            .align(Alignment.TopEnd)
                            .background(Color(0xFF6200EE), RoundedCornerShape(50)) // Morado para resaltar
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = "Agendar", tint = Color.White)
                    }
                }

                // ... (El resto del contenido: Título, Chips, Descripción, Ingredientes, Pasos, Comentarios)
                // ... (Copia el resto del código de la versión anterior aquí, no cambia nada abajo)

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = recipe.title, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Por ${recipe.authorName}", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        InfoChip(icon = Icons.Default.Schedule, text = "${recipe.preparationTimeMinutes} min")
                        Spacer(modifier = Modifier.width(16.dp))
                        InfoChip(icon = Icons.Default.Restaurant, text = "${recipe.portions} porciones")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Descripción", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = recipe.description, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Ingredientes", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    detail.ingredients.forEach { ingredient ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("• ", color = Color(0xFF6200EE), fontWeight = FontWeight.Bold)
                            Text(text = "${ingredient.name} (${ingredient.quantity})", color = Color.LightGray)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Preparación", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    detail.steps.forEach { step ->
                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(text = "${step.stepNumber}.", color = Color(0xFF6200EE), fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
                            Text(text = step.description, color = Color.LightGray)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Divider(color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "Comentarios (${comments.size})", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = viewModel::onCommentTextChanged,
                            placeholder = { Text("Escribe un comentario...") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Color(0xFF6200EE), unfocusedBorderColor = Color.Gray)
                        )
                        IconButton(onClick = { viewModel.sendComment() }, enabled = !isSendingComment && commentText.isNotBlank()) {
                            if (isSendingComment) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White) else Icon(Icons.Default.Send, contentDescription = "Enviar", tint = if (commentText.isNotBlank()) Color(0xFF6200EE) else Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    comments.forEach { comment -> CommentItem(comment) }
                }
            }
        }
    }
}

// --- Componentes Auxiliares ---

@Composable
fun AddToPlannerDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, MealType) -> Unit,
    isLoading: Boolean
) {
    // Estado local del diálogo
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Agendar Comida", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Selecciona una fecha:", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Selector de Fecha Simplificado (Botones para Hoy/Mañana/Pasado)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val today = LocalDate.now()
                    DateChip(date = today, isSelected = selectedDate == today) { selectedDate = today }
                    DateChip(date = today.plusDays(1), isSelected = selectedDate == today.plusDays(1)) { selectedDate = today.plusDays(1) }
                    DateChip(date = today.plusDays(2), isSelected = selectedDate == today.plusDays(2)) { selectedDate = today.plusDays(2) }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Tipo de comida:", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Chips para tipo de comida
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MealTypeChip("Desayuno", MealType.BREAKFAST, selectedMealType) { selectedMealType = it }
                    MealTypeChip("Almuerzo", MealType.LUNCH, selectedMealType) { selectedMealType = it }
                    MealTypeChip("Cena", MealType.DINNER, selectedMealType) { selectedMealType = it }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedDate, selectedMealType) },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
                } else {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}

@Composable
fun DateChip(date: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM")
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF6200EE) else Color(0xFF2C2C2C))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = date.format(formatter), color = Color.White, fontSize = 12.sp)
    }
}

@Composable
fun MealTypeChip(label: String, type: MealType, selectedType: MealType, onClick: (MealType) -> Unit) {
    val isSelected = type == selectedType
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF6200EE) else Color(0xFF2C2C2C))
            .clickable { onClick(type) }
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(text = label, color = Color.White, fontSize = 12.sp)
    }
}

// ... InfoChip y CommentItem (que ya tenías) ...
@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.padding(end = 4.dp))
        Text(text = text, color = Color.White)
    }
}

@Composable
fun CommentItem(comment: CommentResponseDto) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = comment.authorName.first().uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = comment.authorName, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = comment.text, color = Color.LightGray, modifier = Modifier.padding(start = 40.dp))
    }
}