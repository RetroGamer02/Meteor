package meteor.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import meteor.Main
import meteor.ui.compose.GamePanel.RS2GameView

/**
 * The main entry point to the Compose UI
 */
object Window {
    @Composable
    fun MeteorWindow() {
        RS2GameView()
    }
}