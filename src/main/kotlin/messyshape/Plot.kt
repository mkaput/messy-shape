package messyshape

import net.ericaro.surfaceplotter.JSurfacePanel
import net.ericaro.surfaceplotter.surface.ArraySurfaceModel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class Plot {
    private val window: JFrame
    private val plotFrame: JSurfacePanel
    private val plotModel: ArraySurfaceModel
    private val panel1: JPanel
    private val nSlider: JSlider
    private val sliderLabel: JLabel
    var n: Int

    init {
        window = JFrame("messy shape")
        with(window) {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            preferredSize = Dimension(800, 800)
        }

        plotModel = ArraySurfaceModel()
        with(plotModel) {
            isDisplayXY = true
            isDisplayZ = true
            isDisplayGrids = true
            isBoxed = true
        }

        plotFrame = JSurfacePanel(plotModel)
        with(plotFrame) {
            titleText = "Rozwiązanie równania ciepła"
            isConfigurationVisible = false
        }

        window.contentPane.add(plotFrame, BorderLayout.CENTER)
        window.pack()


        n = 10
        sliderLabel = JLabel("Aktualna wartość n: " + n)

        nSlider = JSlider(JSlider.HORIZONTAL, 1, 16, 10)
        with(nSlider) {
            setBorder(EmptyBorder(10, 10, 10, 10))
            setMajorTickSpacing(5)
            setMinorTickSpacing(1)
            setPaintTicks(true)
            addChangeListener(SliderListener())
        }

        panel1 = JPanel()
        panel1.add(sliderLabel)
        panel1.add(nSlider)
        window.contentPane.add(panel1, BorderLayout.SOUTH)
        window.pack()
    }

    fun run() {
        solve(10)
        window.isVisible = true
    }

    fun solve(n: Int) = mes(n, plotModel)

    inner class SliderListener : ChangeListener {

        override fun stateChanged(e: ChangeEvent) {
            val tmp: Int = nSlider.value
            n = tmp
            sliderLabel.text = "Aktualna wartość n: " + n
            solve(n)
        }
    }
}

