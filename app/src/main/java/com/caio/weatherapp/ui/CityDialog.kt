package com.caio.weatherapp.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CityDialog(
    onDismiss: () -> Unit,
    onConfirm: (city: String) -> Unit,
    viewModel: CityDialogViewModel
) {
    val activity = LocalActivity.current as Activity

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Adicionar cidade favorita:")
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Nome da cidade") },
                    value = viewModel.query ?: "",
                    onValueChange = {
                        viewModel.onQueryChanged(it)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                    Text("Carregando sugestões...")
                } else if (viewModel.query.isNullOrBlank()) {
                    Text("Digite uma cidade")
                } else if ((viewModel.query ?: "").length < 3) {
                    Text("Digite pelo menos 3 caracteres")
                } else if (viewModel.suggestions.isEmpty()) {
                    Text("Nenhuma cidade encontrada")
                }

                LazyColumn {
                    items(viewModel.suggestions) { city ->
                        ListItem(
                            headlineContent = { Text(city.name ?: "") },
                            modifier = Modifier.clickable {
                                onConfirm(city.name ?: "")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val city = viewModel.query ?: ""

                        onConfirm(city)

                        Toast.makeText(
                            activity,
                            "Adicionou $city!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}