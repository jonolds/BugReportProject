package com.jonolds.bugreportproject

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jonolds.bugreportproject.ui.StartScreenCompose
import com.jonolds.bugreportproject.ui.TracksTest
import com.jonolds.bugreportproject.ui.TracksTestVM
import com.jonolds.bugreportproject.ui.components.NoRippleNoMinTouchSize



@Composable
fun MainNavHost() {
	
	val navController: NavHostController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = "startscreen",
	) {

		composable(route = "startscreen") { _ ->
			StartScreenCompose { navController}

		}
		
		composable("trackstest") { navEntry ->
			
			val vm: TracksTestVM = viewModel(navEntry)
			
			NoRippleNoMinTouchSize {
				TracksTest(vm = vm)
			}
			
		}
	}
}
