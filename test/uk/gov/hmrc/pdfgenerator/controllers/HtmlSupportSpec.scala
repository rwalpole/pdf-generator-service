package uk.gov.hmrc.pdfgenerator.controllers

import org.scalatest.{FlatSpec, Matchers}
import play.api.data.FormError
import reactivemongo.bson.Macros.Annotations.Key


class HtmlSupportSpec extends FlatSpec with Matchers with HtmlSupport {


  val HTML = "<html><body><h1>Some Html</h1></body></html>"

  val HTML_WITH_SCRIPT_TAG = "<html><script>function(){};</script><body><h1>Some Html</h1></body></html>"

  val  MORE_HTML_WITH_SCRIPT = """
      |<H1>Foo Bar</H1>
      |<script lang='javascript'/>""".stripMargin

  "HtmlSupport" should
    "bind a single non empty form element" in {
    val form = createForm()

    form.bind(Map("html" -> HTML)).fold(_ => "errors", h => {
      h shouldBe HTML
    })

  }

  it should
    "create a binding error if the html is empty" in {
    val form = createForm()

    form.bind(Map("html" -> "")).fold(form => {
      form.errors.size == 1
      val errorsSeq = form.errors.map(formError => formError.messages).flatten
      errorsSeq.contains("error.required") shouldBe true
    }, _ => "html")

  }

  it should
    "create a binding error if the html is not present" in {
    val form = createForm()

    form.bind(Map("a" -> "b")).fold(form => {
      form.errors.size == 1
      val errorsSeq = form.errors.map(formError => formError.messages).flatten
      errorsSeq.contains("error.required") shouldBe true
    }, _ => "html")

  }

  it should
    "create a binding error if the html contains a script tag" in {
    val form = createForm()

    form.bind(Map("html" -> HTML_WITH_SCRIPT_TAG)).fold(form => {
      form.errors.size shouldBe 1
      val errorsSeq = form.errors.map(formError => formError.messages).flatten
      errorsSeq.contains(SCRIPT_ERROR_TAG) shouldBe true
    }, _ => "html")

  }

}
