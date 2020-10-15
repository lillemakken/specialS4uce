package FiveStage
import chisel3._
import chisel3.util.BitPat
import chisel3.util.ListLookup

/**
  Responsible for setting control signals, from the "classic" set in ControlSignals
  to select signals for the ID operator muxes
 */
class Control() extends Module {

  val io = IO(new Bundle {
                val instruction    = Input(new Instruction)

                val controlSignals = Output(new ControlSignals)
                val branchType     = Output(UInt(3.W))
                val op1Select      = Output(UInt(1.W))
                val op2Select      = Output(UInt(1.W))
                val immType        = Output(UInt(3.W))
                val ALUop          = Output(UInt(4.W))
              })

  import lookup._
  import Op1Select._
  import Op2Select._
  import branchType._
  import ImmFormat._

  val N = 0.asUInt(1.W)
  val Y = 1.asUInt(1.W)

  /**
    If you want to you can choose to remove everything that isn't a control signal.
    Another alternative is to just temporarily remove what you don't care about and then add it back afterwards.
    */
  val opcodeMap: Array[(BitPat, List[UInt])] = Array(

    // signal      memToReg, regWrite, memRead, memWrite, branch,  jump, branchType,    Op1Select, Op2Select, ImmSelect,    ALUOp
    ADD    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.ADD),
    SUB    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.SUB),
    AND    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.AND),
    OR     -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.OR),
    XOR    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.XOR),
    SLT    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.SLT),
    SLTU   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.SLTU),
    SRA    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.SRA),
    SRL    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.SRL),
    SLL    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       rs2,       ImmFormat.DC, ALUOps.SLL),
    ADDI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.ADD),
    ANDI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.AND),
    ORI    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.OR),
    XORI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.XOR),
    SLTI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.SLT),
    SLTIU  -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.SLTU),
    SRAI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       SHAMT, ALUOps.SRA),
    SRLI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       SHAMT, ALUOps.SRL),
    SLLI   -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, rs1,       imm,       SHAMT, ALUOps.SLL),
    LUI    -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, Op1Select.DC, imm,    UTYPE, ALUOps.COPY_B),
    AUIPC  -> List(N,        Y,        N,       N,        N,       N,    branchType.DC, PC,       imm,        UTYPE, ALUOps.ADD),
    LW     -> List(Y,        Y,        Y,       N,        N,       N,    branchType.DC, rs1,       imm,       ITYPE, ALUOps.ADD),
    SW     -> List(N,        N,        N,       Y,        N,       N,    branchType.DC, rs1,       imm,       STYPE, ALUOps.ADD),
    JAL    -> List(N,        Y,        N,       N,        N,       Y,    branchType.jump, PC,       imm,       JTYPE, ALUOps.ADD),
    JALR   -> List(N,        Y,        N,       N,        N,       Y,    branchType.jump, PC,       imm,       ITYPE, ALUOps.ADD),
    BEQ    -> List(N,        N,        N,       N,        Y,       N,    branchType.beq, rs1,       rs2,       BTYPE, ALUOps.DC),
    BNE    -> List(N,        N,        N,       N,        Y,       N,    branchType.neq, rs1,       rs2,       BTYPE, ALUOps.DC),
    BLT    -> List(N,        N,        N,       N,        Y,       N,    branchType.lt,  rs1,       rs2,       BTYPE, ALUOps.DC),
    BGE    -> List(N,        N,        N,       N,        Y,       N,    branchType.gte, rs1,       rs2,       BTYPE, ALUOps.DC),
    BLTU   -> List(N,        N,        N,       N,        Y,       N,    branchType.ltu, rs1,       rs2,       BTYPE, ALUOps.DC),
    BGEU   -> List(N,        N,        N,       N,        Y,       N,    branchType.gteu, rs1,      rs2,       BTYPE, ALUOps.DC)
    /**
      Fill in the blanks
      */
    )


  val NOP = List(N, N, N, N, N, N, branchType.DC, rs1, rs2, ImmFormat.DC, ALUOps.DC)

  val decodedControlSignals = ListLookup(
    io.instruction.asUInt(),
    NOP,
    opcodeMap)

  io.controlSignals.memToReg   := decodedControlSignals(0)
  io.controlSignals.regWrite   := decodedControlSignals(1)
  io.controlSignals.memRead    := decodedControlSignals(2)
  io.controlSignals.memWrite   := decodedControlSignals(3)
  io.controlSignals.branch     := decodedControlSignals(4)
  io.controlSignals.jump       := decodedControlSignals(5)
  
  //printf("controlSignals.memToReg er: %d \n", io.controlSignals.memToReg)

  io.branchType := decodedControlSignals(6)
  io.op1Select  := decodedControlSignals(7)
  io.op2Select  := decodedControlSignals(8)
  io.immType    := decodedControlSignals(9)
  io.ALUop      := decodedControlSignals(10)
}
