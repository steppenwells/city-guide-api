package com.gu.cityguide.model

import java.io.File
import io.Codec


object ModelLoader {

  implicit val codec = Codec("UTF-8")

  def loadCities: List[City] = {
    val source = scala.io.Source.fromFile("/home/swells/guardian/city-guide-api/cities.csv")
    val csvText = source mkString ""
    source close
    val csvData = CSV.parse(csvText)

    csvData.tail.map( parseCity )
  }

  def loadItems: List[Item] = {
    val itemSource = scala.io.Source.fromFile("/home/swells/guardian/city-guide-api/items.csv")
    val itemCsvText = itemSource mkString ""
    itemSource close
    val itemData = CSV.parse(itemCsvText)
    itemData.tail.flatMap( parseItem )

  }

  def parseCity(data: List[String]): City = {
    val id: String = data(2)
    new City(
      name = data(1),
      urlName = id,
      latitude = Option(data(3).toDouble),
      longitude = Option(data(4).toDouble),
      mapZoom = data(5) toInt,
      picture = Option(data(6)),
      intro = Option(data(7)),
      whatsOnUrl = Option(data(8)),
      transportUrl = Option(data(9)),
      weatherUrl = Option(data(10)),
      itemsUrl = "/" + id,
      categories = parseCategories(data, id)
    )
  }

  def parseCategories(data: List[String], cityId: String) = {

    List(
      Category("Where to stay", "Where to stay", "accomodation", "/" + cityId + "/accomodation", parseSubCategories(data, 11, 5,  "/" + cityId + "/accomodation")),
      Category("Eats", "Where to eat", "food", "/" + cityId + "/food", parseSubCategories(data, 21, 5, "/" + cityId + "/food")),
      Category("Nightlife", "Nightlife", "nightlife", "/" + cityId + "/nightlife", parseSubCategories(data, 31, 5, "/" + cityId + "/nightlife")),
      Category("Shopping", "Where to shop", "shopping", "/" + cityId + "/shopping", parseSubCategories(data, 41, 5, "/" + cityId + "/shopping")),
      Category("Arts", "Arts", "arts", "/" + cityId + "/arts", parseSubCategories(data, 51, 5, "/" + cityId + "/arts")),
      Category("Outdoors", "Outdoors", "outdoors", "/" + cityId + "/outdoors", parseSubCategories(data, 61, 5, "/" + cityId + "/outdoors"))
    )
  }

  def parseSubCategories(data: List[String], startIndex: Int, count: Int, itemQueryRoot: String) = {
    (0 until count).map { i =>
      // ghastly, it's getting late and it works
      if ((startIndex + i*2 +1) <= data.length) {
        SubCategory(
          name = data(startIndex + (2 * i)),
          urlName = data(startIndex + (2 * i)).replace(" ", ""),
          image = data(startIndex + (2 * i) + 1),
          itemsUrl = itemQueryRoot + "/" + data(startIndex + (2 * i)).replace(" ", "")
        )
      } else{
        SubCategory("", "", "", "")
      }
    }.filterNot(_.name == "").toList
  }

  def parseItem(data: List[String]) = {
    try{
      Some(Item(
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
      )
    } catch {
      case e => {
        println("ERROR parsing line:" )
        data foreach (println(_))
        None
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