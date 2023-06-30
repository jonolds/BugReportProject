@file:Suppress("MissingSuperCall")
package com.jonolds.bugreportproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Stable
import com.jonolds.bugreportproject.ui.theme.ClubhouseTheme


@Stable
class MainComposeActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {

			ClubhouseTheme {
				
				MainNavHost()
				
			}
		}
	}
}
