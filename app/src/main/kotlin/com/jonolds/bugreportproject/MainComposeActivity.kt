@file:Suppress("MissingSuperCall")
package com.jonolds.bugreportproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import com.jonolds.bugreportproject.ui.components.rememberSystemUiController
import com.jonolds.bugreportproject.ui.theme.ClubhouseDarkColorScheme
import com.jonolds.bugreportproject.ui.theme.ClubhouseTheme


@Stable
class MainComposeActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			
			val uiControl = rememberSystemUiController()
			
			DisposableEffect(uiControl) {
				uiControl.setNavigationBarColor(
					color = ClubhouseDarkColorScheme.surface,
					darkIcons = false
				)
				
				onDispose {}
			}

			ClubhouseTheme {
				
				MainNavHost()
				
			}
		}
	}
}
