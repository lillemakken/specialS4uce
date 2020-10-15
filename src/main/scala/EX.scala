package FiveStage
import chisel3._
import chisel3.util.{ MuxLookup, MuxCase}
import ALUOps._

class Execute extends Module {
  val io = IO(
    new Bundle {
    
      
   //   val ALU1 = Input(UInt(32.W))
     // val ALU2 = Input(UInt(32.W))
     
     val PCdest = Input(UInt(32.W))
     
     val A = Input(UInt(32.W))
     val B = Input(UInt(32.W))
      
     val immType = Input(UInt(3.W))
     val immOpSignal = Input(UInt(32.W))
     val instruction = Input(new Instruction)
     
     val ALUout = Output(UInt(32.W))
      
     
    val ALUop = Input(UInt(4.W))
    val op1select = Input(Bool())
    val op2select = Input(Bool())
    val jumpOut = Output(Bool()) //Tidligere BranchTypeOut
    val jumpAddr = Output(UInt(32.W))
    
    val branch = Input(Bool()) //Ja/Nei ?
    val branchType = Input(UInt(4.W))
    
    val memToReg = Input(Bool())
    
    //Tester immOp left shifta + PC til barriere:
   // val sum = Output(UInt(32.W))
    
    val jump = Input(Bool())
    
         
     })
     
      
  //io.sum := (io.immOpSignal << 2) + io.PCdest

  val ALU1 = MuxLookup(io.op1select, 2147483647.U, Array(
    Op1Select.rs1 -> io.A,
    Op1Select.PC -> Mux(io.jump,
      io.PCdest + 4.U,
      io.PCdest)
     ))
    
  val ALU2 = MuxLookup(io.op2select, 2147483647.U, Array(
    Op2Select.rs2 -> io.B,
    Op2Select.imm -> Mux(io.jump,
    0.U,
    io.immOpSignal)
   ))
 // val ALU2 = Mux(io.op2select, io.immOpSignal, io.B)
  
  val shamt = ALU2(4,0).asUInt
  
 // printf("\n ALU1 er: %d og ALU2 er: %d\n", ALU1, ALU2)


//ALU
io.ALUout := MuxLookup(io.ALUop,
    "hAAAAAAAA".U,
    Array(
      ADD    -> (ALU1        +  ALU2       ).asUInt,
      SUB    -> (ALU1        -  ALU2       ).asUInt,
      AND    -> (ALU1        &  ALU2       ).asUInt,
      OR     -> (ALU1        |  ALU2       ).asUInt,
      XOR    -> (ALU1        ^  ALU2       ).asUInt,
      SLT    -> (ALU1.asSInt <  ALU2.asSInt).asUInt,
      SLL    -> (ALU1        << shamt      ).asUInt,
      SLTU   -> (ALU1        <  ALU2       ).asUInt,
      SRL    -> (ALU1        >> shamt      ).asUInt,
      SRA    -> (ALU1.asSInt >> shamt      ).asUInt,
      COPY_A ->  ALU1.asUInt,
      COPY_B ->  ALU2.asUInt,
      DC     ->  ALU1.asUInt
    ))
/*io.ALUout := MuxCase(0.U, Array(
                       (io.ALUop === ALUOps.ADD) -> (ALU1 + ALU2).asUInt,
                       (io.ALUop === ALUOps.SUB) -> (ALU1 - ALU2).asUInt,
                       (io.ALUop === ALUOps.AND) -> (ALU1 & ALU2).asUInt,
                       (io.ALUop === ALUOps.OR) -> (ALU1 | ALU2).asUInt,
                       (io.ALUop === ALUOps.XOR) -> (ALU1 ^ ALU2).asUInt,
                       (io.ALUop === ALUOps.SLT) -> (ALU1.asSInt < ALU2.asSInt).asUInt,
                       (io.ALUop === ALUOps.SLL) -> (ALU1 << shamt).asUInt,
                       (io.ALUop === ALUOps.SLTU) -> (ALU1 < ALU2).asUInt,
                       (io.ALUop === ALUOps.SRL) -> (ALU1 >> shamt).asUInt,
                       (io.ALUop === ALUOps.SRA) -> (ALU1.asSInt >> shamt).asUInt,
                       (io.ALUop === ALUOps.DC) -> (ALU1).asUInt
                       (io.ALUop === ALUOps.COPY_A) -> (ALU1).asUInt,
                       (io.ALUop === ALUOps.COPY_B) -> (ALU2).asUInt,
                       ))
                       */
                       //gg
                       
//Branch-sjekk
/*io.branchTypeOut := false.B

// io.branchType := IDBarrier.branchTypeOut

  //switch(io.branchType) {
  when(io.branchType === branchType.beq) {
    
      when(io.A === io.B) {
        io.branchTypeOut := true.B
        } .otherwise {
        io.branchTypeOut := false.B
        }
        
   }
    when(io.branchType === branchType.neq) {
     
      when(io.A =/= io.B) {
        io.branchTypeOut := true.B
        } .otherwise {
        io.branchTypeOut := false.B
        }
        
   }
    when (io.branchType === branchType.lt) {
      
      when(io.A.asSInt < io.B.asSInt) {
        io.branchTypeOut := true.B
        } .otherwise {
        io.branchTypeOut := false.B
        }
        
    }
     when (io.branchType === branchType.gte) {
     
      when(io.A.asSInt > io.B.asSInt) {
        io.branchTypeOut := true.B
        } .otherwise {
        io.branchTypeOut := false.B
        }
        
    }
      when (io.branchType === branchType.ltu) {
     
        when(io.A < io.B) {
          io.branchTypeOut := true.B
        } .otherwise {
        io.branchTypeOut := false.B
        }
        
        }
     when (io.branchType === branchType.gteu) {
     
        when(io.A > io.B) {
          io.branchTypeOut := true.B
        } .otherwise {
          io.branchTypeOut := false.B
        }
      when (io.branchType === branchType.jump && io.immOpSignal === ImmFormat.JTYPE) {
      
        io.branchTypeOut := true.B
        }  .otherwise {
          io.branchTypeOut := false.B
        }
        
        
     }
      //is (branchType.jump) {    
      */
      //val conditionalBranch
      val BranchValg = MuxLookup(io.branchType,
    false.B, // No branching default
    Array(
      branchType.beq   -> (io.A === io.B),
      branchType.neq   -> (io.A        =/= io.B),
      branchType.lt    -> (io.A.asSInt <   io.B.asSInt),
      branchType.gte   -> (io.A.asSInt >=  io.B.asSInt),
      branchType.ltu   -> (io.A        <   io.B),
      branchType.gteu  -> (io.A        >=  io.B)
    )
  )
  
  val JALRMask = "hfffffff".U
  val jumpBase = Mux(io.immType === ImmFormat.JTYPE, io.PCdest, io.A)
  val branch = (io.branch && BranchValg)
  
  io.jumpOut := (branch || io.jump)
  io.jumpAddr := Mux(branch, io.PCdest + io.immOpSignal, jumpBase + io.immOpSignal & JALRMask)
   
      
     // printf("ALUout er: %d \n", io.ALUout)
           
        
                           
}

