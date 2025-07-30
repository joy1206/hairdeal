package com.example.hairdeal.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.hairdeal.R
import androidx.compose.ui.graphics.Color // Color 클래스 임포트

// Color.kt 파일에 이미 정의되어 있는 색상들을 임포트합니다.
import com.example.hairdeal.ui.theme.Purple80
import com.example.hairdeal.ui.theme.PurpleGrey80
import com.example.hairdeal.ui.theme.Pink80
import com.example.hairdeal.ui.theme.Purple40
import com.example.hairdeal.ui.theme.PurpleGrey40
import com.example.hairdeal.ui.theme.Pink40


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// Noto Sans KR 폰트 패밀리 정의
val NotoSansKr = FontFamily(
    Font(R.font.notosanskr_regular, FontWeight.Normal)
)

// MaterialTheme에서 사용되는 텍스트 스타일들을 정의합니다.
// 이 Typography 객체는 Type.kt (또는 Typography.kt) 파일에서 정의되어야 합니다.
// Theme.kt에서는 이 정의를 제거하고 Type.kt의 Typography를 사용하도록 합니다.
// val Typography = Typography( ... ) // 이 정의는 제거됩니다.


@Composable
fun HairDealTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Type.kt 파일에 정의된 Typography를 사용합니다.
        content = content
    )
}
