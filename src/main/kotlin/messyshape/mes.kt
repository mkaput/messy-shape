package messyshape

import net.ericaro.surfaceplotter.surface.ArraySurfaceModel
import org.apache.commons.math3.linear.*
import java.lang.Math.*

fun mes(n: Int, output: ArraySurfaceModel) {
    val m = solveMatrix(n)
    val v = Array(2 * n + 1) { FloatArray(2 * n + 1) }

    var mi = 0
    for (i in 0..n) {
        for (j in 0..2 * n) {
            v[i][j] = m[mi++].toFloat()
        }
    }
    for (i in n + 1..2 * n) {
        for (j in 0..2 * n) {
            v[i][j] = if (j < n) 0f else m[mi++].toFloat()
        }
    }

    output.setValues(-1f, 1f, -1f, 1f, 2 * n + 1, v, null)
}

private fun solveMatrix(n: Int): RealVector =
    LUDecomposition(mkA(n)).solver.solve(mkB(n))

private fun mkA(n: Int): RealMatrix {
    val s = (n + 1) * (3 * n + 1)
    val m = Array2DRowRealMatrix(s, s)

    for (i in 0 until n * (2 * n + 1)) {
        if (i % (2 * n + 1) !== 2 * n) {
            val idx4 = i
            val idx3 = idx4 + 1
            val idx2 = idx3 + (2 * n + 1)
            val idx1 = idx2 - 1
            mmap(m, idx1, idx2, idx3, idx4)
        }
    }

    for (i in n * (2 * n + 1) + n until s - (n + 1)) {
        if (i % (n + 1) !== n) {
            val idx4 = i
            val idx3 = idx4 + 1
            val idx2 = idx3 + n + 1
            val idx1 = idx2 - 1
            mmap(m, idx1, idx2, idx3, idx4)
        }
    }

    for (i in n * (2 * n + 1)..n * (2 * n + 1) + n) {
        for (j in 0..s - 1) {
            m[i, j] = 0.0
        }
        m[i, i] = 1.0
    }

    for (i in (n + 1) * (2 * n + 1)..s - n step n + 1) {
        for (j in 0..s - 1) {
            m[i, j] = 0.0
        }
        m[i, i] = 1.0
    }

    return m
}

private fun mkB(n: Int): RealVector {
    val s = (n + 1) * (3 * n + 1)
    val m = ArrayRealVector(s)

    val h = 1.0 / n

    m[0] = 0.5 * h * (g(-1.0 + h, 1.0) + g(-1.0, 1.0 - h))

    for (i in 1 until n) {
        m[i * (2 * n + 1)] = 0.5 * h * (g(-1.0, 1.0 - (i - 1.0) * h) + g(-1.0, 1.0 - (i + 1.0) * h))
    }

    for (i in 1 until 2 * n) {
        m[i] = 0.5 * h * (g(-1.0 + (i - 1.0) * h, 1.0) + g(-1.0 + (i + 1.0) * h, 1.0))
    }

    m[2 * n] = 0.5 * h * (g(1.0 - h, 1.0) + g(1.0, 1.0 - h))

    for (i in 1..n) {
        m[(i + 1) * (2 * n + 1) - 1] = 0.5 * h * (g(1.0, 1.0 - (i - 1.0) * h) + g(1.0, 1.0 - (i + 1.0) * h))
    }

    for (i in n + 1 until 2 * n) {
        m[(n + 1) * (2 * n + 1) - 1 + (i - n) * (n + 1)] =
            0.5 * h * (g(1.0, 1.0 - (i - 1.0) * h) + g(1.0, 1.0 - (i + 1.0) * h))
    }

    m[s - 1] = 0.5 * h * (g(1.0 - h, -1.0) + g(1.0, -1.0 + h))

    for (i in 1 until n) {
        m[(s - (n + 1)) + i] = 0.5 * h * (g(((i - 1.0) * h), -1.0) + g((i + 1.0) * h, -1.0))
    }

    return m
}

private fun g(x: Double, y: Double): Double {
    val r = sqrt(x * x + y * y)
    val cos = cos(atan(y / x) - PI / 4)

    return cbrt(r * r) * cbrt(cos * cos)
}

private val matrix = arrayOf(
    doubleArrayOf(2.0 / 3.0, -1.0 / 6.0, -1.0 / 3.0, -1.0 / 6.0),
    doubleArrayOf(-1.0 / 6.0, 2.0 / 3.0, -1.0 / 6.0, -1.0 / 3.0),
    doubleArrayOf(-1.0 / 3.0, -1.0 / 6.0, 2.0 / 3.0, -1.0 / 6.0),
    doubleArrayOf(-1.0 / 6.0, -1.0 / 3.0, -1.0 / 6.0, 2.0 / 3.0))

private fun mmap(result: RealMatrix, _1: Int, _2: Int, _3: Int, _4: Int) {
    result[_1, _1] += matrix[0][0]
    result[_2, _2] += matrix[1][1]
    result[_3, _3] += matrix[2][2]
    result[_4, _4] += matrix[3][3]
    result[_2, _1] += matrix[0][1]
    result[_1, _2] = result[_2, _1]
    result[_3, _1] += matrix[0][2]
    result[_1, _3] = result[_3, _1]
    result[_4, _1] += matrix[0][3]
    result[_1, _4] = result[_4, _1]
    result[_3, _2] += matrix[1][2]
    result[_2, _3] = result[_3, _2]
    result[_4, _2] += matrix[1][3]
    result[_2, _4] = result[_4, _2]
    result[_4, _3] += matrix[2][3]
    result[_3, _4] = result[_4, _3]
}
