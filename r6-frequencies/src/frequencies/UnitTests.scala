
package frequencies

import org.junit.Test
import org.junit.Assert._

/**
 * Some simple unit tests.
 */
class UnitTests {
  @Test def testEmptySeq() {
    assertEquals(Map[String, Int](), frequencies(Seq[String]()))
    assertEquals(Map[Int, Set[String]](), freqToItems(Seq[String]()))
    assertEquals(Set[Double](), mostFrequent(Seq[Double]()))
  }

  /* Sample text from Hamlet */
  val text = """To be, or not to be: that is the question:
Whether 'tis nobler in the mind to suffer
The slings and arrows of outrageous fortune,
Or to take arms against a sea of troubles,
And by opposing end them? To die: to sleep;
No more; and by a sleep to say we end
The heart-ache and the thousand natural shocks
That flesh is heir to, 'tis a consummation
Devoutly to be wish'd. To die, to sleep;
To sleep: perchance to dream: ay, there's the rub;
For in that sleep of death what dreams may come
When we have shuffled off this mortal coil,
Must give us pause: there's the respect
That makes calamity of so long life;
For who would bear the whips and scorns of time,
The oppressor's wrong, the proud man's contumely,
The pangs of despised love, the law's delay,
The insolence of office and the spurns
That patient merit of the unworthy takes,
When he himself might his quietus make
With a bare bodkin? who would fardels bear,
To grunt and sweat under a weary life,
But that the dread of something after death,
The undiscover'd country from whose bourn
No traveller returns, puzzles the will
And makes us rather bear those ills we have
Than fly to others that we know not of?
Thus conscience does make cowards of us all;
And thus the native hue of resolution
Is sicklied o'er with the pale cast of thought,
And enterprises of great pith and moment
With this regard their currents turn awry,
And lose the name of action. Soft you now!
The fair Ophelia! Nymph, in thy orisons
Be all my sins remember'd."""

  /* The words that occur in the text */
  lazy val words = text.split("[^a-zA-Z']+").toSeq.map(_.toLowerCase)

  @Test def testFrequencies() {
    val result = frequencies(words)
    assertEquals("Occurrences of 'and'", 12, result("and"))
    assertEquals("Occurrences of 'question'", 1, result("question"))
    assertEquals("Occurrences of 'sleep'", 5, result("sleep"))
  }
  @Test def testFreqToItems() {
    val result = freqToItems(words)
    assertEquals("Words that occur 5 times", Set("sleep", "a"), result(5))
    assertEquals("Words that occur 7 times", Set("that"), result(7))
  }
  /* No item should occur 10 times in the words sequence */
  @Test(expected = classOf[NoSuchElementException])
  def testFreqToItemsNone() {
    val result = freqToItems(words)
    result(10)
  }
  @Test def testMostFrequent() {
    val result = mostFrequent(words)
    assertEquals("Most frequent words", Set("the"), result)
  }
}


