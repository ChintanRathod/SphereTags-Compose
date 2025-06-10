package com.chintanrathod.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.chintanrathod.example.ui.theme.SphereTagsTheme
import com.chintanrathod.spheretags.SphereTags

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SphereTagsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.background(color = Color.White)
                    ) {
                        val context = LocalContext.current

                        val tagsList = ArrayList<String>()
                        for (i in 0 until 40) {
                            tagsList.add("Tag $i")
                        }
                        SphereTags(
                            radius = 400.00,
                            tags = tagsList,
                            textColor = android.graphics.Color.BLACK,
                            textSize = 12.sp,
                            selectedTag = {
                                Toast.makeText(context, "$it selected", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SphereTagsTheme {
        Greeting("Android")
    }
}