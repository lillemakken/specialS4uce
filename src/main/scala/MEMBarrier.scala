package FiveStage

import chisel3._

class MEMBarrierSignals extends Bundle {
  val readData   = UInt(32.W)
  val ALUResult  = UInt(32.W)
  val registerRd = UInt(5.W)
  val control    = new Bundle {
    val regWrite = Bool()
    val memRead  = Bool()
  }
}

class MEMBarrier extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new MEMBarrierSignals)
      val out = Output(new MEMBarrierSignals)
    })

  val delay = RegNext(io.in)
  io.out   := delay

  // passthrough
  io.out.readData := io.in.readData
}
