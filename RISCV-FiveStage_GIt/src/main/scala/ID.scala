package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase, MuxLookup }
import chisel3.experimental.MultiIOModule


class InstructionDecode extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new IFBarrierSignals)
      val out = Output(new IDBarrierSignals)

      val writeEnable  = Input(Bool())
      val writeData    = Input(UInt(32.W))
      val writeAddress = Input(UInt(32.W))

      // setup/test
      val registerSetup = Input(new RegisterSetupSignals)
      val registerPeek  = Output(UInt(32.W))

      val testUpdates   = Output(new RegisterUpdates)
    })

  val registers = Module(new Registers).io
  val control   = Module(new Control).io

  // Wire up register setup
  registers.setup := io.registerSetup
  io.registerPeek := registers.readData1
  io.testUpdates  := registers.testUpdates

  // Decoding
  control.instruction := io.in.instruction

  // Register routing
  registers.readAddress1 := io.in.instruction.registerRs1
  registers.readAddress2 := io.in.instruction.registerRs2

  // Writeback
  registers.writeEnable  := io.writeEnable
  registers.writeAddress := io.writeAddress
  registers.writeData    := io.writeData

  {
    import ImmFormat._
    io.out.immData := MuxLookup(control.immType,
      control.instruction.immediateIType,
      Array(
        ITYPE -> control.instruction.immediateIType,
        STYPE -> control.instruction.immediateSType,
        BTYPE -> control.instruction.immediateBType,
        UTYPE -> control.instruction.immediateUType,
        JTYPE -> control.instruction.immediateJType,
        SHAMT -> control.instruction.registerRs2.asSInt
      ))
  }

  // Outputs
  io.out.PC                    := io.in.PC
  io.out.readData1             := registers.readData1
  io.out.readData2             := registers.readData2
  io.out.registerRd            := io.in.instruction.registerRd
  io.out.decode.controlSignals := control.controlSignals
  io.out.decode.branchType     := control.branchType
  io.out.decode.op1Select      := control.op1Select
  io.out.decode.op2Select      := control.op2Select
  io.out.decode.immType        := control.immType
  io.out.decode.ALUOp          := control.ALUOp
}
