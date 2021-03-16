package sortArray

import spinal.core._
import spinal.lib._

case class sortNode(width: Int) extends Component {
  val io = new Bundle {
    val flush     = in Bool
    val flushNext = out Bool
    val dataIn    = slave Flow(UInt(width bits))
    val dataOut   = master Flow(UInt(width bits))
  }

  val storedVal = Reg(UInt(width bits))
  val passVal   = Reg(UInt(width bits))

  val sorter = new Area {
    val storedVal = UInt(width bits)
    val newVal    = UInt(width bits)

    val nextStoredVal = UInt(width bits)
    val passVal       = UInt(width bits)

    // Determine value to store and value to pass
    val storeNewVal: Bool = newVal < storedVal
    when(storeNewVal) {
      nextStoredVal := newVal
      passVal := storedVal
    } otherwise {
      nextStoredVal := storedVal
      passVal := newVal
    }
  }

  // Wire inputs to sorter circuit
  sorter.newVal := io.dataIn.payload
  sorter.storedVal := storedVal

  when(io.dataIn.valid) {
    storedVal := sorter.nextStoredVal
    passVal := sorter.passVal
  }

  when(io.flush) {
    storedVal := io.dataIn.payload
    passVal := storedVal
  }

  io.flushNext := Delay(io.flush, 1)
  io.dataOut.valid := Delay(io.dataIn.valid, 1)
  io.dataOut.payload := passVal
}
