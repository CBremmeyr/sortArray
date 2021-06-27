package sortArray

import spinal.core._
import spinal.lib._

//
// Sort unsigned values as they arive serially
// Largest value will be first output after flush
// New input data will be ignored while flushing
//
case class SortArray(width: Int, depth: Int) extends Component {
  val io = new Bundle {
    val flush     = in Bool
    val flushDone = out Bool
    val dataIn    = slave  Flow(UInt(width bits))
    val dataOut   = master Flow(UInt(width bits))
  }

  // Depth must be at least 2
  final val depth_min = 2;
  if(depth < depth_min) {
    throw new IllegalArgumentException("`depth` argument too small, must be at least " + depth_min)
  }

  // Make array of sorting nodes
  val sortArray = Array.fill(depth) (new SortNode(width))

  // Pass inputs to first node
  sortArray(0).io.flush := io.flush
  sortArray(0).io.dataIn <> io.dataIn

  // Wire rest of nodes togeather
  var i = 0
  for(i <- 0 to depth-1-1) {
    sortArray(i+1).io.flush := sortArray(i).io.flushNext
    sortArray(i+1).io.dataIn <> sortArray(i).io.dataOut
  }

  // Drive outputs with last node's outputs
  io.flushDone := sortArray(depth-1).io.flushNext
  io.dataOut <> sortArray(depth-1).io.dataOut
}

