package internal

import internal.models._

object rendering {
  def render(root: ApiRoot, paths: Seq[ApiPath], schemata: Seq[ApiSchema]): String = {
    s"""
       |$root
       |$paths
       |$schemata
     """.stripMargin
  }
}
