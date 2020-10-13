package FiveStage

import chisel3._
import chisel3.util.{ MuxLookup, MuxCase}
import ALUOps._

/**
  */
class Execute extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new IDBarrierSignals)
      val out = Output(new EXBarrierSignals)
    })

  val A = MuxLookup(io.in.decode.op1Select,
    2147483647.U, // Largest prime <= 32 bits
    Array(
      Op1Select.rs1 -> io.in.readData1,
      Op1Select.PC  -> Mux(io.in.decode.controlSignals.jump,
        io.in.PC + 4.U,
        io.in.PC)
    ))

  val B = MuxLookup(io.in.decode.op2Select,
    2147483647.U,
    Array(
      Op2Select.rs2 -> io.in.readData2,
      Op2Select.imm -> Mux(io.in.decode.controlSignals.jump,
        0.U,
        io.in.immData.asUInt)
    ))

  val ALU = Module(new ALU).io
  ALU.A  := A
  ALU.B  := B
  ALU.op := io.in.decode.ALUOp

  // Do we take a conditional branch?
  val conditionalBranch = MuxLookup(io.in.decode.branchType,
    false.B, // No branching
    Array(
      branchType.beq   -> (A        === B),
      branchType.neq   -> (A        =/= B),
      branchType.lt    -> (A.asSInt <   B.asSInt),
      branchType.gte   -> (A.asSInt >=  B.asSInt),
      branchType.ltu   -> (A        <   B),
      branchType.gteu  -> (A        >=  B)
    )
  )

  // Relative (branch)
  // Absolute (jump)
  val jalrMask     = "hfffffffe".U
  val jumpBase     = Mux(io.in.decode.immType === ImmFormat.JTYPE,
    io.in.PC,
    io.in.readData1)
  val branch       = io.in.decode.controlSignals.branch && conditionalBranch
  io.out.jump     := (branch || io.in.decode.controlSignals.jump)
  io.out.jumpAddr := Mux(io.in.decode.controlSignals.branch,
    io.in.PC + io.in.immData.asUInt, // JAL && branches
    jumpBase + io.in.immData.asUInt & jalrMask) // JALR

  io.out.ALUResult        := ALU.result
  io.out.readData         := io.in.readData2
  io.out.zero             := false.B
  io.out.registerRd       := io.in.registerRd
  io.out.control.memToReg := io.in.decode.controlSignals.memToReg
  io.out.control.memRead  := io.in.decode.controlSignals.memRead
  io.out.control.memWrite := io.in.decode.controlSignals.memWrite
  io.out.control.regWrite := io.in.decode.controlSignals.regWrite
}
