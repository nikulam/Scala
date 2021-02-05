package genericInterval
import scala.math._

/*
 * Tässä tehtävässä on tarkoitus harjoitella olemassaolevan luokan muuttamista geneeriseksi.
 * Tällainen on suhteellisen normaali refaktorointitoimenpide, kun huomataan että vanhalla koodilla  
 * on käyttöä laajemminkin kuin aluksi arvasi.
 * 
 * Alkuperäisessä tehtävässä kierroksella 3, toteutettiin luokka Interval, joka käsitteli
 * aikaväliä kahden Moment-olion välillä. Tehtävässä toteutettiin lukuisia metodeja kuten
 * union, intersection, jne. jotka ovat yleisiä mille tahansa intervalleille.
 * 
 * Tässä tehtävässä kaivat siis vanhan Interval-luokkasi naftaliinista ja muokkaat siitä
 * parametrisoidun version GenericInterval[T]. Suurin osa Moment-luokan tarjoamasta
 * toiminnallisuudesta, jota Interval tarvitsi toimiakseen, liittyi Moment-olioiden vertailuun. 
 * Yleistämme tämänkin, tarjoamalla Ordering[T]-olion, jolla intervalli pystyy tekemään
 * tarkalleen samat vertailut.
 *
 * Ominaisuus, jota Ordering[T] ei voi tarjota, ja jota alkuperäinen Interval käytti,
 * on etäisyys kahden "hetken" välillä. Jätämme tämän tietoisesti pois luokasta.
 * Pituutta käytettiin luokassa muutenkin vain toString-metodin osana, joten se ei
 * vaikuta muihin metodeihin.
 */

/**
 * Each instance of the class `GenericInterval` represents an interval -- an inclusive range --
 * on a scale. An interval has a "start" and an "end", represented
 * as objects of type T. An interval always contains at least a single item.
 *
 * An interval object may be used represent a range of just about anything that has an Ordering.
 *
 * An `Interval` object is immutable after it has been created. That is, its state
 * can not be changed in any way.
 *
 * @param start the start of the interval (that is, the first item included in the interval)
 * @param end the end of the interval (that is, the last item included in the interval); equal to or higher than `start`
 */
class GenericInterval[T](val start: T, val end: T)(implicit order: Ordering[T]) {

	/**
	 * Returns a textual description of the interval.
	 * For instance, the interval from 1900 to 2013 is represented by the string `"1900...2013"`.
	 */

	override def toString = this.start + "..." + this.end

	/**
	 * Determines whether this interval is after the given item.
	 * This is only deemed to be the case if the entire interval comes after the
	 * given item.
	 *
	 * @param item the item being compared
	 * @return `true` if this entire interval is after the item, `false` in all other cases
	 */
	def isLaterThan(item: T): Boolean = {
	  order.lt(item, this.start)
	}

	/**
	 * Determines whether this interval is after than the given interval.
	 * This is only deemed to be the case if this entire interval comes after the
	 * given interval. That is, no overlap is allowed.
	 *
	 * @param another an interval
	 * @return `true` if this entire interval is after than the other, `false` in all other cases
	 */
	def isLaterThan(another: GenericInterval[T]): Boolean = {
	  order.lt(another.end, this.start)
	}

	/**
	 * Determines whether this interval contains the given item.
	 * (An interval also includes its start and end items.)
	 *
	 * @param item a single item
	 * @return a boolean value indicating if the item is inside this interval
	 */
	def contains(item: T): Boolean = {
	  order.gteq(item, this.start) && order.lteq(item, this.end)
	}

	/**
	 * Determines whether this interval contains the given interval.
	 * This is the case if and only if all items within the other interval are
	 * contained within this interval.
	 *
	 * @param another an interval
	 * @return `true` if this interval contains the other, `false` otherwise
	 */
	def contains(another: GenericInterval[T]): Boolean = {
	  order.gteq(another.start, this.start) && order.lteq(another.end, this.end)
	}

	/**
	 * Determines whether this interval overlaps (intersects) the given interval.
	 * This is the case if (and only if) one or more of the items within the other
	 * interval are contained within this interval. (Note: this also includes cases
	 * in which this interval is entirely contained within the other interval.)
	 *
	 * @param another an interval
	 * @return `true` if the intervals overlap, `false` otherwise
	 */
	def overlaps(another: GenericInterval[T]): Boolean = {
	  (order.gteq(this.start, another.start) && order.lteq(this.start, another.end)) || (order.gteq(this.end, another.start) && order.lteq(this.end, another.end))
	}

	/**
	 * Creates, and returns a reference to, a new `GenericInterval` object that represents the union
	 * of this interval with the given interval. That is, the starting item of the new interval
	 * is the starting item of one of the two original intervals, whichever is smaller.
	 * Similarly, the end of the new interval is the greater of the two original ends.
	 *
	 * The two original intervals may overlap, but are not required to do so.
	 *
	 * Examples: The union of the interval from 1995 to 2003 with the interval from 2000 to 2013 is
	 * a new interval from 1995 to 2013. The union of the interval from 2000 to 2001 with the interval
	 * from 1995 to 1997 is a new interval from 1995 to 2001.
	 *
	 * @param another an interval
	 * @return the union of the two intervals
	 */
	def union(another: GenericInterval[T]): GenericInterval[T] = {
	  new GenericInterval[T](order.min(this.start, another.start), order.max(this.end, another.end))
	}

	/**
	 * Creates, and returns a reference to, a new `GenericInterval` object that represents the intersection
	 * of this interval with the given interval. That is, the start of the new interval
	 * is the start of one of the two original intervals, whichever is greater.
	 * Similarly, the end of the new interval is the smaller of the two original ends.
	 *
	 * However, this method only produces a new interval in case the two original intervals overlap.
	 *
	 * Examples: The intersection of the interval from 1995 to 2003 with the interval from 2000 to 2013 is
	 * a new interval from 2000 to 2003. The intersection of the interval from 2000 to 2001 with the interval
	 * from 1995 to 1997 does not exist.
	 *
	 * @param another an interval
	 * @return the intersection of the two intervals, if one exists, in which case it is wrapped inside
	 *         a `Some` object. If no intersection exists, returns `None` instead.
	 * @see union
	 */
	def intersection(another: GenericInterval[T]): Option[GenericInterval[T]] = {
	  if(this.overlaps(another)) Some(new GenericInterval[T](order.max(this.start, another.start), order.min(this.end, another.end)))
	  else None
	}
}






