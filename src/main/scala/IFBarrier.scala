package FiveStage

import chisel3._

class IFBarrierSignals extends Bundle {
  val PC          = UInt(32.W)
  val instruction = new Instruction
}

class IFBarrier extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new IFBarrierSignals)
      val out = Output(new IFBarrierSignals)
    })

  val delay = RegNext(io.in)
  io.out   := delay

  // Passthrough (overwrites reg signals)
  io.out.instruction := io.in.instruction
}
