package com.example.schoolinventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.schoolinventoryapp.ui.theme.SchoolInventoryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SchoolInventoryAppTheme {
                SchoolInventoryApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SchoolInventoryAppPreview() {
    SchoolInventoryAppTheme {
        SchoolInventoryApp()
    }
}