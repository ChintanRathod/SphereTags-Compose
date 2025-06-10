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
import kotlin.random.Random

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

                        val tagsList = generateRandomSyllableNames(40)

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

fun generateRandomSyllableNames(count: Int): List<String> {
    val prefixes = listOf("An", "El", "Ka", "La", "Mi", "Nor", "Pen", "Quen", "Sar")
    val middles = listOf("dra", "li", "ton", "vian", "mar", "ric", "lyn", "beth", "der")
    val suffixes = listOf("a", "o", "lee", "e", "ian", "ia", "son", "ford")

    val randomNames = mutableListOf<String>()
    repeat(count) {
        val name = StringBuilder()
        name.append(prefixes.random(Random)) // Use random() for collection
        if (Random.nextBoolean()) { // Sometimes add a middle part
            name.append(middles.random(Random))
        }
        name.append(suffixes.random(Random))
        randomNames.add(name.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }) // Ensure first letter is capitalized
    }
    return randomNames
}