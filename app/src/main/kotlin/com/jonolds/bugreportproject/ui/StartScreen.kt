package com.jonolds.bugreportproject.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun StartScreenCompose(
    getNavController: () -> NavHostController
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Column {

            FilledTonalButton(
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.height(42.dp),
                onClick = {
                    getNavController().navigate("trackstest")
                },
                content = {
                    Text(text = "Start", fontSize = 14.sp)
                }
            )

        }
    }
}


