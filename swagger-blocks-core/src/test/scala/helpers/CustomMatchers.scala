package helpers

import com.github.fge.jsonschema.core.report.ProcessingReport
import org.scalatest._
import matchers._

trait CustomMatchers {

  val beValidSwaggerJson =
    Matcher { (left: String) =>
      val validationReport: ProcessingReport = JsonValidator.validateWithReport(left)

      MatchResult(
        validationReport.isSuccess,
        s"${validationReport.toString}\n$left",
        s"'$left' is valid swagger json"
      )
    }
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers
