package nl.vindh.kamerstukken

import java.util.NoSuchElementException

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

import cats.implicits._

import scala.util.{Failure, Success, Try}

object KamerstukkenScraper {
  val browser = JsoupBrowser()
  def getDoc(num: Int): Try[browser.DocumentType] =
    Try {
      browser.get(s"https://zoek.officielebekendmakingen.nl/dossier/$num")
    }

  def scrapeDossierTitle(doc: browser.DocumentType): Try[String] =
    Try(doc >> text(".result-item-content")) match {
      case Success(title: String) if title.size > 0 => Success(title)
      case Success(_) => Failure(new NoSuchElementException)
      case f => f
    }

  def scrapeOneDossier(num: Int): Try[Option[Dossier]] =
    (for {
      doc <- getDoc(num)
      title <- scrapeDossierTitle(doc)
    } yield Some(Dossier(num, title)))
      .recover {
        case _: NoSuchElementException => None
      }

  def withLogging[U](interval: Int, logFun: Int => Unit): (Int => U) => (Int => U) = {
    f =>
      i => {
        if (i % interval == 0) logFun(i)
        f(i)
      }
  }

  def withDefaultLogging[U] = withLogging[U](100, i => println(s"Scraping dossier $i"))

  def scrapeDossierRange(numLow: Int, numHigh: Int): Try[List[Dossier]] =
    (numLow to numHigh)
      .map(withDefaultLogging(scrapeOneDossier)).toList
      .sequence
      .map(_.flatten)
}
