package messyshape

import net.ericaro.surfaceplotter.JSurfacePanel
import net.ericaro.surfaceplotter.surface.ArraySurfaceModel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.border.EmptyBorder

class Plot {
    private val window: JFrame
    private val plotFrame: JSurfacePanel
    private val plotModel: ArraySurfaceModel
    private val panel1: JPanel
    private val nSlider: JSlider
    private val sliderLabel: JLabel
    private var n: Int = 9

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

        sliderLabel = JLabel("Ilość podziałów na elementy skończone: $n")

        nSlider = JSlider(JSlider.HORIZONTAL, 2, 16, 9)
        with(nSlider) {
            border = EmptyBorder(10, 10, 10, 10)
            majorTickSpacing = 5
            minorTickSpacing = 1
            paintTicks = true
            paintLabels = true
            addChangeListener({
                if (!nSlider.valueIsAdjusting) {
                    n = nSlider.value
                    sliderLabel.text = "Ilość podziałów na elementy skończone: $n"
                    solve(n)
                }
            })
        }

        panel1 = JPanel()
        panel1.add(sliderLabel)
        panel1.add(nSlider)

        window.contentPane.add(plotFrame, BorderLayout.CENTER)
        window.contentPane.add(panel1, BorderLayout.SOUTH)
        window.pack()
    }

    fun run() {
        solve(n)
        window.isVisible = true
    }

    fun solve(n: Int) = mes(n, plotModel)
}

