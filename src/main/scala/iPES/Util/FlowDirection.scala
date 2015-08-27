package iPES.Util

/** Enum for direction of flow in the circuit. */
object FlowDirection extends Enumeration {
    type FlowDirection = Value
    val FORWARD, STOP, REVERSE = Value
}
