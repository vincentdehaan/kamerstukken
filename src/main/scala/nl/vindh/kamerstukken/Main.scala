package nl.vindh.kamerstukken

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
    case x => println(s"An error occured: $x")
  }
}
