package com.yourname.mllearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yourname.mllearn.navigation.AppNav
import com.yourname.mllearn.ui.theme.MLLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MLLearnTheme {
                AppNav()
            }
        }
    }
}
