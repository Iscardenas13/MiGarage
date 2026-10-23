package com.example.migarage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.migarage.data.AppDatabase
import com.example.migarage.data.Vehicle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VehicleViewModel(application: Application) : AndroidViewModel(application) {

    private val vehicleDao = AppDatabase.getDatabase(application).vehicleDao()
    private val auth = FirebaseAuth.getInstance()

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        val currentUserId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            vehicleDao.getVehiclesForUser(currentUserId).collect { list ->
                _vehicles.value = list
            }
        }
    }

    fun addVehicle(make: String, model: String, year: Int, licensePlate: String, mileage: Int) {
        val currentUserId = auth.currentUser?.uid ?: return
        val newVehicle = Vehicle(
            make = make,
            model = model,
            year = year,
            licensePlate = licensePlate,
            mileage = mileage,
            userId = currentUserId
        )
        viewModelScope.launch {
            vehicleDao.insertVehicle(newVehicle)
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.deleteVehicle(vehicle)
        }
    }

    fun logout() {
        auth.signOut()
    }
}