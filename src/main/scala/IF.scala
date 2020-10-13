package FiveStage
import chisel3._
import chisel3.experimental.MultiIOModule

class InstructionFetch extends Module {
  val io = IO(
    new Bundle {

      /**
        You need to add inputs and outputs here
        A good start is branch/jump address as input, and
        instruction as output.
        */
      val jump     = Input(Bool())
      val jumpAddr = Input(UInt(32.W))

      val out = Output(new IFBarrierSignals)

      // setup/test
      val IMEMsetup = Input(new IMEMsetupSignals)
    })

  // Setup
  val IMEM = Module(new IMEM).io
  val PC = RegInit(UInt(32.W), 0.U)
  IMEM.setup := io.IMEMsetup

  IMEM.instructionAddress := PC
  val instruction = Wire(new Instruction)
  instruction := IMEM.instruction.asTypeOf(new Instruction)

  /**
    Your code here
    */

  when (!io.IMEMsetup.setup) {
    when (io.jump) {
      PC := io.jumpAddr // jump / branch condition met
    } .otherwise {
      PC := PC + 4.U // nexti / branch condition not met
    }
  }

  // Outputs
  io.out.instruction := instruction
  io.out.PC          := PC
}
