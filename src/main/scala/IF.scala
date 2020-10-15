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
	    val branchDest = Input(UInt(32.W))
	    val branchDestV = Input(Bool())
	    val instruction = Output(new Instruction)
      val PC = Output(UInt(32.W))

     
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

   // PC := Mux(io.branchDestV,PC+io.branchDest,PC+4.U)
    
   when (!io.IMEMsetup.setup) {
    when (io.branchDestV) {
      PC := io.branchDest // jump branch condition met
    } .otherwise {
      PC := PC + 4.U // next branch condition not met
    }
  }
  io.instruction := instruction
  io.PC := PC
  
//  printf("Instruksjon fra IF er: %d\n", io.instruction)
	
}



	



