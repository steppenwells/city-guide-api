package com.gu.cityguide

import configuration.{Configuration}
import model.ModelLoader
import org.scalatra.ScalatraFilter
import org.slf4j.{LoggerFactory, Logger}
import net.liftweb.json._
import net.liftweb.json.Extraction._
import net.liftweb.json.DefaultFormats

class PublicActions extends ScalatraFilter {

  protected val log = LoggerFactory.getLogger(getClass)
  implicit val formats = DefaultFormats

  val cities = ModelLoader.loadCities
  val items = ModelLoader.loadItems

  before() { contentType = "application/json" }

  get("/cities") {
    pretty(render(decompose(cities)))
  }

  get("/city/:city") {
    println(params("city") + " categories request")
    val city = cities.find(_.urlName == params("city"))
    city match {
      case Some(c) => pretty(render(decompose(c.categories)))
      case None => halt(404)
    }
  }

  get("/city/:city/:cat/subcategories") {
    val city = cities.find(_.urlName == params("city"))
    val category = city.flatMap(_.categories.find(_.urlName == params("cat")))
    category match {
      case Some(c) => pretty(render(decompose(c.subCategories)))
      case None => halt(404)
    }
  }

  get("/city/:city/:cat/:subcat") {
    val city = cities.find(_.urlName == params("city"))
    val category = city.flatMap(_.categories.find(_.urlName == params("cat")))
    val subCategory = category.flatMap(_.subCategories.find(_.urlName == params("subcat")))

    (city, category, subCategory) match {
      case (Some(c), Some(cat), Some(sub)) => {
        pretty(render(decompose(items
          .filter(_.city == c.name)
          .filter(_.category == cat.id)
          .filter(_.subcategory == sub.name))))
      }
      case _ => halt(404)
    }

  }

  get("/city/:city/:cat") {
    val city = cities.find(_.urlName == params("city"))
    val category = city.flatMap(_.categories.find(_.urlName == params("cat")))

    (city, category) match {
      case (Some(c), Some(cat)) => {
        pretty(render(decompose(items
          .filter(_.city == c.name)
          .filter(_.category == cat.id))))
      }
      case _ => halt(404)
    }

  }

  error { case e => {
      log.error(e.toString)
      val stackTrace = e.getStackTraceString.split("\n") map { "\tat " + _ } mkString "\n"
      e.toString + "\n" + stackTrace
    }
  }

}
