package messyshape

import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector

operator fun RealMatrix.get(row: Int, column: Int): Double = getEntry(row, column)
operator fun RealMatrix.set(row: Int, column: Int, value: Double) = setEntry(row, column, value)
operator fun RealVector.get(index: Int): Double = getEntry(index)
operator fun RealVector.set(index: Int, value: Double) = setEntry(index, value)
