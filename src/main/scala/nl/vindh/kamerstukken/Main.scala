package nl.vindh.kamerstukken

// TODO: some dossiers (for example, 35168) do not get the right title
// TODO: println prints strange characters (for example, 35176)

object Main extends App {
  val usage =
    """
      |Usage: scala nl.vindh.kamerstukken.Main [--dossier num] [--dossiers numLow numHigh]
    """.stripMargin

  if(args.length == 0) println(usage)

  def nextOption(map: Map[String, Any], lst: List[String]): Map[String, Any] =
    lst match {
      case Nil => map
      case "--dossier" :: num :: tail => nextOption(map ++ Map("dossier" -> num.toInt), tail)
      case "--dossiers" :: numLow :: numHigh :: tail =>
        nextOption(map ++ Map("dossiers" -> (numLow.toInt, numHigh.toInt)), tail)
      case option :: _ => {
        println(s"Unknown option: $option")
        System.exit(1).asInstanceOf[Map[String, Any]]
      }
    }

  val parsedArgs = nextOption(Map(), args.toList)
println(parsedArgs)
  println(parsedArgs.get(""))
  parsedArgs.get("dossier") match {
    case Some(num: Int) => println(KamerstukkenScraper.scrapeOneDossier(num))
    case Some(x) => println(s"An error occured: $x")
    case None =>
  }

  parsedArgs.get("dossiers") match {
    case Some((numLow: Int, numHigh: Int)) => {
      val dossiers = KamerstukkenScraper.scrapeDossierRange(numLow, numHigh)
      println(dossiers.get.mkString("\n"))
      Thread.sleep(1000) // This is necessary for sbt not to truncate the output. // TODO: Bug?
    }
    case Some(x) => println(s"An error occured: $x")
    case None =>
  }
}
