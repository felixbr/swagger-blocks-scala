package helpers

import com.github.fge.jsonschema.core.report.ProcessingReport
import org.scalatest._
import matchers._

trait CustomMatchers {

  val beValidSwaggerJson =
    Matcher { (leftSideJson: String) =>
      val validationReport: ProcessingReport = JsonValidator.validateWithReport(leftSideJson)

      MatchResult(
        validationReport.isSuccess,
        s"${validationReport.toString}\n$leftSideJson",
        s"'$leftSideJson' is valid swagger json"
      )
    }
}

// Make them easy to import with:
// import CustomMatchers._
object CustomMatchers extends CustomMatchers
