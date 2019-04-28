package nl.vindh.kamerstukken

import scala.concurrent.Future
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import scala.util.{Success, Try}

object KamerstukkenScraper {


  def scrapeOneDossier(num: Int): Option[Dossier] = {
    val browser = JsoupBrowser()
    val doc = browser.get(s"https://zoek.officielebekendmakingen.nl/dossier/$num")
    println("AAAAAAAAAA")
    Try(doc >> text(".result-item-content")) match {
      case Success(title: String) if title.size > 0 => Some(Dossier(num, title))
      case _ => None
    }
  }


}
