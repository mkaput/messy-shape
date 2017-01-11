package messyshape

import net.ericaro.surfaceplotter.JSurfacePanel
import net.ericaro.surfaceplotter.surface.ArraySurfaceModel
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.lang.Math.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class Plot {
    private val gCieplo = fun(x: Double, y: Double): Double {
        val r = sqrt(x * x + y * y)
        val cos = cos(atan(y / x) - PI / 4)

        return cbrt(r * r) * cbrt(cos * cos)
    }

    private val gMS = fun(x: Double, y: Double): Double = when {
        y == 1.0 -> x
        y == -1.0 -> -x
        x == 1.0 -> y
        x == -1.0 -> -y
        else -> error("unreachable")
    }

    private val window: JFrame
    private val plotFrame: JSurfacePanel
    private val panel1: JPanel
    private val combo: JComboBox<(Double, Double) -> Double>
    private val nSlider: JSlider
    private val sliderLabel: JLabel
    private var n: Int = 9
    private var g: (Double, Double) -> Double = gCieplo

    init {
        window = JFrame("messy shape")
        with(window) {
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            preferredSize = Dimension(800, 800)
        }

        plotFrame = JSurfacePanel()
        with(plotFrame) {
            titleText = "Rozwiązanie równania ciepła"
            isConfigurationVisible = false
        }

        combo = JComboBox(arrayOf(gCieplo, gMS))
        combo.renderer = object : JLabel(), ListCellRenderer<(Double, Double) -> Double> {
            override fun getListCellRendererComponent(
                list: JList<out (Double, Double) -> Double>,
                value: ((Double, Double) -> Double),
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                text = when (value) {
                    gCieplo -> "Równanie ciepła"
                    gMS -> "Manufactured solution"
                    else -> error("unknown fn")
                }

                return this
            }
        }
        combo.addActionListener {
            val cb = it.source as JComboBox<*>
            @Suppress("UNCHECKED_CAST")
            g = cb.selectedItem as (Double, Double) -> Double
            solve()
        }

        sliderLabel = JLabel("Ilość podziałów na elementy skończone: $n")

        nSlider = JSlider(JSlider.HORIZONTAL, 2, 16, 9)
        with(nSlider) {
            border = EmptyBorder(10, 10, 10, 10)
            majorTickSpacing = 5
            minorTickSpacing = 1
            paintTicks = true
            paintLabels = true
            addChangeListener {
                if (!nSlider.valueIsAdjusting) {
                    n = nSlider.value
                    sliderLabel.text = "Ilość podziałów na elementy skończone: $n"
                    solve()
                }
            }
        }

        panel1 = JPanel()
        panel1.add(combo)
        panel1.add(sliderLabel)
        panel1.add(nSlider)

        window.contentPane.add(plotFrame, BorderLayout.CENTER)
        window.contentPane.add(panel1, BorderLayout.SOUTH)
        window.pack()
    }

    fun run() {
        solve()
        window.isVisible = true
    }

    fun solve() {
        val model = createModel()
        mes(n, g, model)
        plotFrame.setModel(model)
        plotFrame.repaint()
    }

    private fun createModel(): ArraySurfaceModel = ArraySurfaceModel().apply {
        isDisplayXY = true
        isDisplayZ = true
        isDisplayGrids = true
        isBoxed = true
    }
}

