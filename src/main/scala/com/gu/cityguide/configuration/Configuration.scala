package com.gu.cityguide.configuration

object Configuration {

  private def appIdDev = "231601780193590"    // gu-cityguide-dev app id
  private def appIdProd = "180444840287"      // gu-cityguide app id
  private def appIdCode = "280765171934707"   // gu-cityguide-code app id
    
  private def omnitureAccountNameDev = "guardiangu-facebookdev"
  private def omnitureAccountNameCode = "guardiangu-facebookdev"
  private def omnitureAccountNameProd = "guardiangu-cityguide,guardiangu-network"

  object Platform extends Enumeration {
     type Platform = Value
     val Dev, Code, Prod = Value
   }
  val environment = Platform.Dev;

  def appId = environment match {
    case Platform.Dev => appIdDev
    case Platform.Code => appIdCode
    case Platform.Prod => appIdProd
  }
  
  def omnitureAccountName = environment match {
    case Platform.Dev => omnitureAccountNameDev
    case Platform.Code => omnitureAccountNameCode
    case Platform.Prod => omnitureAccountNameProd
  }
  
}