package FiveStage

import chisel3._
import chisel3.core.Input
import chisel3.experimental.MultiIOModule
import chisel3.experimental._


class CPU extends Module {

  val io = IO(
    new Bundle {
      val setupSignals = Input(new SetupSignals)
      val testReadouts = Output(new TestReadouts)
      val regUpdates   = Output(new RegisterUpdates)
      val memUpdates   = Output(new MemUpdates)
      val currentPC    = Output(UInt(32.W))
    }
  )

  /**
    You need to create the classes for these yourself
    */
  val IFBarrier  = Module(new IFBarrier).io
  val IDBarrier  = Module(new IDBarrier).io
  val EXBarrier  = Module(new EXBarrier).io
  val MEMBarrier = Module(new MEMBarrier).io

  val IF  = Module(new InstructionFetch).io
  val ID  = Module(new InstructionDecode).io
  val EX  = Module(new Execute).io
  val MEM = Module(new MemoryFetch).io
  val WB  = Module(new WriteBack).io

  /**
    setup stuff
    */
  IF.IMEMsetup     := io.setupSignals.IMEMsignals
  ID.registerSetup := io.setupSignals.registerSignals
  MEM.DMEMsetup    := io.setupSignals.DMEMsignals

  io.testReadouts.registerRead := ID.registerPeek
  io.testReadouts.DMEMread     := MEM.DMEMpeek

  /**
    spying stuff
    */
  io.regUpdates := ID.testUpdates
  io.memUpdates := MEM.testUpdates
  io.currentPC  := IF.out.PC

  /**
    Your signals here
    */

  // In general: for all signals from/to barriers
  // are Bundled in in/out for all stages.
  // Signals not ending up in, or coming from a barrier,
  // are not prefixed infixed in. or out.

  IFBarrier.in := IF.out

  ID.in        := IFBarrier.out
  IDBarrier.in := ID.out

  EX.in        := IDBarrier.out
  EXBarrier.in := EX.out

  MEM.in        := EX.out
  MEMBarrier.in := MEM.out
  // No barrier signals
  IF.jump       := MEM.jump
  IF.jumpAddr   := MEM.jumpAddr

  WB.in           := MEMBarrier.out
  ID.writeEnable  := WB.writeEnable
  ID.writeData    := WB.writeData
  ID.writeAddress := WB.writeAddress
}
