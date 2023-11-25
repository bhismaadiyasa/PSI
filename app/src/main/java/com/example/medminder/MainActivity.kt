package com.example.medminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Add
import androidx.compose.material3.icons.filled.Delete
import androidx.compose.material3.icons.filled.Edit
import androidx.compose.material3.ui.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.medminder.ui.theme.MedminderTheme

// Data Model
data class Medicine(val id: Int, val name: String, val dosage: String, val schedule: String)

// Sample Data
val sampleMedicines = listOf(
    Medicine(1, "Paracetamol", "500mg", "Morning"),
    Medicine(2, "Aspirin", "300mg", "Afternoon"),
    Medicine(3, "Ibuprofen", "200mg", "Evening")
)

// Main Function
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedminderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Medicines Screen
                    MedicinesScreen()
                }
            }
        }
    }
}

// Medicines Screen
@Composable
fun MedicinesScreen() {
    var medicines by remember { mutableStateOf(sampleMedicines) }
    var isAddingMedicine by remember { mutableStateOf(false) }
    var isEditingMedicine by remember { mutableStateOf(false) }
    var selectedMedicine by remember { mutableStateOf(Medicine(0, "", "", "")) }
    var medicineName by remember { mutableStateOf("") }
    var medicineDosage by remember { mutableStateOf("") }
    var medicineSchedule by remember { mutableStateOf("") }

    Column {
        // Medicines List
        MedicinesList(
            medicines = medicines,
            onEditClick = { medicine ->
                selectedMedicine = medicine
                medicineName = medicine.name
                medicineDosage = medicine.dosage
                medicineSchedule = medicine.schedule
                isEditingMedicine = true
            },
            onDeleteClick = { medicine ->
                medicines = medicines.filter { it.id != medicine.id }
            }
        )

        // Add/Edit Medicine Section
        AddEditMedicineSection(
            isAddingMedicine = isAddingMedicine,
            isEditingMedicine = isEditingMedicine,
            medicineName = medicineName,
            medicineDosage = medicineDosage,
            medicineSchedule = medicineSchedule,
            onNameChange = { medicineName = it },
            onDosageChange = { medicineDosage = it },
            onScheduleChange = { medicineSchedule = it },
            onAddClick = {
                medicines = medicines + Medicine(
                    id = medicines.size + 1,
                    name = medicineName,
                    dosage = medicineDosage,
                    schedule = medicineSchedule
                )
                resetAddEditState()
            },
            onEditClick = {
                medicines = medicines.map {
                    if (it.id == selectedMedicine.id) {
                        Medicine(selectedMedicine.id, medicineName, medicineDosage, medicineSchedule)
                    } else {
                        it
                    }
                }
                resetAddEditState()
            },
            onCancelClick = {
                resetAddEditState()
            }
        )
    }
}

// Medicines List
@Composable
fun MedicinesList(
    medicines: List<Medicine>,
    onEditClick: (Medicine) -> Unit,
    onDeleteClick: (Medicine) -> Unit
) {
    LazyColumn {
        items(medicines) { medicine ->
            MedicinesListItem(
                medicine = medicine,
                onEditClick = { onEditClick(medicine) },
                onDeleteClick = { onDeleteClick(medicine) }
            )
        }
    }
}

// Medicines List Item
@Composable
fun MedicinesListItem(
    medicine: Medicine,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ListItem(
        text = { Text(text = medicine.name) },
        secondaryText = { Text(text = "Dosage: ${medicine.dosage}, Schedule: ${medicine.schedule}") },
        icon = {
            IconButton(onClick = { onEditClick() }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        },
        trailing = {
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    )
}

// Add/Edit Medicine Section
@Composable
fun AddEditMedicineSection(
    isAddingMedicine: Boolean,
    isEditingMedicine: Boolean,
    medicineName: String,
    medicineDosage: String,
    medicineSchedule: String,
    onNameChange: (String) -> Unit,
    onDosageChange: (String) -> Unit,
    onScheduleChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    // Check if it's in adding or editing state
    if (isAddingMedicine || isEditingMedicine) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Medicine Name Input
            OutlinedTextField(
                value = medicineName,
                onValueChange = { onNameChange(it) },
                label = { Text("Medicine Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Medicine Dosage Input
            OutlinedTextField(
                value = medicineDosage,
                onValueChange = { onDosageChange(it) },
                label = { Text("Dosage") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Medicine Schedule Input
            OutlinedTextField(
                value = medicineSchedule,
                onValueChange = { onScheduleChange(it) },
                label = { Text("Schedule") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Add/Edit Button
                Button(
                    onClick = {
                        if (isAddingMedicine) {
                            onAddClick()
                        } else if (isEditingMedicine) {
                            onEditClick()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(
                        text = if (isAddingMedicine) "Add" else "Update",
                        style = MaterialTheme.typography.button
                    )
                }

                // Cancel Button
                Button(
                    onClick = { onCancelClick() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(text = "Cancel", style = MaterialTheme.typography.button)
                }
            }
        }
    } else {
        // Floating Action Button to Add Medicine
        FloatingActionButton(
            onClick = { onAddClick() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

// Reset Add/Edit Medicine State
fun resetAddEditState(
    isAddingMedicine: MutableState<Boolean>,
    isEditingMedicine: MutableState<Boolean>,
    medicineName: MutableState<String>,
    medicineDosage: MutableState<String>,
    medicineSchedule: MutableState<String>,
) {
    isAddingMedicine.value = false
    isEditingMedicine.value = false
    medicineName.value = ""
    medicineDosage.value = ""
    medicineSchedule.value = ""
}
