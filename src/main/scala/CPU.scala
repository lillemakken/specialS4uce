package FiveStage

import chisel3._
import chisel3.core.Input
import chisel3.experimental.MultiIOModule
import chisel3.experimental._


class IFBarrier extends Module {
  val io = IO( new Bundle {
  
  val PCin = Input(UInt(32.W))
  val PCout = Output(UInt(32.W))
  
  val instruction = Output(new Instruction)
  val instructionIn = Input(new Instruction)
  
  val regRd = Output(UInt(32.W))
  //val instructionOut = Output(new Instruction)
    
  
})

val PCreg = RegInit(UInt(32.W), 0.U)
PCreg := io.PCin
io.PCout := PCreg

io.regRd := io.instruction.registerRd
io.instruction := io.instructionIn


}



class IDBarrier extends Module {

  val io = IO( new Bundle {
		val PCin = Input(UInt(32.W))
		val PCout = Output(UInt(32.W))
		
		val read1in = Input(UInt(32.W))
		val read1out = Output(UInt(32.W))
		
		
		val read2in = Input(UInt(32.W))
		val read2out = Output(UInt(32.W))
		
		val immOpin = Input(UInt(32.W))
		val immOpout = Output(UInt(32.W))
		
		val instructionin = Input(new Instruction)
		val instructionout = Output(new Instruction)
		
		val op1Selectin = Input(UInt(32.W))
		val op1Selectout = Output(UInt(32.W))
		
		val op2Selectin = Input(UInt(32.W))
		val op2Selectout = Output(UInt(32.W))
		
		val ALUopin = Input(UInt(32.W))
		val ALUopout = Output(UInt(32.W))
		
		val memToRegin = Input(Bool())
		val memToRegout = Output(Bool())
		
		val memWritein = Input(Bool())
		val memWriteout = Output(Bool())
		
		val branchTypeIn = Input(UInt(4.W))
		val branchTypeOut = Output(UInt(4.W))
		
		val jumpIn = Input(Bool())
    val jumpOut = Output(Bool())
    
    val branchIn = Input(Bool())
    val branchOut = Output(Bool())
    
    val immTypeIn = Input(UInt(3.W))
    val immTypeOut = Output(UInt(3.W))
    
    val memReadIn = Input(Bool())
    val memReadOut = Output(Bool())
    
    val writeEnableIn = Input(Bool())
    val writeEnableOut = Output(Bool())
    
    val regRdIn = Input(UInt(5.W))
    val regRdOut = Output(UInt(5.W))
    
		
		
	})
	
	val PCreg = RegInit(UInt(32.W), 0.U)
	PCreg := io.PCin
	io.PCout := PCreg
	
	val read1reg = RegInit(UInt(32.W), 0.U)
	read1reg := io.read1in
	io.read1out := read1reg
	
	val read2reg = RegInit(UInt(32.W), 0.U)
	read2reg := io.read2in
	io.read2out := read2reg
	
	val immOpreg = RegInit(UInt(32.W), 0.U)
	immOpreg := io.immOpin
	io.immOpout := immOpreg
	
	val instruksReg = RegInit(new Instruction, Instruction.default)
	instruksReg := io.instructionin
	io.instructionout := instruksReg
	
	val ALUreg = RegInit(UInt(32.W), 0.U)
	ALUreg := io.ALUopin
	io.ALUopout := ALUreg 
	
	val op1Reg = RegInit(UInt(32.W), 0.U)
	op1Reg := io.op1Selectin
	io.op1Selectout := op1Reg
	
	val op2Reg = RegInit(UInt(32.W), 0.U)
	op2Reg := io.op2Selectin
	io.op2Selectout := op2Reg
	
	val memToReginReg = RegInit(UInt(32.W), 0.U)
	memToReginReg := io.memToRegin
	io.memToRegout := memToReginReg
	
	val memWriteReg = RegInit(UInt(32.W), 0.U)
	memWriteReg := io.memWritein
	io.memWriteout := memWriteReg
	
	val branchTypeReg = RegInit(UInt(32.W), 0.U)
	branchTypeReg := io.branchTypeIn
	io.branchTypeOut := branchTypeReg
	
  val jumpReg = RegInit(UInt(32.W), 0.U)
  jumpReg := io.jumpIn
  io.jumpOut := jumpReg

  val branchReg = RegInit(UInt(32.W), 0.U)
  branchReg := io.branchIn
  io.branchOut := branchReg
  
  val immTypeReg = RegInit(UInt(32.W), 0.U)
  immTypeReg := io.immTypeIn
  io.immTypeOut := immTypeReg
  
  val memReadReg = RegInit(UInt(32.W), 0.U)
  memReadReg := io.memReadIn
  io.memReadOut := memReadReg
  
  val writeEnableReg = RegInit(UInt(32.W), 0.U)
  writeEnableReg := io.writeEnableIn
  io.writeEnableOut := writeEnableReg
  
  val regRdReg = RegInit(UInt(32.W), 0.U)
  regRdReg := io.regRdIn
  io.regRdOut := regRdReg
}
	
class EXBarrier extends Module {
  
  val io = IO( new Bundle {
  
    
    //val ALU0 = UInt(32.W)
    
    val ALUresIn = Input(UInt(32.W))
    val ALUresOut = Output(UInt(32.W))
    
    val Binn = Input(UInt(32.W))
    val Bout = Output(UInt(32.W))
    
    val instructionin = Input(new Instruction)
    val instructionout = Output(new Instruction)
    
    val memToRegin = Input(Bool())
    val memToRegout = Output(Bool())
    
    val memWritein = Input(Bool())
    val memWriteout = Output(Bool())
    
    val jumpOutIn = Input(Bool())
    val jumpOutOut = Output(Bool())
    
    val jumpAddrIn = Input(UInt(32.W))
    val jumpAddrOut = Output(UInt(32.W))
    
    val memReadIn = Input(Bool())
    val memReadOut = Output(Bool())
    
    val writeEnableIn = Input(Bool())
    val writeEnableOut = Output(Bool())
    
    val regRdIn = Input(UInt(5.W))
    val regRdOut = Output(UInt(5.W))
    
  /*  val sumIn = Input(UInt(32.W))
    val sumOut = Output(UInt(32.W))
    */
    
    
  })
  
  /*val sumReg = RegInit(UInt(32.W), 0.U)
  sumReg := io.sumIn
  io.sumOut := sumReg*/
  
  val ALUreg = RegInit(UInt(32.W), 0.U)
  ALUreg := io.ALUresIn
  io.ALUresOut := ALUreg
  
  val Breg = RegInit(UInt(32.W), 0.U)
  Breg := io.Binn
  io.Bout := Breg
  
  
  val instruksReg = RegInit(new Instruction, Instruction.default)
  instruksReg := io.instructionin
  io.instructionout := instruksReg
  
  val memToReginReg = RegInit(UInt(32.W), 0.U)
  memToReginReg := io.memToRegin
  io.memToRegout := memToReginReg
  
  val memWriteReg = RegInit(UInt(32.W), 0.U)
  memWriteReg := io.memWritein
  io.memWriteout := memWriteReg

  
  val jumpOutReg = RegInit(UInt(32.W), 0.U)
  jumpOutReg := io.jumpOutIn
  io.jumpOutOut := jumpOutReg
  
  val jumpAddrReg = RegInit(UInt(32.W), 0.U)
  jumpAddrReg := io.jumpAddrIn
  io.jumpAddrOut := jumpAddrReg
  
  val memReadReg = RegInit(UInt(32.W), 0.U)
  memReadReg := io.memReadIn
  io.memReadOut := memReadReg
  
   val writeEnableReg = RegInit(UInt(32.W), 0.U)
  writeEnableReg := io.writeEnableIn
  io.writeEnableOut := writeEnableReg
  
   val regRdReg = RegInit(UInt(32.W), 0.U)
  regRdReg := io.regRdIn
  io.regRdOut := regRdReg
  
 }
 

class MEMBarrier extends Module {

  val io = IO(
    new Bundle {
    
    val ReadDataIn = Input(UInt(32.W))
    val ReadDataOut = Output(UInt(32.W))
    
    val ALUresIn = Input(UInt(32.W))
    val ALUresOut = Output(UInt(32.W))
    
    val instructionin = Input(new Instruction)
    val instructionout = Output(new Instruction)
    
    val memToRegin = Input(Bool())
    val memToRegout = Output(Bool())
    
    val memReadIn = Input(Bool())
    val memReadOut = Output(Bool())
    
    val writeEnableIn = Input(Bool())
    val writeEnableOut = Output(Bool())
    
  /*  val jumpOutIn = Input(Bool())
    val jumpOutOut = Output(Bool())
    
    val jumpAddrIn = Input(UInt(32.W))
    val jumpAddrOut = Output(UInt(32.W))
    */
    val regRdIn = Input(UInt(5.W))
    val regRdOut = Output(UInt(5.W))
    
    })
    
    
  io.ReadDataOut := io.ReadDataIn
  
  val ALUreg = RegInit(UInt(32.W), 0.U)
  ALUreg := io.ALUresIn
  io.ALUresOut := ALUreg
  
  val instruksReg = RegInit(new Instruction, Instruction.default)
  instruksReg := io.instructionin
  io.instructionout := instruksReg
  
  val memToReginReg = RegInit(UInt(32.W), 0.U)
  memToReginReg := io.memToRegin
  io.memToRegout := memToReginReg
  
  val memReadReg = RegInit(UInt(32.W), 0.U)
  memReadReg := io.memReadIn
  io.memReadOut := memReadReg
  
   val writeEnableReg = RegInit(UInt(32.W), 0.U)
  writeEnableReg := io.writeEnableIn
  io.writeEnableOut := writeEnableReg
  
  /*val jumpOutReg = RegInit(UInt(32.W), 0.U)
  jumpOutReg := io.jumpOutIn
  io.jumpOutOut := jumpOutReg
  
  val jumpAddrReg = RegInit(UInt(32.W), 0.U)
  jumpAddrReg := io.jumpAddrIn
  io.jumpAddrOut := jumpAddrReg*/
  
   val regRdReg = RegInit(UInt(32.W), 0.U)
  regRdReg := io.regRdIn
  io.regRdOut := regRdReg
  }
  
    
  


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


// ############ IF #######################
  val IF  = Module(new InstructionFetch).io
  IFBarrier.instructionIn := IF.instruction
  
  //IFBarrier.instruction := IMem.instruction
  
  IFBarrier.PCin := IF.PC
  
  
  val ID  = Module(new InstructionDecode).io
  

  // ######## ID ###########
  
  IDBarrier.ALUopin := ID.ALUOp
  IDBarrier.op1Selectin := ID.op1Select
  IDBarrier.op2Selectin := ID.op2Select
  
  
  //
  //ID.readRegister1 := IFBarrier.instruction.registerRs1
 // ID.readRegister2 := IFBarrier.instruction.registerRs2
  
  //memWrite
  IDBarrier.memWritein := ID.memWrite
  EXBarrier.memWritein := IDBarrier.memWriteout
  
  //MemtoReg
  IDBarrier.memToRegin := ID.memToReg
  
  //memRead
  IDBarrier.memReadIn := ID.memRead

  //writeEnable
  IDBarrier.writeEnableIn := ID.regWrite
  
  //PC fra IFBarrier til IDBArrier
  IDBarrier.PCin := IFBarrier.PCout 
  

// Read data 1 & 2 kobles til barriere  
IDBarrier.read1in := ID.readData1
IDBarrier.read2in := ID.readData2

//printf("ID.readData1 er = %d \n",ID.readData1)

//immOp til barrier
IDBarrier.immOpin := ID.immOpres


//Instruksjon til barrier
ID.instruction := IFBarrier.instruction
IDBarrier.instructionin := ID.instruction


//branchType til barrier

IDBarrier.branchTypeIn := ID.branchType

//jumpInstruks

IDBarrier.jumpIn := ID.jump

//immType til IDBarrier
IDBarrier.immTypeIn := ID.immType

//branchBool
IDBarrier.branchIn := ID.branch
  
  
 IDBarrier.regRdIn := ID.regRd
  
 val EX = Module(new Execute).io
  
  // ############### EX ##############
  
  //FRA BARRIER
  
  
  //Jump Signal
  EX.jump := IDBarrier.jumpOut
  
  //immType fra IDBarrier
  EX.immType := IDBarrier.immTypeOut
  EX.PCdest := IDBarrier.PCout 
  
  EX.A := IDBarrier.read1out
  EX.B := IDBarrier.read2out
  
  EX.memToReg := IDBarrier.memToRegout
  EX.ALUop := IDBarrier.ALUopout
  EX.op1select := IDBarrier.op1Selectout
  EX.op2select := IDBarrier.op2Selectout
  EX.immOpSignal := IDBarrier.immOpout
  
  EX.instruction := IDBarrier.instructionout
  
  EX.branchType := IDBarrier.branchTypeOut //Branch Type
  EX.branch := IDBarrier.branchOut //Branch taken
  
  //TIL BARRIER
  
  EXBarrier.memToRegin := EX.memToReg
  EXBarrier.ALUresIn := EX.ALUout
  EXBarrier.memToRegin := EX.memToReg

  EXBarrier.Binn := EX.B
  
  EXBarrier.instructionin := EX.instruction
  
   
  EXBarrier.jumpOutIn := EX.jumpOut  //Branch type
  
  EXBarrier.jumpAddrIn := EX.jumpAddr
  
  EXBarrier.memReadIn := IDBarrier.memReadOut //MemRead
  
  EXBarrier.writeEnableIn := IDBarrier.writeEnableOut //writeEnable
  
  EXBarrier.regRdIn := IDBarrier.regRdOut
  
  val MEM = Module(new MemoryFetch).io
  
  // ############# MEME ###############
  
  MEM.memToReg := EXBarrier.memToRegout
  MEMBarrier.memToRegin := MEM.memToReg
  
  MEM.WriteData := EXBarrier.Bout
  MEM.Address := EXBarrier.ALUresOut
  
  MEMBarrier.ReadDataIn := MEM.ReadData
  
  MEM.ALUresult := EXBarrier.ALUresOut
  MEMBarrier.ALUresIn := MEM.ALUresult
  
  MEM.instruction := EXBarrier.instructionout
  MEMBarrier.instructionin := MEM.instruction
  
  //memWrite
  MEM.memWrite := EXBarrier.memWriteout
  
  //writeEnable
  MEMBarrier.writeEnableIn := EXBarrier.writeEnableOut
  
  //memRead
  MEMBarrier.memReadIn := EXBarrier.memReadOut
  
  //MEM.jump := EXBarrier.jumpOutOut
  //MEM.jumpAddr := EXBarrier.jumpAddrOut
  
  
 
  MEMBarrier.regRdIn := EXBarrier.regRdOut
  
  // ############# Write Back ###########
  
  ID.writeBackData := Mux(MEMBarrier.memToRegout, MEMBarrier.ReadDataOut, MEMBarrier.ALUresOut)
  
  //printf("memToRegout er: %d \n", MEMBarrier.memToRegout)
  
 // printf("ID.writeBackData er: %d \n", ID.writeBackData)
  
  ID.writeBackAddress := MEMBarrier.regRdOut
 //printf("regRdOut er: %d \n", MEMBarrier.regRdOut)
  ID.writeEnable := MEMBarrier.writeEnableOut
  
  //printf("IDBarrierregRDOut er: %d \n", IDBarrier.regRdOut)
   
  

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
  io.currentPC  := IF.PC

  /**
    Your signals here
    */


 //MEMSTAGE sitt Aluresultat tilbake til IF:
  IF.branchDest := EXBarrier.jumpAddrOut
  IF.branchDestV := EXBarrier.jumpOutOut
  //IF.branchDest := EXBarrier.sumOut
  //IF.branchDest := EXBarrier.
}













