package FiveStage
import chisel3._
import chisel3.util.{ BitPat, MuxCase, MuxLookup }

class WriteBack() extends Module {
  val io = IO(
    new Bundle {
      val in  = Input(new MEMBarrierSignals)
      val writeEnable  = Output(Bool())
      val writeData    = Output(UInt(32.W))
      val writeAddress = Output(UInt(32.W))
    })

  io.writeEnable  := io.in.control.regWrite
  io.writeAddress := io.in.registerRd
  io.writeData    := Mux(
    io.in.control.memRead,
    io.in.readData,
    io.in.ALUResult)
}
