package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase, MuxLookup }
import chisel3.experimental.MultiIOModule
import ImmFormat._

/**
  */
class InstructionDecode extends Module {
  val io = IO(
    new Bundle {

      // setup/test
	
      val registerSetup  = Input(new RegisterSetupSignals)
      val registerPeek   = Output(UInt(32.W))

      
     
      val writeEnable = Input(Bool())
      val writeBackData = Input(UInt(32.W))
      val writeBackAddress = Input(UInt(32.W))
      
      //val PCin = Input(UInt(32.W))
      
      val immOpres = Output(UInt(32.W))
      
      val instruction = Input(new Instruction)
      
      //Gjennomgående signaler (Ignore)
      val ALUOp = Output(UInt(32.W))
      val op1Select = Output(UInt(1.W))
      val op2Select = Output(UInt(1.W))
      val memToReg = Output(Bool())
      val branchType = Output(UInt(4.W))
      val memWrite = Output(Bool())
      val jump = Output(Bool())
      val branch = Output(Bool())
      val immType = Output(UInt(3.W))
      val memRead = Output(Bool())
      val regWrite = Output(Bool())
      val regRd = Output(UInt(32.W))
      
      //Ut av Registerblokken
      val readData1 = Output(UInt(32.W))
      val readData2 = Output(UInt(32.W))
       
      //
      //Trenger jeg denne under? V V V
      //val setupWriteData = Input(UInt(32.W))
      
     // val readRegister1 = Input(UInt(32.W))
      //val readRegister2 = Input(UInt(32.W))
      

      val testUpdates       = Output(new RegisterUpdates)
    })
    

  val control      = Module(new Control).io
  val registers    = Module(new Registers).io
  
 
  // Wire up register setup
  registers.setup := io.registerSetup
  io.registerPeek := registers.readData1
  io.testUpdates  := registers.testUpdates
  
  control.instruction := io.instruction
 
  
  io.memWrite := control.controlSignals.memWrite
  

  
  registers.readAddress1 := io.instruction.registerRs1  
  registers.readAddress2 := io.instruction.registerRs2
  
//  printf("readAddress1 er : %d \n", io.instruction.registerRs1)
  

  registers.writeAddress := io.writeBackAddress
  registers.writeData := io.writeBackData
  registers.writeEnable := io.writeEnable
  
  io.regWrite := control.controlSignals.regWrite
  
  io.regRd := io.instruction.registerRd //Dette var feilen. Hvorfor skulle denne defineres her og ikke fra IF?
 

  io.readData1 := registers.readData1
  io.readData2 := registers.readData2  
 // printf("readData1 rett ut er: %d \n", io.readData1)

  //registers.setup.writeData := io.writeBackData 


//printf("writeBackData er naa: %d \n", io.writeBackData)
//printf("#")
 

 
  io.ALUOp := control.ALUop
  io.op1Select := control.op1Select
  io.op2Select := control.op2Select
  io.memToReg := control.controlSignals.memToReg
  io.branchType := control.branchType
  io.jump := control.controlSignals.jump
  io.branch := control.controlSignals.branch
  io.immType := control.immType
  io.memRead := control.controlSignals.memRead
 
 //printf("memToReg i ID er: %d \n", io.memToReg)
 
/*io.immOpres := MuxCase(0.S, Array(
                       (control.immType === ITYPE) -> io.instruction.immediateIType,
                       (control.immType === STYPE) -> io.instruction.immediateSType,
                       (control.immType === BTYPE) -> io.instruction.immediateBType,
                       (control.immType === UTYPE) -> io.instruction.immediateUType,
                       (control.immType === JTYPE) ->
io.instruction.immediateJType)).asUInt()
*/

  {
    import ImmFormat._
    io.immOpres := MuxLookup(control.immType,
      control.instruction.immediateIType,
      Array(
        ITYPE -> control.instruction.immediateIType,
        STYPE -> control.instruction.immediateSType,
        BTYPE -> control.instruction.immediateBType,
        UTYPE -> control.instruction.immediateUType,
        JTYPE -> control.instruction.immediateJType,
        SHAMT -> control.instruction.registerRs2.asSInt
      )).asUInt()
  }


//io.immOp := out.immOp





}
