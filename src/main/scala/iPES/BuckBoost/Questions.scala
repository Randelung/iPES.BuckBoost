package iPES.BuckBoost

import org.scalajs.dom
import org.scalajs.dom.raw.{HTMLDivElement, HTMLInputElement}

import scala.scalajs.js.annotation.JSExport

/**
 * Checks Question section for correct answer.
 */
@JSExport
object Questions {

	/** Method to check the answer to the question section section.
	  *
	  * This method is called directly from the webpage and can be located in any file, as long as `@JSExport` is
	  * annotated. By not revealing the correct answer in the IDs the solution remains slightly more hidden.
	  */
	@JSExport
	def checkAnswer1() = {
		if (dom.document.getElementById("answer4").asInstanceOf[HTMLInputElement].checked) {
			var feedback = dom.document.getElementById("feedback1right").asInstanceOf[HTMLDivElement]
			feedback.style.setProperty("display", "initial")
			feedback = dom.document.getElementById("feedback1wrong").asInstanceOf[HTMLDivElement]
			feedback.style.setProperty("display", "none")
		}
		else {
			var feedback = dom.document.getElementById("feedback1right").asInstanceOf[HTMLDivElement]
			feedback.style.setProperty("display", "none")
			feedback = dom.document.getElementById("feedback1wrong").asInstanceOf[HTMLDivElement]
			feedback.style.setProperty("display", "initial")
		}
	}
}
