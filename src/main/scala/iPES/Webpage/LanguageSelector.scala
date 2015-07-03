package iPES.Webpage

import org.scalajs.dom
import org.scalajs.dom.raw._

import scala.scalajs.js.Date
import scalatags.JsDom.all._

class LanguageSelector(var lang: String = "en") {

    val container = dom.document.createElement("div")
    container.setAttribute("position", "absolute")
    container.innerHTML = "Language: "

    val langSelector = select(
        option(value := "en")("English"),
        option(value := "fr")("Français"),
        option(value := "de")("Deutsch")
    ).render

    if (dom.document.cookie.contains("lang=")) {
        val cookies = dom.document.cookie.split(';')
        for (i <- cookies) {
            var cookie = i
            while (cookie.charAt(0) == ' ') cookie = cookie.substring(1)
            if (cookie.indexOf("lang=") == 0) {
                val temp = cookie.substring(5, cookie.length)
                if (langSelector.options.map(element => element.value).contains(temp))
                    lang = temp
            }
        }
    }

    langSelector.value = lang

    langSelector.onchange = (e: Event) => {
        lang = langSelector.value
        var d = new Date()
        d.setTime(d.getTime() + 604800000)
        dom.document.cookie = "lang=" + lang + "; expires=" + d.toUTCString()
        reloadContent()
    }

    container.appendChild(langSelector)
    dom.document.body.appendChild(container)

    val xhr = new XMLHttpRequest()
    var contentFile: Document = _

    xhr.onreadystatechange = (e: Event) => {
        if (xhr.readyState == 4 && xhr.status == 200) {
            contentFile = xhr.responseXML
            reloadContent()
        }
    }
    xhr.open("GET", "content.xml", async = true)
    xhr.send()

    def reloadContent() = {
        val content = contentFile.getElementsByTagName("language")
        var node: Node = null
        for (i <- 0 to content.length - 1) {
            if (content.item(i).attributes.getNamedItem("lang").value == lang)
                node = content.item(i)
        }

        val children = node.childNodes
        for (i <- 0 to children.length - 1) {
            val newNode = children.item(i).cloneNode(deep = true)
            if (newNode != null) {
                if (newNode.localName == "content") {
                    val oldNode = dom.document.getElementById(newNode.attributes.getNamedItem("id").value)
                    if (oldNode != null) {
                        val tmp = dom.document.createElement("div")
                        tmp.appendChild(newNode)
                        oldNode.innerHTML = tmp.innerHTML
                    } else {
                        println("Couldn't find a node to replace with id " + newNode.attributes.getNamedItem("id").value)
                    }
                }
            }
        }
    }
}
