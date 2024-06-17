package com.example.designbuild_epilogger.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.designbuild_epilogger.R

val customFontFamily = FontFamily(
    Font(R.font.alfa_slab_one_regular, FontWeight.Normal)
)

val AppTypography = Typography( // Omdøb denne linje til AppTypography
    displayLarge = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp
    ),
    // Definer andre tekststile her
)
