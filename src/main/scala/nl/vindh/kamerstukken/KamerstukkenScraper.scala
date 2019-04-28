package nl.vindh.kamerstukken

import java.util.NoSuchElementException

import scala.concurrent.Future
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

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

  def scrapeOneDossier(num: Int): Try[Dossier] =
    for {
      doc <- getDoc(num)
      title <- scrapeDossierTitle(doc)
    } yield Dossier(num, title)

  def scrapeDossierRange(numLow: Int, numHigh: Int): Try[List[Dossier]] =
    (numLow to numHigh).foldLeft(Success(List[Dossier]()).asInstanceOf[Try[List[Dossier]]]) {
      case (Success(acc), nw) => {
        if(nw % 100 == 0) println(s"Scraping dossier $nw ...")
        scrapeOneDossier(nw) match {
          case Success(dossier) => Success(dossier :: acc)
          case Failure(_: NoSuchElementException) => Success(acc)
          case f: Failure[Dossier] => f.asInstanceOf[Failure[List[Dossier]]] // This is not nice!
        }
      }
      case (f, _) => f
    }
    /*for { // How to do this using a ?? monad transformer ??
      num <- numLow to numHigh
      dossier <- scrapeOneDossier(num)
    } yield dossier*/
}
