package sortArray

import spinal.core._

// Sort unsigned values as they arive serially
// Largest value will be fisrt output after flush
// new input data will be ignored while flushing
case class SortArray(width: Int, depth: Int) extends Component {
  val io = new Bundle {
    val flush     = in Bool
    val flushing  = out Bool
    val dataIn    = slave  Flow(Bits(width bits))
    val dataOut   = master Flow(Bits(width bits))
  }

  val doingFlush = Reg(Bool) init(0)
  when(!doingFlush) {

  }

  val sortArea = new Area {

    // Make sorting array out of sort nodes
    val sortArray = Array.fill(depth) (new sortNode(width))

    // Wire inputs to each node
    sortArray(0).io.flush := io.flush
    sortArray(0).io.dataIn := io.dataIn
    for(var i <- 1 until depth) {
      sortArray(i).io.flush := sortArray(i-1).io.flushNext
      sortArray(i).io.dataIn := sortArray(i-1).io.dataOut
    }

    // Wire last node's outputs
    val flushOut := sortArray(depth-1).io.flushNext
    val dataOut := sortArray(depth-1).io.dataOut
  }
}
