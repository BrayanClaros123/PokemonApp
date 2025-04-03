package com.example.pokeapp.ui.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PokemonTypePill(type: String) {
    val color = PokemonTypeColors[type.toLowerCase()] ?: Color.Gray
    Text(
        text = type.capitalize(),
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}