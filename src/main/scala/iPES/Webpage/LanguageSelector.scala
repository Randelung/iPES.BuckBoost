package iPES.Webpage

import org.scalajs.dom
import org.scalajs.dom.raw._

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
class LanguageSelector(div: HTMLDivElement) {

	// populate div with elements
	private val languageTag = dom.document.createElement("span").asInstanceOf[HTMLSpanElement]
	private val xhr = new XMLHttpRequest()
	private val langSelector = select().render
	private var contentFile: Document = _
	languageTag.setAttribute("id", "language")
	div.appendChild(languageTag)

	div.appendChild(langSelector)

	// sets cookie and reloads content from xml
	langSelector.onchange = (e: Event) => {
		val d = new Date()
		d.setTime(d.getTime() + 604800000)
		dom.document.cookie = "lang=" + langSelector.value + "; expires=" + d.toUTCString()
		reloadContent()
	}

	// reacts to reception of content xml
	xhr.onreadystatechange = (e: Event) => {
		if (xhr.readyState == 4 && xhr.status == 200) {
			contentFile = xhr.responseXML
			addOptions()

			if (dom.document.cookie.contains("lang=")) {
				val cookies = dom.document.cookie.split(';')
				for (i <- cookies) {
					var cookie = i
					while (cookie.charAt(0) == ' ') cookie = cookie.substring(1)
					if (cookie.indexOf("lang=") == 0) {
						val temp = cookie.substring(5, cookie.length)
						if (langSelector.options.map(element => element.value).contains(temp))
							langSelector.value = temp
					}
				}
			}

			reloadContent()
		}
	}
	xhr.open("GET", "content.xml", async = true)
	xhr.send()

	/* scans the received content for the selected language and replaces all corresponding nodes in the webpage with the
	   content from the xml, then triggers a MathJax refresh. */
	private def reloadContent(): Unit = {
		val content = contentFile.getElementsByTagName("language")
		var node: Node = null
		for (i <- 0 to content.length - 1) {
			if (content.item(i).attributes.getNamedItem("lang").value == langSelector.value)
				node = content.item(i)
		}

		val children = node.childNodes
		for (i <- 0 to children.length - 1) {
			val newNode = children.item(i).cloneNode(deep = true)
			if (newNode.nodeName == "content") {
				val oldNode = dom.document.getElementById(newNode.attributes.getNamedItem("id").value)
				if (oldNode != null) {
					oldNode.innerHTML = newNode.asInstanceOf[Element].innerHTML
				}
				else {
					println("Couldn't find a node to replace with id " + newNode.attributes.getNamedItem("id").value)
				}
			}
		}

		MathJaxHub.Queue(js.Array("Typeset", MathJaxHub))
	}

	// adds all language options contained in the received content xml to the language selector.
	private def addOptions(): Unit = {
		val content = contentFile.getElementsByTagName("language")
		for (index <- 0 to content.length - 1) {
			val optionsItem = dom.document.createElement("option").asInstanceOf[HTMLOptionElement]
			optionsItem.text = content.item(index).attributes.getNamedItem("humanReadable").value
			optionsItem.value = content.item(index).attributes.getNamedItem("lang").value
			langSelector.add(optionsItem)
		}
	}
}

/** Companion object for the class; handles single instance. */
@JSExport
object LanguageSelector {

	private var instantiated = false

	@JSExport
	def main(div: HTMLDivElement): Unit = {
		if (!instantiated) {
			new LanguageSelector(div)
			instantiated = true
		}
	}
}