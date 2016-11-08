package swaggerblocks

sealed trait ParameterIn
case object Query extends ParameterIn
case object Header extends ParameterIn
case object Path extends ParameterIn
case object FormData extends ParameterIn
case object Body extends ParameterIn
