package com.ad.brainshopchatbot.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ad.brainshopchatbot.R

// Set of Material typography styles to start with
val Poppins = FontFamily(Font(R.font.poppins))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
//     Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    displayLarge = TextStyle(fontFamily = Poppins),
    displayMedium = TextStyle(fontFamily = Poppins),
    displaySmall = TextStyle(fontFamily = Poppins),
    headlineLarge = TextStyle(fontFamily = Poppins),
    headlineMedium = TextStyle(fontFamily = Poppins),
    headlineSmall = TextStyle(fontFamily = Poppins),
    titleMedium = TextStyle(fontFamily = Poppins),
    titleSmall = TextStyle(fontFamily = Poppins),
    bodyMedium = TextStyle(fontFamily = Poppins),
    bodySmall = TextStyle(fontFamily = Poppins),
    labelLarge = TextStyle(fontFamily = Poppins),
    labelMedium = TextStyle(fontFamily = Poppins)
)