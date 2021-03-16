package sortArray

import spinal.core._

object sortArrayMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(sortNode(4))
  }
}
