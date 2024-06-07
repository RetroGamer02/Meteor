package meteor.ui.swing

import meteor.Constants.RS_DIMENSIONS
import meteor.Main
import meteor.events.DrawFinished
import meteor.ui.compose.GamePanel
import meteor.ui.compose.GamePanel.stretchedHeight
import meteor.ui.compose.GamePanel.stretchedWidth
import meteor.ui.config.AspectMode
import meteor.ui.config.CPUFilter
import meteor.ui.config.RenderMode
import org.rationalityfrontline.kevent.KEVENT
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.KeyboardFocusManager
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.JPanel


/**
 *  Handle scaling / stretching / post-processing
 */
class PostProcessGamePanel : JPanel() {
    private var graphics2D: Graphics2D? = null
    private val hints = RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    private var loading = true
    init {
        //Loading
        Thread(kotlinx.coroutines.Runnable {
            while (loading) {
                //Must sleep 1ms to draw correctly on fast cpus
                Thread.sleep(1)
                RS2GamePanel.image?.let { draw() }
            }
        }).start()

        //Login/In-game
        KEVENT.subscribe<DrawFinished> {
            //Kill the loading drawing thread
            loading = false
            checkFocus()
            draw()
        }
    }

    fun draw() {
        super.getGraphics()?.let {
            graphics2D = it as Graphics2D
            var finalImage = RS2GamePanel.image!!
            updateSizeAndScale()
            when (Main.client.renderMode) {
                RenderMode.GPU -> finalImage = upscaleGPU(finalImage)?: return
                RenderMode.CPU -> setCPURenderingHints(it)
                else -> {}
            }
            Main.updateStatusText()
            drawToSurface(it, finalImage)
        }
    }

    private fun updateSizeAndScale() {
        val scale = getScale()
        var stretchedWidth = width
        var stretchedHeight = height
        if (Main.client.aspectMode == AspectMode.FIT) {
            stretchedWidth = (RS_DIMENSIONS.width * scale).toInt()
            stretchedHeight = (RS_DIMENSIONS.height * scale).toInt()
        }

        GamePanel.stretchedWidth.value = stretchedWidth
        GamePanel.stretchedHeight.value = stretchedHeight

        if (Main.client.aspectMode == AspectMode.FIT)
            updatePadding((width - stretchedWidth.toFloat()) / 2)
        else
            updatePadding(0f)
    }

    private fun drawToSurface(graphics: Graphics, finalImage: BufferedImage) {
        when (Main.client.aspectMode) {
            AspectMode.FIT -> {
                graphics.drawImage(finalImage, Main.client.padding.toInt(), 0,
                    stretchedWidth.value, stretchedHeight.value,this)
            }
            AspectMode.FILL -> graphics.drawImage(finalImage, 0, 0, width, height,this)
            else -> {}
        }
    }

    private fun updatePadding(padding: Float) {
        Main.client.padding = padding.coerceAtLeast(0f)
        GamePanel.xPadding.value = Main.client.padding
    }

    private fun setCPURenderingHints(graphics2D: Graphics2D) {
        if (Main.client.cpuFilter == CPUFilter.BILINEAR) {
            graphics2D.setRenderingHints(hints)
        }
    }

    private fun upscaleGPU(inputImage: BufferedImage) : BufferedImage? {
        try {
            if (stretchedWidth.value > 0 && stretchedHeight.value > 0) {
                return Main.client.gpuResizeAndFilter(
                    inputImage, stretchedWidth.value, stretchedHeight.value, Main.client.gpuFilter.filter)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error occurred during GPU upscaling, disabling...")
            Main.client.renderMode = RenderMode.CPU
        }
        return null
    }

    private fun getScale(): Float {
        val windowSize: Int = height
        val canvasSize: Int = Main.client.gamePanel?.height ?: Main.initialSize.height
        val scale = windowSize.toFloat() / canvasSize

        val s = (scale)
        return s
    }

    /**
     * This fixes a compose bug where focus would be lost every click causing subsequent keystrokes to be ignored.
     */
    private fun checkFocus() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusOwner?.let {
            if (it::class.java.toString().contains("org.jetbrains.skiko.SkiaLayer")) {
                it.isFocusable = false
                Main.gamePanel.grabFocus()
            }
        }
    }
}