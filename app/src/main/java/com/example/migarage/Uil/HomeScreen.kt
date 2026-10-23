package com.example.migarage.uil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.migarage.VehicleViewModel
import com.example.migarage.data.Vehicle
import com.example.migarage.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: VehicleViewModel,
    onLogout: () -> Unit
) {
    val vehicles by viewModel.vehicles.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Garaje", color = PurplePrimary, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        // Reemplaza Icons.Default.ExitToApp por:
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión", tint = PurpleDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = BlueAccent
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Vehículo", tint = BackgroundColor)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (vehicles.isEmpty()) {
                Text(
                    text = "No tienes vehículos guardados.\n¡Toca el botón + para agregar uno!",
                    modifier = Modifier.align(Alignment.Center),
                    color = PurpleDark,
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vehicles) { vehicle ->
                        VehicleCard(vehicle = vehicle, onDelete = { viewModel.deleteVehicle(vehicle) })
                    }
                }
            }
        }

        if (showDialog) {
            AddVehicleDialog(
                onDismiss = { showDialog = false },
                onAdd = { make, model, year, plate, mileage ->
                    viewModel.addVehicle(make, model, year, plate, mileage)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun VehicleCard(vehicle: Vehicle, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "${vehicle.make} ${vehicle.model} (${vehicle.year})", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PurplePrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Placa: ${vehicle.licensePlate}", fontSize = 14.sp)
                Text(text = "Kilometraje: ${vehicle.mileage} km", fontSize = 14.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddVehicleDialog(
    onDismiss: () -> Unit,
    onAdd: (make: String, model: String, year: Int, plate: String, mileage: Int) -> Unit
) {
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Vehículo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = make, onValueChange = { make = it }, label = { Text("Marca") })
                OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Modelo") })
                OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Año") })
                OutlinedTextField(value = plate, onValueChange = { plate = it }, label = { Text("Placa") })
                OutlinedTextField(value = mileage, onValueChange = { mileage = it }, label = { Text("Kilometraje") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onAdd(
                    make,
                    model,
                    year.toIntOrNull() ?: 2024,
                    plate,
                    mileage.toIntOrNull() ?: 0
                )
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}