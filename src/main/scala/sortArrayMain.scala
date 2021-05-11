package sortArray

import spinal.core._

object sortArrayMain {
  def main(args: Array[String]) {
    val depth = 3
    val width = 8
    SpinalConfig(targetDirectory = "rtl").generateVerilog(SortArray(width, depth))
//    SpinalConfig(targetDirectory = "rtl").generateVerilog(SortNode(width))
  }
}
