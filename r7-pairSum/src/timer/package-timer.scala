
import java.lang.management.{ ManagementFactory, ThreadMXBean }

/**
 * A small package for measuring time usage of functions.
 * @author Tommi Junttila
 */
package object timer {
  val bean: ThreadMXBean = ManagementFactory.getThreadMXBean()
  def getCpuTime = if (bean.isCurrentThreadCpuTimeSupported()) bean.getCurrentThreadCpuTime() else 0
  /**
   * Runs the argument function f and measures the user+system time spent in it in seconds.
   * Accuracy depends on the system, preferably not used for runs taking less than 0.1 seconds.
   * Returns a pair consisting of
   * - the return value of the function call and
   * - the time spent in executing the function.
   */
  def measureCpuTime[T](f: => T): (T, Double) = {
    val start = getCpuTime
    val r = f
    val end = getCpuTime
    val t = (end - start) / 1000000000.0
    (r, t)
  }

  /**
   * The same as measureCpuTime but the function f is applied repeatedly
   * until a cumulative threshold time use is reached (currently 0.1 seconds).
   * The time returned is the cumulative time divided by the number of repetitions.
   * Therefore, better accuracy is obtained for very small run-times.
   * The function f should be side-effect free!
   */
  def measureCpuTimeRepeated[T](f: => T): (T, Double) = {
    val start = getCpuTime
    var end = start
    var runs = 0
    var r: Option[T] = None
    while (end - start < 100000000L) {
      runs += 1
      r = Some(f)
      end = getCpuTime
    }
    val t = (end - start) / (runs * 1000000000.0)
    (r.get, t)
  }
  
  /**
   * The same as measureCpuTime except that wall clock time is measured
   * (meaning that the other processes etc running in the same machine
   *  can effect the returned run-time quite a lot)
   * Better accuracy, can also be used for shorter runs.
   */
  def measureWallClockTime[T](f: => T): (T, Double) = {
    val start = System.nanoTime
    val r = f
    val end = System.nanoTime
    val t = (end - start) / 1000000000.0
    (r, t)
  }
}


