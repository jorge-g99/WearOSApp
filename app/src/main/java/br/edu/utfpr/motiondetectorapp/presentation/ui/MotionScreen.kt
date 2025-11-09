package br.edu.utfpr.motiondetectorapp.presentation.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import kotlinx.coroutines.delay

@Composable
fun MotionScreen(
    magnitude: Double,
    status: String,
    mockEnabled: Boolean,
    onToggleMock: () -> Unit
) {
    val background = Brush.verticalGradient(
        listOf(Color.Black, Color(0xFF101010))
    )

    val statusColor = when (status) {
        "MOVIMENTO FORTE" -> Color(0xFFFF3D00)
        "MOVIMENTO" -> Color(0xFF00E676)
        "PARADO" -> Color(0xFF9E9E9E)
        else -> Color(0xFF424242)
    }

    var scale by remember { mutableStateOf(1f) }
    LaunchedEffect(status) {
        if (status.contains("MOVIMENTO")) {
            scale = 1.15f
            delay(180)
            scale = 1f
        }
    }
    val animatedScale by animateFloatAsState(scale)

    Scaffold(
        timeText = { TimeText(modifier = Modifier.padding(top = 8.dp)) },
        vignette = { Vignette(VignettePosition.TopAndBottom) },
        modifier = Modifier.background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(top = 28.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detector de Movimento",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                CompactChip(
                    onClick = onToggleMock,
                    label = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (mockEnabled) "Modo Simulação" else "Sensor Real",
                                fontSize = 9.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = if (mockEnabled) Color(0xFF1565C0) else Color(0xFF2E7D32)
                    ),
                    modifier = Modifier
                        .height(26.dp)
                        .width(100.dp)
                )
            }

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(animatedScale)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(statusColor.copy(alpha = 0.4f), Color.Transparent),
                                radius = 220f
                            ),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(95.dp)
                        .background(statusColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "%.1f".format(magnitude),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = status,
                color = statusColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
