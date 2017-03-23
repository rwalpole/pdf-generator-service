package uk.gov.hmrc.pdfgenerator.controllers
import play.api.data.Forms.{single, text}
import play.api.data.{Form, Mapping}
import play.api.data.validation._

import scala.util.matching.Regex

/**
  * Created by peter on 22/03/2017.
  */
trait HtmlSupport {

  private val noScriptRegex: Regex = ".?<script.*".r

  val SCRIPT_ERROR_TAG = "error.noScriptTagsAllowed"

  def noScriptTags(errorMessage: String = SCRIPT_ERROR_TAG): Constraint[String] = Constraint[String](SCRIPT_ERROR_TAG) { html =>
    val maybeInvalid: Option[Invalid] = noScriptRegex.findFirstIn(html)
      .map(_ => Invalid(ValidationError(errorMessage)))

    maybeInvalid.getOrElse(Valid)
  }


  val noScriptTags: Constraint[String] = noScriptTags()

  val html: Mapping[String] = text verifying (Constraints.nonEmpty , noScriptTags)

  def createForm() = {
    Form(
      single(
        "html" -> html
      )
    )
  }

}
