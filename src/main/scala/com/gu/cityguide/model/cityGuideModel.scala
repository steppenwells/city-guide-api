package com.gu.cityguide.model


case class City(
  name: String,
  urlName: String,
  latitude: Option[Double],
  longitude: Option[Double],
  mapZoom: Int,
  picture: Option[String],
  intro: Option[String],
  whatsOnUrl: Option[String],
  transportUrl: Option[String],
  weatherUrl: Option[String],
  categories: List[Category]
)

case class Category(
  name: String,
  urlName: String,
  subCategories: List[SubCategory]
)

case class SubCategory(
  name: String,
  urlName: String,
  items: List[Item]
)

case class Item(
  name: String,
  urlName: String,
  url: Option[String],
  latitude: Option[Double],
  longitude: Option[Double],
  bubble: Option[String],
  image: Option[String],
  caption: Option[String],
  byline: Option[String],
  bylineUrl: Option[String],
  guardianArticleUrl: Option[String],
  guardianArticleTitle: Option[String],
  mapCentreLatitude: Option[Double],
  mapCentreLongitude: Option[Double],
  mapCentreZoom: Option[Int]
)