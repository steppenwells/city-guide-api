package com.gu.cityguide.model

import java.io.File


object ModelLoader {

  def load {
    val source = scala.io.Source.fromFile("/home/swells/guardian/city-guide-api/cities.csv")
    val csvText = source mkString ""
    source close

    val csvData = CSV.parse(csvText)

    val cities = csvData.tail.map( parseCity _ )

    val itemSource = scala.io.Source.fromFile("/home/swells/guardian/city-guide-api/items.csv")
    val itemCsvText = itemSource mkString ""
    itemSource close
    val itemData = CSV.parse(itemCsvText)
    val items = itemData.tail.map( parseItem )

    println(items head)


  }

  def parseCity(data: List[String]) = {
    new City(
      name = data(1),
      urlName = data(2),
      latitude = Option(data(3).toDouble),
      longitude = Option(data(4).toDouble),
      mapZoom = data(5) toInt,
      picture = Option(data(6)),
      intro = Option(data(7)),
      whatsOnUrl = Option(data(8)),
      transportUrl = Option(data(9)),
      weatherUrl = Option(data(10)),
      categories = parseCategories(data)
    )
  }

  def parseCategories(data: List[String]) = {

    List(
      Category("Where to stay", "accomodation", parseSubCategories(data, 11, 5)),
      Category("Where to eat", "eats", parseSubCategories(data, 21, 5)),
      Category("Nightlife", "nightlife", parseSubCategories(data, 31, 5)),
      Category("Where to shop", "shopping", parseSubCategories(data, 41, 5)),
      Category("Arts", "arts", parseSubCategories(data, 51, 5)),
      Category("Outdoors", "outdoors", parseSubCategories(data, 61, 5))
    )
  }

  def parseSubCategories(data: List[String], startIndex: Int, count: Int) = {
    (0 until count).map { i =>
      // ghastly, it's getting late and it works
      if ((startIndex + i*2 +1) <= data.length) {
        SubCategory(
          name = data(startIndex + (2 * i)),
          urlName = data(startIndex + (2 * i)).replace(" ", ""),
          image = data(startIndex + (2 * i) + 1),
          items = Nil
        )
      } else{
        SubCategory("", "", "", Nil)
      }
    }.filterNot(_.name == "").toList
  }

  def parseItem(data: List[String]) = {
    try{ Item(
      city = data(0),
      category = data(1),
      subcategory = data(2),
      name = data(3),
      urlName = data(3).replace(" ", ""),
      url = noneIfEmpty (data, 4),
      latitude = noneIfEmpty(data, 5) map (_.toDouble),
      longitude = noneIfEmpty(data, 6) map (_.toDouble),
      description = noneIfEmpty(data, 7),
      image = noneIfEmpty(data, 8),
      caption = noneIfEmpty(data, 9),
      byline = noneIfEmpty(data, 10),
      bylineUrl = noneIfEmpty(data, 11),
      guardianArticleUrl = noneIfEmpty(data, 12),
      guardianArticleTitle = noneIfEmpty(data, 13),
      mapCentreLatitude = noneIfEmpty(data, 14) map (_.toDouble),
      mapCentreLongitude = noneIfEmpty(data, 15) map (_.toDouble),
      mapCentreZoom = noneIfEmpty(data, 16) map (_.toInt)
    )
    } catch {
      case e => {
        println("ERROR parsing line:" )
        data foreach (println(_))
      }

    }
  }


  def noneIfEmpty(data: List[String], i: Int) = {
    if (i >= data.length) {
      None
    } else {
      data(i) match {
        case null => None
        case "" => None
        case s => Some(s)
      }
    }
  }
}