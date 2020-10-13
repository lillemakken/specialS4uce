package FiveStage
import chisel3._
import chisel3.util.{ MuxLookup }

class ALU extends Module {
  val io = IO(
    new Bundle {
      val A  = Input(UInt(32.W))
      val B  = Input(UInt(32.W))
      val op = Input(UInt(4.W))

      val result = Output(UInt(32.W))
    }
  )

  val shamt = io.B(4,0).asUInt

  import ALUOps._
  io.result := MuxLookup(io.op,
    "hAAAAAAAA".U,
    Array(
      ADD    -> (io.A        +  io.B       ).asUInt,
      SUB    -> (io.A        -  io.B       ).asUInt,
      AND    -> (io.A        &  io.B       ).asUInt,
      OR     -> (io.A        |  io.B       ).asUInt,
      XOR    -> (io.A        ^  io.B       ).asUInt,
      SLT    -> (io.A.asSInt <  io.B.asSInt).asUInt,
      SLL    -> (io.A        << shamt      ).asUInt,
      SLTU   -> (io.A        <  io.B       ).asUInt,
      SRL    -> (io.A        >> shamt      ).asUInt,
      SRA    -> (io.A.asSInt >> shamt      ).asUInt,
      COPY_A ->  io.A.asUInt,
      COPY_B ->  io.B.asUInt,
      DC     ->  io.A.asUInt
    ))
}
