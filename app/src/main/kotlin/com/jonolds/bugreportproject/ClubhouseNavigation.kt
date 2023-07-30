package com.jonolds.bugreportproject

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jonolds.bugreportproject.tests.customlazytest.CustomLazyTest
import com.jonolds.bugreportproject.tests.kindalazytest.KindaLazyTest
import com.jonolds.bugreportproject.ui.StartScreenCompose
import com.jonolds.bugreportproject.tests.trackstest.TracksTest
import com.jonolds.bugreportproject.tests.trackstest.TracksTestVM
import com.jonolds.bugreportproject.ui.components.NoRippleNoMinTouchSize
import com.jonolds.bugreportproject.tests.lazydragrowtest.LazyDragRowTest
import com.jonolds.bugreportproject.tests.lazydragrowtest.LazyDragRowVM
import com.jonolds.bugreportproject.tests.movablecontenttest.MovableTest
import com.jonolds.bugreportproject.tests.reotest.ReoTest
import com.jonolds.bugreportproject.tests.tabrowtest.TabRowTest
import com.jonolds.bugreportproject.tests.tabrowtest.TabRowTestVM


@Composable
fun MainNavHost() {
	
	val navController: NavHostController = rememberNavController()




	NavHost(
		navController = navController,
		startDestination = "startscreen",
		enterTransition = { EnterTransition.None },
		exitTransition = { ExitTransition.None },
		popEnterTransition = { EnterTransition.None },
		popExitTransition = { ExitTransition.None },
	) {

		composable(route = "startscreen") { _ ->

			StartScreenCompose(
				navigateToTracksTest = { navController.navigate("tracksTest") },
				navigateToLazyDragRowTest = { navController.navigate("lazyDragRowTest") },
				navigateToTabRowTest = { navController.navigate("tabRowTest") },
				navigateToKindaLazyTest = { navController.navigate("kindaLazyTest") },
				navigateToReoTest = { navController.navigate("reoTest") },
				navigateToMovableTest = { navController.navigate("movableTest")}
			)
		}
		
		composable("tracksTest") { navEntry ->
			val vm: TracksTestVM = viewModel(navEntry)
			NoRippleNoMinTouchSize {
				TracksTest(vm = vm)
			}
		}

		composable("lazyDragRowTest") { navEntry ->
			val vm: LazyDragRowVM = viewModel(navEntry)
			NoRippleNoMinTouchSize {
				LazyDragRowTest(vm = vm)
			}
		}

		composable("tabRowTest") { navEntry ->
			val vm: TabRowTestVM = viewModel(navEntry)
			NoRippleNoMinTouchSize {
				TabRowTest(vm = vm)
			}
		}


		composable("kindaLazyTest") { navEntry ->
			val vm: TracksTestVM = viewModel(navEntry)
			NoRippleNoMinTouchSize {
				KindaLazyTest(vm = vm)
			}
		}



		composable("reoTest") { navEntry ->
			val vm: TracksTestVM = viewModel(navEntry)
			NoRippleNoMinTouchSize {
				ReoTest(vm = vm)
			}
		}


		composable("movableTest") { navEntry ->
			val vm: TracksTestVM = viewModel(navEntry)
			NoRippleNoMinTouchSize {
				MovableTest(vm = vm)
			}
		}
	}
}
