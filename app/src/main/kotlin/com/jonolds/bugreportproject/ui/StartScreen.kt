package com.jonolds.bugreportproject.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonolds.bugreportproject.ui.theme.ClubhouseDarkColorScheme


@Composable
fun StartScreenCompose(
    navigateToTracksTest: () -> Unit,
    navigateToLazyDragRowTest: () -> Unit,
    navigateToTabRowTest: () -> Unit,
    navigateToKindaLazyTest: () -> Unit,
    navigateToReoTest: () -> Unit,
    navigateToMovableTest: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(ClubhouseDarkColorScheme.surface)
    ) {


        FilledTonalButton(
            shape = MaterialTheme.shapes.extraLarge,
            onClick = navigateToTracksTest,
            content = {
                Text(text = "Tracks Test", fontSize = 14.sp)
            },
            modifier = Modifier
                .padding(12.dp)
                .height(42.dp)
        )

        FilledTonalButton(
            shape = MaterialTheme.shapes.extraLarge,
            onClick = navigateToLazyDragRowTest,
            content = {
                Text(text = "LazyDragRow Test", fontSize = 14.sp)
            },
            modifier = Modifier
                .padding(12.dp)
                .height(42.dp)
        )

        FilledTonalButton(
            shape = MaterialTheme.shapes.extraLarge,
            onClick = navigateToTabRowTest,
            content = {
                Text(text = "TabRow Test", fontSize = 14.sp)
            },
            modifier = Modifier
                .padding(12.dp)
                .height(42.dp)
        )

        FilledTonalButton(
            shape = MaterialTheme.shapes.extraLarge,
            onClick = navigateToReoTest,
            content = {
                Text(text = "Reo Test", fontSize = 14.sp)
            },
            modifier = Modifier
                .padding(12.dp)
                .height(42.dp)
        )

        FilledTonalButton(
            shape = MaterialTheme.shapes.extraLarge,
            onClick = navigateToMovableTest,
            content = {
                Text(text = "Movable Test", fontSize = 14.sp)
            },
            modifier = Modifier
                .padding(12.dp)
                .height(42.dp)
        )

        FilledTonalButton(
            shape = MaterialTheme.shapes.extraLarge,
            onClick = navigateToKindaLazyTest,
            content = {
                Text(text = "KindaLazy Test", fontSize = 14.sp)
            },
            modifier = Modifier
                .padding(12.dp)
                .height(42.dp)
        )
    }
}


