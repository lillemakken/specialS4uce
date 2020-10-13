package FiveStage

import chisel3._

class EXBarrierSignals extends Bundle {
  val readData   = UInt(32.W)
  val ALUResult  = UInt(32.W)
  val zero       = Bool()
  val registerRd = UInt(5.W)
  val jump       = Bool()
  val jumpAddr   = UInt(32.W)
  val control    = new Bundle {
    val memToReg = Bool()
    val memRead  = Bool()
    val memWrite = Bool()
    val regWrite = Bool()
  }
}

class EXBarrier extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new EXBarrierSignals)
      val out = Output(new EXBarrierSignals)
    })

  val delay = RegNext(io.in)
  io.out   := delay
}
