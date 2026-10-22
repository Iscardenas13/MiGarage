package com.example.migarage.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val make: String,       // Marca (ej. Toyota, Ford)
    val model: String,      // Modelo (ej. Corolla, Mustang)
    val year: Int,          // Año (ej. 2022)
    val licensePlate: String, // Placa
    val mileage: Int,       // Kilometraje
    val userId: String      // ID del usuario de Firebase
)