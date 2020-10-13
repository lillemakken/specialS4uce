package FiveStage

import chisel3._

class IDBarrierSignals extends Bundle {
  val PC         = UInt(32.W)
  val decode     = new DecodeSignals
  val readData1  = UInt(32.W)
  val readData2  = UInt(32.W)
  val registerRd = UInt(5.W)
  val immData    = SInt(32.W)
}

class IDBarrier extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new IDBarrierSignals)
      val out = Output(new IDBarrierSignals)
    })

  val delay = RegNext(io.in)
  io.out   := delay
}
