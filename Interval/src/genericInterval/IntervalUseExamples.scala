package genericInterval

/*
 * Tämä objekti on annettu esimerkkinä GenericInterval-luokan testaamisesta.
 * Arvostelutestauksessa käytetään laajempia testejä, joten tässä kuvattujen
 * operaatioiden onnistuminen ei vielä takaa täysiä pisteitä. Voit lisätä
 * omia kokeiluja vapaasti, sillä tätä tiedostoa ei palauteta.
 */

object IntervalUseExamples extends App {

	val decade = new GenericInterval(1990, 2000)
	val years = new GenericInterval(1994, 2003)
	val names = new GenericInterval("Matti", "Pekka")
	val otherNames = new GenericInterval("Eeva", "Otto")

	import java.util.{ GregorianCalendar => Cal }

	def makeTime(day: Int, month: Int, year: Int) = new Cal(year, month - 1, day).getTime()

	val dates = new GenericInterval(makeTime(12, 1, 1995), makeTime(22, 3, 1995))
	val dates2 = new GenericInterval(makeTime(17, 1, 1994), makeTime(15, 2, 1995))

	/*
  * --String interpolation--
  *
  * s"variable $x is greater than ${y(z)}"
  *    is shorthand for
  * "variable " + x + " is greater than " + y(z)
  */

	println(s"Does $decade contain 1995?  ${decade.contains(1995)}")
	println(s"The intersection of $decade and $years is ${decade.intersection(years)}")

	println(s"Does $names contain Anneli? ${names.contains("Anneli")}")
	println(s"Does $names contain Niilo? ${names.contains("Niilo")}")
	println(s"The intersection of $names and $otherNames is ${names.intersection(otherNames)}")

	println(s"The intersection of\n\t$dates\n\t\tand\n\t$dates2 \n\t\tis\n\t${dates.intersection(dates2)}")
}