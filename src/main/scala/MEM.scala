package FiveStage
import chisel3._
import chisel3.util._
import chisel3.experimental.MultiIOModule

class MemoryFetch() extends Module {
  val io = IO(
    new Bundle {


      // setup/test
      val DMEMsetup      = Input(new DMEMsetupSignals)
      val DMEMpeek       = Output(UInt(32.W))

      val testUpdates    = Output(new MemUpdates)
      
      //DMEM
      val Address = Input(UInt(32.W))
      val WriteData = Input(UInt(32.W))
      val ReadData = Output(UInt(32.W))
      
    //  val jump = Output(Bool())
     // val jumpAddr = Output(UInt(32.W))
      

      val ALUresult = Input(UInt(32.W))
      val instruction = Input(new Instruction)
      
      val memToReg = Input(Bool())
      val memWrite = Input(Bool())
     // val memRead = Input(Bool())
      
    })

  val DMEM = Module(new DMEM).io
  

  // setup
  DMEM.setup := io.DMEMsetup
  io.DMEMpeek := DMEM.dataOut
  io.testUpdates := DMEM.testUpdates
  
//  io.Address := EX.ALUresOut
 // io.WriteData := EX.Bout
 
 DMEM.dataAddress := io.ALUresult
 DMEM.dataIn := io.WriteData
 DMEM.writeEnable := io.memWrite
 
 
 io.ReadData := DMEM.dataOut
 
 //printf("ReadData er naa: %d \n", io.ReadData)
 //printf("ALU i MEM stage er naa: %d \n", io.ALUresult)
 
 
  
  

}
