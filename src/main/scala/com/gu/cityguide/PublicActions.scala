package com.gu.cityguide

import configuration.{Configuration}
import org.scalatra.ScalatraFilter
import org.slf4j.{LoggerFactory, Logger}
import net.liftweb.json._
import net.liftweb.json.Extraction._
import net.liftweb.json.DefaultFormats

class PublicActions extends ScalatraFilter {

  protected val log = LoggerFactory.getLogger(getClass)
  implicit val formats = DefaultFormats

  before() { contentType = "application/json" }

  get("/hello/*") {
    pretty(render(decompose(Map("hello"->"world"))))
  }

  error { case e => {
      log.error(e.toString)
      val stackTrace = e.getStackTraceString.split("\n") map { "\tat " + _ } mkString "\n"
      e.toString + "\n" + stackTrace
    }
  }

}
