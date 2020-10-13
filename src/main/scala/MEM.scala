package FiveStage
import chisel3._
import chisel3.util._
import chisel3.experimental.MultiIOModule


class MemoryFetch() extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new EXBarrierSignals)
      val out = Output(new MEMBarrierSignals)

      val jump     = Output(Bool())
      val jumpAddr = Output(UInt(32.W))

      // setup/test
      val DMEMsetup      = Input(new DMEMsetupSignals)
      val DMEMpeek       = Output(UInt(32.W))

      val testUpdates    = Output(new MemUpdates)
    })

  val DMEM = Module(new DMEM).io

  // setup
  DMEM.setup := io.DMEMsetup
  io.DMEMpeek := DMEM.dataOut
  io.testUpdates := DMEM.testUpdates

  DMEM.writeEnable := io.in.control.memWrite
  DMEM.dataIn      := io.in.readData
  DMEM.dataAddress := io.in.ALUResult

  io.out.readData         := DMEM.dataOut
  io.out.ALUResult        := io.in.ALUResult
  io.out.control.memRead  := io.in.control.memRead
  io.out.control.regWrite := io.in.control.regWrite
  io.out.registerRd       := io.in.registerRd

  // Jump signals, TODO: refactor to EX?
  io.jump     := io.in.jump
  io.jumpAddr := io.in.jumpAddr
}
