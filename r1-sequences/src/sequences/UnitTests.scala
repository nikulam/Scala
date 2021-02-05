
package sequences

import org.junit.Test
import org.junit.Assert._

/*
 * Unit tests for sequences.
 *
 */

class UnitTests {

  @Test def testTask1() {
    assertTrue("failure on input Array[Int]()",
               addOneToSeq(Array[Int]()).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array(-47)",
               addOneToSeq(Array(-47)).toSeq equals (Array(-46).toSeq))
    assertTrue("failure on input Array(-12)",
               addOneToSeq(Array(-12)).toSeq equals (Array(-11).toSeq))
    assertTrue("failure on input Array(-37)",
               addOneToSeq(Array(-37)).toSeq equals (Array(-36).toSeq))
    assertTrue("failure on input Array(-77, 8, 70)",
               addOneToSeq(Array(-77, 8, 70)).toSeq equals (Array(-76, 9, 71).toSeq))
    assertTrue("failure on input Array(69, 60, -97)",
               addOneToSeq(Array(69, 60, -97)).toSeq equals (Array(70, 61, -96).toSeq))
    assertTrue("failure on input Array(-43, -19, -13)",
               addOneToSeq(Array(-43, -19, -13)).toSeq equals (Array(-42, -18, -12).toSeq))
    assertTrue("failure on input Array(-60, 13, 83)",
               addOneToSeq(Array(-60, 13, 83)).toSeq equals (Array(-59, 14, 84).toSeq))
    assertTrue("failure on input Array(93, 26, -97)",
               addOneToSeq(Array(93, 26, -97)).toSeq equals (Array(94, 27, -96).toSeq))
    assertTrue("failure on input Array(50, 25, -9, 24, 40)",
               addOneToSeq(Array(50, 25, -9, 24, 40)).toSeq equals (Array(51, 26, -8, 25, 41).toSeq))
    assertTrue("failure on input Array(72, 59, -23, -45, -100)",
               addOneToSeq(Array(72, 59, -23, -45, -100)).toSeq equals (Array(73, 60, -22, -44, -99).toSeq))
    assertTrue("failure on input Array(-14, -10, 68, -54, -63)",
               addOneToSeq(Array(-14, -10, 68, -54, -63)).toSeq equals (Array(-13, -9, 69, -53, -62).toSeq))
    assertTrue("failure on input Array(5, 92, -80, -52, -80)",
               addOneToSeq(Array(5, 92, -80, -52, -80)).toSeq equals (Array(6, 93, -79, -51, -79).toSeq))
    assertTrue("failure on input Array(-90, -19, -62, 35, 45)",
               addOneToSeq(Array(-90, -19, -62, 35, 45)).toSeq equals (Array(-89, -18, -61, 36, 46).toSeq))
  }

  @Test def testTask2() {
    assertTrue("failure on input Array[Int]()",
               removeOdd(Array[Int]()).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array(46)",
               removeOdd(Array(46)).toSeq equals (Array(46).toSeq))
    assertTrue("failure on input Array(36)",
               removeOdd(Array(36)).toSeq equals (Array(36).toSeq))
    assertTrue("failure on input Array(51)",
               removeOdd(Array(51)).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array(98, 48, 44)",
               removeOdd(Array(98, 48, 44)).toSeq equals (Array(98, 48, 44).toSeq))
    assertTrue("failure on input Array(65, 13, 3)",
               removeOdd(Array(65, 13, 3)).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array(44, 62, 56)",
               removeOdd(Array(44, 62, 56)).toSeq equals (Array(44, 62, 56).toSeq))
    assertTrue("failure on input Array(95, 21, 89)",
               removeOdd(Array(95, 21, 89)).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array(90, 6, 11)",
               removeOdd(Array(90, 6, 11)).toSeq equals (Array(90, 6).toSeq))
    assertTrue("failure on input Array(63, 5, 22, 95, 19)",
               removeOdd(Array(63, 5, 22, 95, 19)).toSeq equals (Array(22).toSeq))
    assertTrue("failure on input Array(65, 48, 41, 93, 94)",
               removeOdd(Array(65, 48, 41, 93, 94)).toSeq equals (Array(48, 94).toSeq))
    assertTrue("failure on input Array(14, 12, 58, 62, 20)",
               removeOdd(Array(14, 12, 58, 62, 20)).toSeq equals (Array(14, 12, 58, 62, 20).toSeq))
    assertTrue("failure on input Array(32, 89, 96, 50, 22)",
               removeOdd(Array(32, 89, 96, 50, 22)).toSeq equals (Array(32, 96, 50, 22).toSeq))
    assertTrue("failure on input Array(89, 33, 10, 6, 92)",
               removeOdd(Array(89, 33, 10, 6, 92)).toSeq equals (Array(10, 6, 92).toSeq))
  }

  @Test def testTask3() {
    assertTrue("failure on input Array(0)",
               mySum(Array(0)) == 0)
    assertTrue("failure on input Array(36)",
               mySum(Array(36)) == 36)
    assertTrue("failure on input Array(51)",
               mySum(Array(51)) == 51)
    assertTrue("failure on input Array(98)",
               mySum(Array(98)) == 98)
    assertTrue("failure on input Array(48, 44, 65)",
               mySum(Array(48, 44, 65)) == 157)
    assertTrue("failure on input Array(13, 3, 44)",
               mySum(Array(13, 3, 44)) == 60)
    assertTrue("failure on input Array(62, 56, 95)",
               mySum(Array(62, 56, 95)) == 213)
    assertTrue("failure on input Array(21, 89, 90)",
               mySum(Array(21, 89, 90)) == 200)
    assertTrue("failure on input Array(6, 11, 63)",
               mySum(Array(6, 11, 63)) == 80)
    assertTrue("failure on input Array(5, 22, 95, 19, 65)",
               mySum(Array(5, 22, 95, 19, 65)) == 206)
    assertTrue("failure on input Array(48, 41, 93, 94, 14)",
               mySum(Array(48, 41, 93, 94, 14)) == 290)
    assertTrue("failure on input Array(12, 58, 62, 20, 32)",
               mySum(Array(12, 58, 62, 20, 32)) == 184)
    assertTrue("failure on input Array(89, 96, 50, 22, 89)",
               mySum(Array(89, 96, 50, 22, 89)) == 346)
    assertTrue("failure on input Array(33, 10, 6, 92, 20)",
               mySum(Array(33, 10, 6, 92, 20)) == 161)
  }

  @Test def testTask4() {
    assertTrue("failure on input Array(0, 3)",
               sumOfSquaresOfOdd(Array(0, 3)) == 9)
    assertTrue("failure on input Array(99, 48)",
               sumOfSquaresOfOdd(Array(99, 48)) == 9801)
    assertTrue("failure on input Array(66, 15)",
               sumOfSquaresOfOdd(Array(66, 15)) == 225)
    assertTrue("failure on input Array(44, 63)",
               sumOfSquaresOfOdd(Array(44, 63)) == 3969)
    assertTrue("failure on input Array(95, 23, 89, 90)",
               sumOfSquaresOfOdd(Array(95, 23, 89, 90)) == 17475)
    assertTrue("failure on input Array(6, 22, 97, 19)",
               sumOfSquaresOfOdd(Array(6, 22, 97, 19)) == 9770)
    assertTrue("failure on input Array(95, 94, 14, 12)",
               sumOfSquaresOfOdd(Array(95, 94, 14, 12)) == 9025)
    assertTrue("failure on input Array(33, 89, 96, 50)",
               sumOfSquaresOfOdd(Array(33, 89, 96, 50)) == 9010)
    assertTrue("failure on input Array(10, 7, 92, 20)",
               sumOfSquaresOfOdd(Array(10, 7, 92, 20)) == 49)
    assertTrue("failure on input Array(97, 98, 33, 20, 77, 43, 68, 47)",
               sumOfSquaresOfOdd(Array(97, 98, 33, 20, 77, 43, 68, 47)) == 20485)
    assertTrue("failure on input Array(82, 41, 8, 22, 59, 63, 90, 18)",
               sumOfSquaresOfOdd(Array(82, 41, 8, 22, 59, 63, 90, 18)) == 9131)
    assertTrue("failure on input Array(84, 93, 26, 56, 63, 99, 40, 65)",
               sumOfSquaresOfOdd(Array(84, 93, 26, 56, 63, 99, 40, 65)) == 26644)
    assertTrue("failure on input Array(81, 63, 73, 4, 7, 82, 59, 74)",
               sumOfSquaresOfOdd(Array(81, 63, 73, 4, 7, 82, 59, 74)) == 19389)
    assertTrue("failure on input Array(39, 13, 85, 45, 29, 96, 54, 6)",
               sumOfSquaresOfOdd(Array(39, 13, 85, 45, 29, 96, 54, 6)) == 11781)
  }

  @Test def testTask5() {
    assertTrue("failure on input Array(2, 0)",
               productOfNonzero(Array(2, 0)) == 2)
    assertTrue("failure on input Array(4, 0)",
               productOfNonzero(Array(4, 0)) == 4)
    assertTrue("failure on input Array(0, 2)",
               productOfNonzero(Array(0, 2)) == 2)
    assertTrue("failure on input Array(0, 1)",
               productOfNonzero(Array(0, 1)) == 1)
    assertTrue("failure on input Array(0, -5, 1, 0)",
               productOfNonzero(Array(0, -5, 1, 0)) == -5)
    assertTrue("failure on input Array(3, 0, 3, 2)",
               productOfNonzero(Array(3, 0, 3, 2)) == 18)
    assertTrue("failure on input Array(4, 0, 0, -4)",
               productOfNonzero(Array(4, 0, 0, -4)) == -16)
    assertTrue("failure on input Array(-1, 0, 3, 0)",
               productOfNonzero(Array(-1, 0, 3, 0)) == -3)
    assertTrue("failure on input Array(0, 3, 4, -3)",
               productOfNonzero(Array(0, 3, 4, -3)) == -36)
    assertTrue("failure on input Array(2, -1, -3, -5, 2, 0, 0, 3)",
               productOfNonzero(Array(2, -1, -3, -5, 2, 0, 0, 3)) == -180)
    assertTrue("failure on input Array(-5, 0, 2, -1, 3, 1, -3, -2)",
               productOfNonzero(Array(-5, 0, 2, -1, 3, 1, -3, -2)) == 180)
    assertTrue("failure on input Array(-1, -2, 0, 4, -2, 2, -4, 3)",
               productOfNonzero(Array(-1, -2, 0, 4, -2, 2, -4, 3)) == 384)
    assertTrue("failure on input Array(-2, 2, -5, 4, 0, -3, -4, -1)",
               productOfNonzero(Array(-2, 2, -5, 4, 0, -3, -4, -1)) == -960)
    assertTrue("failure on input Array(-3, 0, 0, 2, -4, -3, 0, 0)",
               productOfNonzero(Array(-3, 0, 0, 2, -4, -3, 0, 0)) == -72)
    assertTrue("failure on input Array(-3, -4, -1, 0, 1, 3, 2, 3)",
               productOfNonzero(Array(-3, -4, -1, 0, 1, 3, 2, 3)) == -216)
    assertTrue("failure on input Array(1, 3, 1, -4, 1, 0, -4, 1)",
               productOfNonzero(Array(1, 3, 1, -4, 1, 0, -4, 1)) == 48)
    assertTrue("failure on input Array(3, 3, 3, -2, 3, -4, -5, 0)",
               productOfNonzero(Array(3, 3, 3, -2, 3, -4, -5, 0)) == -3240)
    assertTrue("failure on input Array(1, 2, 2, 1, -1, -5, 2, 0)",
               productOfNonzero(Array(1, 2, 2, 1, -1, -5, 2, 0)) == 40)
    assertTrue("failure on input Array(1, 1, -3, -2, -5, -4, 0, -1)",
               productOfNonzero(Array(1, 1, -3, -2, -5, -4, 0, -1)) == -120)
  }

  @Test def testTask6() {
    assertTrue("failure on input Array(100, -28)",
               minOfSquaresAtLeast17OfPositive(Array(100, -28)) == 10000)
    assertTrue("failure on input Array(30, -12)",
               minOfSquaresAtLeast17OfPositive(Array(30, -12)) == 900)
    assertTrue("failure on input Array(-94, 73)",
               minOfSquaresAtLeast17OfPositive(Array(-94, 73)) == 5329)
    assertTrue("failure on input Array(90, 100)",
               minOfSquaresAtLeast17OfPositive(Array(90, 100)) == 8100)
    assertTrue("failure on input Array(-88, -78, 82, -90)",
               minOfSquaresAtLeast17OfPositive(Array(-88, -78, 82, -90)) == 6724)
    assertTrue("failure on input Array(79, -18, 86, 88)",
               minOfSquaresAtLeast17OfPositive(Array(79, -18, 86, 88)) == 6241)
    assertTrue("failure on input Array(50, -36, 78, 92)",
               minOfSquaresAtLeast17OfPositive(Array(50, -36, 78, 92)) == 2500)
    assertTrue("failure on input Array(-80, -88, 100, -60)",
               minOfSquaresAtLeast17OfPositive(Array(-80, -88, 100, -60)) == 10000)
    assertTrue("failure on input Array(94, -34, 95, 54)",
               minOfSquaresAtLeast17OfPositive(Array(94, -34, 95, 54)) == 2916)
    assertTrue("failure on input Array(32, -48, 20, 86, 48, -42, 64, -18)",
               minOfSquaresAtLeast17OfPositive(Array(32, -48, 20, 86, 48, -42, 64, -18)) == 400)
    assertTrue("failure on input Array(20, -54, 83, 74, 96, 68, 86, -48)",
               minOfSquaresAtLeast17OfPositive(Array(20, -54, 83, 74, 96, 68, 86, -48)) == 400)
    assertTrue("failure on input Array(-40, -54, -50, 35, 58, 26, 46, -92)",
               minOfSquaresAtLeast17OfPositive(Array(-40, -54, -50, 35, 58, 26, 46, -92)) == 676)
    assertTrue("failure on input Array(16, -24, -40, 30, -76, 70, -10, -42)",
               minOfSquaresAtLeast17OfPositive(Array(16, -24, -40, 30, -76, 70, -10, -42)) == 256)
    assertTrue("failure on input Array(18, -58, 68, 28, 88, 16, 80, 68)",
               minOfSquaresAtLeast17OfPositive(Array(18, -58, 68, 28, 88, 16, 80, 68)) == 256)
  }

  @Test def testTask7() {
    assertTrue("failure on input Array[(Int,Int)]()",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array[(Int,Int)]()).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array((46,36))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((46,36))).toSeq equals (Array(36).toSeq))
    assertTrue("failure on input Array((51,98))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((51,98))).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array((48,44))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((48,44))).toSeq equals (Array(44).toSeq))
    assertTrue("failure on input Array((65,3), (13,44))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((65,3), (13,44))).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array((62,95), (56,21))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((62,95), (56,21))).toSeq equals (Array(95, 21).toSeq))
    assertTrue("failure on input Array((89,6), (90,11))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((89,6), (90,11))).toSeq equals (Array(11).toSeq))
    assertTrue("failure on input Array((63,19), (5,65), (22,48), (95,41))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((63,19), (5,65), (22,48), (95,41))).toSeq equals (Array(48).toSeq))
    assertTrue("failure on input Array((93,58), (94,62), (14,20), (12,32))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((93,58), (94,62), (14,20), (12,32))).toSeq equals (Array(62, 20, 32).toSeq))
    assertTrue("failure on input Array((89,89), (96,33), (50,10), (22,6))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((89,89), (96,33), (50,10), (22,6))).toSeq equals (Array(33, 10, 6).toSeq))
    assertTrue("failure on input Array((92,40), (20,97), (17,97), (87,33))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((92,40), (20,97), (17,97), (87,33))).toSeq equals (Array(40, 97).toSeq))
    assertTrue("failure on input Array((20,46), (77,78), (43,66), (68,26))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((20,46), (77,78), (43,66), (68,26))).toSeq equals (Array(46, 26).toSeq))
    assertTrue("failure on input Array((60,59), (93,62), (84,90), (29,18), (82,23), (41,31), (8,60), (22,23))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((60,59), (93,62), (84,90), (29,18), (82,23), (41,31), (8,60), (22,23))).toSeq equals (Array(59, 90, 23, 60, 23).toSeq))
    assertTrue("failure on input Array((73,99), (87,40), (98,63), (84,44), (93,92), (26,66), (56,30), (63,23))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((73,99), (87,40), (98,63), (84,44), (93,92), (26,66), (56,30), (63,23))).toSeq equals (Array(63, 44, 66, 30).toSeq))
    assertTrue("failure on input Array((25,59), (64,74), (79,43), (63,36), (73,2), (4,18), (7,58), (81,38))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((25,59), (64,74), (79,43), (63,36), (73,2), (4,18), (7,58), (81,38))).toSeq equals (Array(74, 18).toSeq))
    assertTrue("failure on input Array((30,6), (39,77), (12,54), (85,81), (45,57), (29,13), (96,59), (53,21))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((30,6), (39,77), (12,54), (85,81), (45,57), (29,13), (96,59), (53,21))).toSeq equals (Array(6, 54, 59).toSeq))
    assertTrue("failure on input Array((84,38), (11,90), (94,81), (58,24), (90,23), (84,11), (97,3), (2,96))",
               sequenceOfSecondPartsWhoseFirstPartIsEven(Array((84,38), (11,90), (94,81), (58,24), (90,23), (84,11), (97,3), (2,96))).toSeq equals (Array(38, 81, 24, 23, 11, 96).toSeq))
  }

  @Test def testTask8() {
    assertTrue("failure on input Array((46,36))",
               sumOfProductsOfPairs(Array((46,36))) == 1656)
    assertTrue("failure on input Array((51,48), (98,44))",
               sumOfProductsOfPairs(Array((51,48), (98,44))) == 6760)
    assertTrue("failure on input Array((65,3), (13,44))",
               sumOfProductsOfPairs(Array((65,3), (13,44))) == 767)
    assertTrue("failure on input Array((62,95), (56,21))",
               sumOfProductsOfPairs(Array((62,95), (56,21))) == 7066)
    assertTrue("failure on input Array((89,63), (90,5), (6,22), (11,95))",
               sumOfProductsOfPairs(Array((89,63), (90,5), (6,22), (11,95))) == 7234)
    assertTrue("failure on input Array((19,93), (65,94), (48,14), (41,12))",
               sumOfProductsOfPairs(Array((19,93), (65,94), (48,14), (41,12))) == 9041)
    assertTrue("failure on input Array((58,89), (62,96), (20,50), (32,22))",
               sumOfProductsOfPairs(Array((58,89), (62,96), (20,50), (32,22))) == 12818)
    assertTrue("failure on input Array((89,92), (33,20), (10,17), (6,87))",
               sumOfProductsOfPairs(Array((89,92), (33,20), (10,17), (6,87))) == 9540)
    assertTrue("failure on input Array((40,20), (97,77), (97,43), (33,68))",
               sumOfProductsOfPairs(Array((40,20), (97,77), (97,43), (33,68))) == 14684)
    assertTrue("failure on input Array((46,82), (78,41), (66,8), (26,22), (60,59), (93,62), (84,90), (29,18))",
               sumOfProductsOfPairs(Array((46,82), (78,41), (66,8), (26,22), (60,59), (93,62), (84,90), (29,18))) == 25458)
    assertTrue("failure on input Array((23,93), (31,26), (60,56), (23,63), (73,99), (87,40), (98,63), (84,44))",
               sumOfProductsOfPairs(Array((23,93), (31,26), (60,56), (23,63), (73,99), (87,40), (98,63), (84,44))) == 28331)
    assertTrue("failure on input Array((92,73), (66,4), (30,7), (23,81), (25,59), (64,74), (79,43), (63,36))",
               sumOfProductsOfPairs(Array((92,73), (66,4), (30,7), (23,81), (25,59), (64,74), (79,43), (63,36))) == 20929)
    assertTrue("failure on input Array((2,45), (18,29), (58,96), (38,53), (30,6), (39,77), (12,54), (85,81))",
               sumOfProductsOfPairs(Array((2,45), (18,29), (58,96), (38,53), (30,6), (39,77), (12,54), (85,81))) == 18910)
    assertTrue("failure on input Array((57,90), (13,84), (59,97), (21,2), (84,38), (11,90), (94,81), (58,24))",
               sumOfProductsOfPairs(Array((57,90), (13,84), (59,97), (21,2), (84,38), (11,90), (94,81), (58,24))) == 25175)
  }

  @Test def testTask9() {
    assertTrue("failure on input a = Array[Int](), b = Array[Int]()",
               sumOfTwoDDimVectors(Array[Int](),Array[Int]()).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input a = Array(-47), b = Array(-12)",
               sumOfTwoDDimVectors(Array(-47),Array(-12)).toSeq equals (Array(-59).toSeq))
    assertTrue("failure on input a = Array(-37), b = Array(-77)",
               sumOfTwoDDimVectors(Array(-37),Array(-77)).toSeq equals (Array(-114).toSeq))
    assertTrue("failure on input a = Array(8), b = Array(70)",
               sumOfTwoDDimVectors(Array(8),Array(70)).toSeq equals (Array(78).toSeq))
    assertTrue("failure on input a = Array(69, 60, -97), b = Array(-43, -19, -13)",
               sumOfTwoDDimVectors(Array(69, 60, -97),Array(-43, -19, -13)).toSeq equals (Array(26, 41, -110).toSeq))
    assertTrue("failure on input a = Array(-60, 13, 83), b = Array(93, 26, -97)",
               sumOfTwoDDimVectors(Array(-60, 13, 83),Array(93, 26, -97)).toSeq equals (Array(33, 39, -14).toSeq))
    assertTrue("failure on input a = Array(50, 25, -9), b = Array(24, 40, 72)",
               sumOfTwoDDimVectors(Array(50, 25, -9),Array(24, 40, 72)).toSeq equals (Array(74, 65, 63).toSeq))
    assertTrue("failure on input a = Array(59, -23, -45), b = Array(-100, -14, -10)",
               sumOfTwoDDimVectors(Array(59, -23, -45),Array(-100, -14, -10)).toSeq equals (Array(-41, -37, -55).toSeq))
    assertTrue("failure on input a = Array(68, -54, -63), b = Array(5, 92, -80)",
               sumOfTwoDDimVectors(Array(68, -54, -63),Array(5, 92, -80)).toSeq equals (Array(73, 38, -143).toSeq))
    assertTrue("failure on input a = Array(-52, -80, -90, -19, -62), b = Array(35, 45, 56, -98, -66)",
               sumOfTwoDDimVectors(Array(-52, -80, -90, -19, -62),Array(35, 45, 56, -98, -66)).toSeq equals (Array(-17, -35, -34, -117, -128).toSeq))
    assertTrue("failure on input a = Array(28, 79, 69, 85, -2), b = Array(19, -45, 15, 25, 23)",
               sumOfTwoDDimVectors(Array(28, 79, 69, 85, -2),Array(19, -45, 15, 25, 23)).toSeq equals (Array(47, 34, 84, 110, 21).toSeq))
    assertTrue("failure on input a = Array(83, 34, -80, -45, 31), b = Array(-34, 59, 96, -28, 26)",
               sumOfTwoDDimVectors(Array(83, 34, -80, -45, 31),Array(-34, 59, 96, -28, 26)).toSeq equals (Array(49, 93, 16, -73, 57).toSeq))
    assertTrue("failure on input a = Array(74, -93, -36, 98, 43), b = Array(74, -26, 64, -45, 80)",
               sumOfTwoDDimVectors(Array(74, -93, -36, 98, 43),Array(74, -26, 64, -45, 80)).toSeq equals (Array(148, -119, 28, 53, 123).toSeq))
    assertTrue("failure on input a = Array(47, -42, 45, 41, -100), b = Array(85, 46, -63, -6, 14)",
               sumOfTwoDDimVectors(Array(47, -42, 45, 41, -100),Array(85, 46, -63, -6, 14)).toSeq equals (Array(132, 4, -18, 35, -86).toSeq))
  }

  @Test def testTask10() {
    assertTrue("failure on input a = Array(-2), b = Array(3)",
               innerProductOfTwoDDimVectors(Array(-2),Array(3)) == -6)
    assertTrue("failure on input a = Array(-37), b = Array(-77)",
               innerProductOfTwoDDimVectors(Array(-37),Array(-77)) == 2849)
    assertTrue("failure on input a = Array(8), b = Array(70)",
               innerProductOfTwoDDimVectors(Array(8),Array(70)) == 560)
    assertTrue("failure on input a = Array(69), b = Array(60)",
               innerProductOfTwoDDimVectors(Array(69),Array(60)) == 4140)
    assertTrue("failure on input a = Array(-97, -43, -19), b = Array(-13, -60, 13)",
               innerProductOfTwoDDimVectors(Array(-97, -43, -19),Array(-13, -60, 13)) == 3594)
    assertTrue("failure on input a = Array(83, 93, 26), b = Array(-97, 50, 25)",
               innerProductOfTwoDDimVectors(Array(83, 93, 26),Array(-97, 50, 25)) == -2751)
    assertTrue("failure on input a = Array(-9, 24, 40), b = Array(72, 59, -23)",
               innerProductOfTwoDDimVectors(Array(-9, 24, 40),Array(72, 59, -23)) == -152)
    assertTrue("failure on input a = Array(-45, -100, -14), b = Array(-10, 68, -54)",
               innerProductOfTwoDDimVectors(Array(-45, -100, -14),Array(-10, 68, -54)) == -5594)
    assertTrue("failure on input a = Array(-63, 5, 92), b = Array(-80, -52, -80)",
               innerProductOfTwoDDimVectors(Array(-63, 5, 92),Array(-80, -52, -80)) == -2580)
    assertTrue("failure on input a = Array(-90, -19, -62, 35, 45), b = Array(56, -98, -66, 28, 79)",
               innerProductOfTwoDDimVectors(Array(-90, -19, -62, 35, 45),Array(56, -98, -66, 28, 79)) == 5449)
    assertTrue("failure on input a = Array(69, 85, -2, 19, -45), b = Array(15, 25, 23, 83, 34)",
               innerProductOfTwoDDimVectors(Array(69, 85, -2, 19, -45),Array(15, 25, 23, 83, 34)) == 3161)
    assertTrue("failure on input a = Array(-80, -45, 31, -34, 59), b = Array(96, -28, 26, 74, -93)",
               innerProductOfTwoDDimVectors(Array(-80, -45, 31, -34, 59),Array(96, -28, 26, 74, -93)) == -13617)
    assertTrue("failure on input a = Array(-36, 98, 43, 74, -26), b = Array(64, -45, 80, 47, -42)",
               innerProductOfTwoDDimVectors(Array(-36, 98, 43, 74, -26),Array(64, -45, 80, 47, -42)) == 1296)
    assertTrue("failure on input a = Array(45, 41, -100, 85, 46), b = Array(-63, -6, 14, -34, -70)",
               innerProductOfTwoDDimVectors(Array(45, 41, -100, 85, 46),Array(-63, -6, 14, -34, -70)) == -10591)
  }

  @Test def testTask11() {
    assertTrue("failure on input a = Array(-2), b = Array(3)",
               squareOfDDimEuclideanDistance(Array(-2),Array(3)) == 25)
    assertTrue("failure on input a = Array(-37), b = Array(-77)",
               squareOfDDimEuclideanDistance(Array(-37),Array(-77)) == 1600)
    assertTrue("failure on input a = Array(8), b = Array(70)",
               squareOfDDimEuclideanDistance(Array(8),Array(70)) == 3844)
    assertTrue("failure on input a = Array(69), b = Array(60)",
               squareOfDDimEuclideanDistance(Array(69),Array(60)) == 81)
    assertTrue("failure on input a = Array(-97, -43, -19), b = Array(-13, -60, 13)",
               squareOfDDimEuclideanDistance(Array(-97, -43, -19),Array(-13, -60, 13)) == 8369)
    assertTrue("failure on input a = Array(83, 93, 26), b = Array(-97, 50, 25)",
               squareOfDDimEuclideanDistance(Array(83, 93, 26),Array(-97, 50, 25)) == 34250)
    assertTrue("failure on input a = Array(-9, 24, 40), b = Array(72, 59, -23)",
               squareOfDDimEuclideanDistance(Array(-9, 24, 40),Array(72, 59, -23)) == 11755)
    assertTrue("failure on input a = Array(-45, -100, -14), b = Array(-10, 68, -54)",
               squareOfDDimEuclideanDistance(Array(-45, -100, -14),Array(-10, 68, -54)) == 31049)
    assertTrue("failure on input a = Array(-63, 5, 92), b = Array(-80, -52, -80)",
               squareOfDDimEuclideanDistance(Array(-63, 5, 92),Array(-80, -52, -80)) == 33122)
    assertTrue("failure on input a = Array(-90, -19, -62, 35, 45), b = Array(56, -98, -66, 28, 79)",
               squareOfDDimEuclideanDistance(Array(-90, -19, -62, 35, 45),Array(56, -98, -66, 28, 79)) == 28778)
    assertTrue("failure on input a = Array(69, 85, -2, 19, -45), b = Array(15, 25, 23, 83, 34)",
               squareOfDDimEuclideanDistance(Array(69, 85, -2, 19, -45),Array(15, 25, 23, 83, 34)) == 17478)
    assertTrue("failure on input a = Array(-80, -45, 31, -34, 59), b = Array(96, -28, 26, 74, -93)",
               squareOfDDimEuclideanDistance(Array(-80, -45, 31, -34, 59),Array(96, -28, 26, 74, -93)) == 66058)
    assertTrue("failure on input a = Array(-36, 98, 43, 74, -26), b = Array(64, -45, 80, 47, -42)",
               squareOfDDimEuclideanDistance(Array(-36, 98, 43, 74, -26),Array(64, -45, 80, 47, -42)) == 32803)
    assertTrue("failure on input a = Array(45, 41, -100, 85, 46), b = Array(-63, -6, 14, -34, -70)",
               squareOfDDimEuclideanDistance(Array(45, 41, -100, 85, 46),Array(-63, -6, 14, -34, -70)) == 54486)
  }

  @Test def testTask12() {
    assertTrue("failure on input a = Array(-2), b = Array(3), j = 0",
               pairWithOffset(Array(-2),Array(3),0).toSeq equals (Array((-2,3)).toSeq))
    assertTrue("failure on input a = Array(-77), b = Array(8), j = 0",
               pairWithOffset(Array(-77),Array(8),0).toSeq equals (Array((-77,8)).toSeq))
    assertTrue("failure on input a = Array(69), b = Array(60), j = 0",
               pairWithOffset(Array(69),Array(60),0).toSeq equals (Array((69,60)).toSeq))
    assertTrue("failure on input a = Array(-43), b = Array(-19), j = 0",
               pairWithOffset(Array(-43),Array(-19),0).toSeq equals (Array((-43,-19)).toSeq))
    assertTrue("failure on input a = Array(-60, 13, 83), b = Array(93, 26, -97), j = 1",
               pairWithOffset(Array(-60, 13, 83),Array(93, 26, -97),1).toSeq equals (Array((-60,26), (13,-97)).toSeq))
    assertTrue("failure on input a = Array(25, -9, 24), b = Array(40, 72, 59), j = 1",
               pairWithOffset(Array(25, -9, 24),Array(40, 72, 59),1).toSeq equals (Array((25,72), (-9,59)).toSeq))
    assertTrue("failure on input a = Array(-45, -100, -14), b = Array(-10, 68, -54), j = 2",
               pairWithOffset(Array(-45, -100, -14),Array(-10, 68, -54),2).toSeq equals (Array((-45,-54)).toSeq))
    assertTrue("failure on input a = Array(5, 92, -80), b = Array(-52, -80, -90), j = 2",
               pairWithOffset(Array(5, 92, -80),Array(-52, -80, -90),2).toSeq equals (Array((5,-90)).toSeq))
    assertTrue("failure on input a = Array(-62, 35, 45), b = Array(56, -98, -66), j = 2",
               pairWithOffset(Array(-62, 35, 45),Array(56, -98, -66),2).toSeq equals (Array((-62,-66)).toSeq))
    assertTrue("failure on input a = Array(79, 69, 85, -2, 19), b = Array(-45, 15, 25, 23, 83), j = 1",
               pairWithOffset(Array(79, 69, 85, -2, 19),Array(-45, 15, 25, 23, 83),1).toSeq equals (Array((79,15), (69,25), (85,23), (-2,83)).toSeq))
    assertTrue("failure on input a = Array(-80, -45, 31, -34, 59), b = Array(96, -28, 26, 74, -93), j = 0",
               pairWithOffset(Array(-80, -45, 31, -34, 59),Array(96, -28, 26, 74, -93),0).toSeq equals (Array((-80,96), (-45,-28), (31,26), (-34,74), (59,-93)).toSeq))
    assertTrue("failure on input a = Array(98, 43, 74, -26, 64), b = Array(-45, 80, 47, -42, 45), j = 1",
               pairWithOffset(Array(98, 43, 74, -26, 64),Array(-45, 80, 47, -42, 45),1).toSeq equals (Array((98,80), (43,47), (74,-42), (-26,45)).toSeq))
    assertTrue("failure on input a = Array(-100, 85, 46, -63, -6), b = Array(14, -34, -70, 29, -36), j = 0",
               pairWithOffset(Array(-100, 85, 46, -63, -6),Array(14, -34, -70, 29, -36),0).toSeq equals (Array((-100,14), (85,-34), (46,-70), (-63,29), (-6,-36)).toSeq))
    assertTrue("failure on input a = Array(-91, -80, 8, 77, 56), b = Array(45, 9, -19, 30, 7), j = 1",
               pairWithOffset(Array(-91, -80, 8, 77, 56),Array(45, 9, -19, 30, 7),1).toSeq equals (Array((-91,9), (-80,-19), (8,30), (77,7)).toSeq))
  }

  @Test def testTask13() {
    assertTrue("failure on input Array(-2)",
               consecutivePositionPairs(Array(-2)).toSeq equals (Array[(Int,Int)]().toSeq))
    assertTrue("failure on input Array(-12, -37, -77)",
               consecutivePositionPairs(Array(-12, -37, -77)).toSeq equals (Array((-12,-37), (-37,-77)).toSeq))
    assertTrue("failure on input Array(8, 70, 69)",
               consecutivePositionPairs(Array(8, 70, 69)).toSeq equals (Array((8,70), (70,69)).toSeq))
    assertTrue("failure on input Array(60, -97, -43)",
               consecutivePositionPairs(Array(60, -97, -43)).toSeq equals (Array((60,-97), (-97,-43)).toSeq))
    assertTrue("failure on input Array(-19, -13, -60)",
               consecutivePositionPairs(Array(-19, -13, -60)).toSeq equals (Array((-19,-13), (-13,-60)).toSeq))
    assertTrue("failure on input Array(13, 83, 93)",
               consecutivePositionPairs(Array(13, 83, 93)).toSeq equals (Array((13,83), (83,93)).toSeq))
    assertTrue("failure on input Array(26, -97, 50, 25, -9)",
               consecutivePositionPairs(Array(26, -97, 50, 25, -9)).toSeq equals (Array((26,-97), (-97,50), (50,25), (25,-9)).toSeq))
    assertTrue("failure on input Array(24, 40, 72, 59, -23)",
               consecutivePositionPairs(Array(24, 40, 72, 59, -23)).toSeq equals (Array((24,40), (40,72), (72,59), (59,-23)).toSeq))
    assertTrue("failure on input Array(-45, -100, -14, -10, 68)",
               consecutivePositionPairs(Array(-45, -100, -14, -10, 68)).toSeq equals (Array((-45,-100), (-100,-14), (-14,-10), (-10,68)).toSeq))
    assertTrue("failure on input Array(-54, -63, 5, 92, -80)",
               consecutivePositionPairs(Array(-54, -63, 5, 92, -80)).toSeq equals (Array((-54,-63), (-63,5), (5,92), (92,-80)).toSeq))
    assertTrue("failure on input Array(-52, -80, -90, -19, -62)",
               consecutivePositionPairs(Array(-52, -80, -90, -19, -62)).toSeq equals (Array((-52,-80), (-80,-90), (-90,-19), (-19,-62)).toSeq))
    assertTrue("failure on input Array(35, 45, 56, -98, -66, 28, 79)",
               consecutivePositionPairs(Array(35, 45, 56, -98, -66, 28, 79)).toSeq equals (Array((35,45), (45,56), (56,-98), (-98,-66), (-66,28), (28,79)).toSeq))
    assertTrue("failure on input Array(69, 85, -2, 19, -45, 15, 25)",
               consecutivePositionPairs(Array(69, 85, -2, 19, -45, 15, 25)).toSeq equals (Array((69,85), (85,-2), (-2,19), (19,-45), (-45,15), (15,25)).toSeq))
    assertTrue("failure on input Array(23, 83, 34, -80, -45, 31, -34)",
               consecutivePositionPairs(Array(23, 83, 34, -80, -45, 31, -34)).toSeq equals (Array((23,83), (83,34), (34,-80), (-80,-45), (-45,31), (31,-34)).toSeq))
    assertTrue("failure on input Array(59, 96, -28, 26, 74, -93, -36)",
               consecutivePositionPairs(Array(59, 96, -28, 26, 74, -93, -36)).toSeq equals (Array((59,96), (96,-28), (-28,26), (26,74), (74,-93), (-93,-36)).toSeq))
    assertTrue("failure on input Array(98, 43, 74, -26, 64, -45, 80)",
               consecutivePositionPairs(Array(98, 43, 74, -26, 64, -45, 80)).toSeq equals (Array((98,43), (43,74), (74,-26), (-26,64), (64,-45), (-45,80)).toSeq))
  }

  @Test def testTask14() {
    assertTrue("failure on input Array(-47)",
               firstMaxPos(Array(-47)) == 0)
    assertTrue("failure on input Array(-12, -37)",
               firstMaxPos(Array(-12, -37)) == 0)
    assertTrue("failure on input Array(-77, 8)",
               firstMaxPos(Array(-77, 8)) == 1)
    assertTrue("failure on input Array(70, 69)",
               firstMaxPos(Array(70, 69)) == 0)
    assertTrue("failure on input Array(60, -97, -43, -19)",
               firstMaxPos(Array(60, -97, -43, -19)) == 0)
    assertTrue("failure on input Array(-13, -60, 13, 83)",
               firstMaxPos(Array(-13, -60, 13, 83)) == 3)
    assertTrue("failure on input Array(93, 26, -97, 50)",
               firstMaxPos(Array(93, 26, -97, 50)) == 0)
    assertTrue("failure on input Array(25, -9, 24, 40)",
               firstMaxPos(Array(25, -9, 24, 40)) == 3)
    assertTrue("failure on input Array(72, 59, -23, -45)",
               firstMaxPos(Array(72, 59, -23, -45)) == 0)
    assertTrue("failure on input Array(-100, -14, -10, 68, -54, -63, 5, 92)",
               firstMaxPos(Array(-100, -14, -10, 68, -54, -63, 5, 92)) == 7)
    assertTrue("failure on input Array(-80, -52, -80, -90, -19, -62, 35, 45)",
               firstMaxPos(Array(-80, -52, -80, -90, -19, -62, 35, 45)) == 7)
    assertTrue("failure on input Array(56, -98, -66, 28, 79, 69, 85, -2)",
               firstMaxPos(Array(56, -98, -66, 28, 79, 69, 85, -2)) == 6)
    assertTrue("failure on input Array(19, -45, 15, 25, 23, 83, 34, -80)",
               firstMaxPos(Array(19, -45, 15, 25, 23, 83, 34, -80)) == 5)
    assertTrue("failure on input Array(-45, 31, -34, 59, 96, -28, 26, 74)",
               firstMaxPos(Array(-45, 31, -34, 59, 96, -28, 26, 74)) == 4)
  }

  @Test def testTask15() {
    assertTrue("failure on input a = Array(-47, -12), b = Array(-37, -77)",
               sumAndDifferenceSeqs(Array(-47, -12),Array(-37, -77)) equals (Array(-84, -89).toSeq,Array(-10, 65).toSeq))
    assertTrue("failure on input a = Array(8, 70), b = Array(69, 60)",
               sumAndDifferenceSeqs(Array(8, 70),Array(69, 60)) equals (Array(77, 130).toSeq,Array(-61, 10).toSeq))
    assertTrue("failure on input a = Array(-97, -43), b = Array(-19, -13)",
               sumAndDifferenceSeqs(Array(-97, -43),Array(-19, -13)) equals (Array(-116, -56).toSeq,Array(-78, -30).toSeq))
    assertTrue("failure on input a = Array(-60, 13, 83), b = Array(93, 26, -97)",
               sumAndDifferenceSeqs(Array(-60, 13, 83),Array(93, 26, -97)) equals (Array(33, 39, -14).toSeq,Array(-153, -13, 180).toSeq))
    assertTrue("failure on input a = Array(50, 25, -9), b = Array(24, 40, 72)",
               sumAndDifferenceSeqs(Array(50, 25, -9),Array(24, 40, 72)) equals (Array(74, 65, 63).toSeq,Array(26, -15, -81).toSeq))
    assertTrue("failure on input a = Array(59, -23, -45), b = Array(-100, -14, -10)",
               sumAndDifferenceSeqs(Array(59, -23, -45),Array(-100, -14, -10)) equals (Array(-41, -37, -55).toSeq,Array(159, -9, -35).toSeq))
    assertTrue("failure on input a = Array(68, -54, -63), b = Array(5, 92, -80)",
               sumAndDifferenceSeqs(Array(68, -54, -63),Array(5, 92, -80)) equals (Array(73, 38, -143).toSeq,Array(63, -146, 17).toSeq))
    assertTrue("failure on input a = Array(-52, -80, -90), b = Array(-19, -62, 35)",
               sumAndDifferenceSeqs(Array(-52, -80, -90),Array(-19, -62, 35)) equals (Array(-71, -142, -55).toSeq,Array(-33, -18, -125).toSeq))
    assertTrue("failure on input a = Array(45, 56, -98, -66, 28), b = Array(79, 69, 85, -2, 19)",
               sumAndDifferenceSeqs(Array(45, 56, -98, -66, 28),Array(79, 69, 85, -2, 19)) equals (Array(124, 125, -13, -68, 47).toSeq,Array(-34, -13, -183, -64, 9).toSeq))
    assertTrue("failure on input a = Array(-45, 15, 25, 23, 83), b = Array(34, -80, -45, 31, -34)",
               sumAndDifferenceSeqs(Array(-45, 15, 25, 23, 83),Array(34, -80, -45, 31, -34)) equals (Array(-11, -65, -20, 54, 49).toSeq,Array(-79, 95, 70, -8, 117).toSeq))
    assertTrue("failure on input a = Array(59, 96, -28, 26, 74), b = Array(-93, -36, 98, 43, 74)",
               sumAndDifferenceSeqs(Array(59, 96, -28, 26, 74),Array(-93, -36, 98, 43, 74)) equals (Array(-34, 60, 70, 69, 148).toSeq,Array(152, 132, -126, -17, 0).toSeq))
    assertTrue("failure on input a = Array(-26, 64, -45, 80, 47), b = Array(-42, 45, 41, -100, 85)",
               sumAndDifferenceSeqs(Array(-26, 64, -45, 80, 47),Array(-42, 45, 41, -100, 85)) equals (Array(-68, 109, -4, -20, 132).toSeq,Array(16, 19, -86, 180, -38).toSeq))
    assertTrue("failure on input a = Array(46, -63, -6, 14, -34), b = Array(-70, 29, -36, -59, -91)",
               sumAndDifferenceSeqs(Array(46, -63, -6, 14, -34),Array(-70, 29, -36, -59, -91)) equals (Array(-24, -34, -42, -45, -125).toSeq,Array(116, -92, 30, 73, 57).toSeq))
  }

  @Test def testTask16() {
    assertTrue("failure on input Array[String]()",
               stringsConcatenated(Array[String]()) equals "")
    assertTrue("failure on input Array(\"\")",
               stringsConcatenated(Array("")) equals "")
    assertTrue("failure on input Array(\"wy\", \"y\")",
               stringsConcatenated(Array("wy", "y")) equals "wyy")
    assertTrue("failure on input Array(\"ww\", \"x\")",
               stringsConcatenated(Array("ww", "x")) equals "wwx")
    assertTrue("failure on input Array(\"yy\", \"\")",
               stringsConcatenated(Array("yy", "")) equals "yy")
    assertTrue("failure on input Array(\"\", \"xw\", \"yx\", \"\")",
               stringsConcatenated(Array("", "xw", "yx", "")) equals "xwyx")
    assertTrue("failure on input Array(\"zzy\", \"w\", \"zw\", \"\")",
               stringsConcatenated(Array("zzy", "w", "zw", "")) equals "zzywzw")
    assertTrue("failure on input Array(\"xwy\", \"z\", \"y\", \"\")",
               stringsConcatenated(Array("xwy", "z", "y", "")) equals "xwyzy")
    assertTrue("failure on input Array(\"xw\", \"wzy\", \"x\", \"\")",
               stringsConcatenated(Array("xw", "wzy", "x", "")) equals "xwwzyx")
    assertTrue("failure on input Array(\"wxy\", \"\", \"z\", \"\")",
               stringsConcatenated(Array("wxy", "", "z", "")) equals "wxyz")
    assertTrue("failure on input Array(\"yzw\", \"yzz\", \"\", \"\", \"y\", \"y\", \"wxy\", \"yzx\")",
               stringsConcatenated(Array("yzw", "yzz", "", "", "y", "y", "wxy", "yzx")) equals "yzwyzzyywxyyzx")
    assertTrue("failure on input Array(\"zxx\", \"ww\", \"\", \"\", \"zxw\", \"\", \"\", \"z\")",
               stringsConcatenated(Array("zxx", "ww", "", "", "zxw", "", "", "z")) equals "zxxwwzxwz")
    assertTrue("failure on input Array(\"y\", \"xz\", \"zyz\", \"yy\", \"yz\", \"\", \"y\", \"x\")",
               stringsConcatenated(Array("y", "xz", "zyz", "yy", "yz", "", "y", "x")) equals "yxzzyzyyyzyx")
    assertTrue("failure on input Array(\"xw\", \"\", \"yx\", \"z\", \"yy\", \"xyw\", \"xzy\", \"yyz\")",
               stringsConcatenated(Array("xw", "", "yx", "z", "yy", "xyw", "xzy", "yyz")) equals "xwyxzyyxywxzyyyz")
    assertTrue("failure on input Array(\"xwx\", \"yx\", \"yy\", \"y\", \"zz\", \"xy\", \"\", \"yzw\")",
               stringsConcatenated(Array("xwx", "yx", "yy", "y", "zz", "xy", "", "yzw")) equals "xwxyxyyyzzxyyzw")
  }

  @Test def testTask17() {
    assertTrue("failure on input Array[Int]()",
               runningSum(Array[Int]()).toSeq equals (Array[Int]().toSeq))
    assertTrue("failure on input Array(-47)",
               runningSum(Array(-47)).toSeq equals (Array(-47).toSeq))
    assertTrue("failure on input Array(-12)",
               runningSum(Array(-12)).toSeq equals (Array(-12).toSeq))
    assertTrue("failure on input Array(-37)",
               runningSum(Array(-37)).toSeq equals (Array(-37).toSeq))
    assertTrue("failure on input Array(-77, 8, 70)",
               runningSum(Array(-77, 8, 70)).toSeq equals (Array(-77, -69, 1).toSeq))
    assertTrue("failure on input Array(69, 60, -97)",
               runningSum(Array(69, 60, -97)).toSeq equals (Array(69, 129, 32).toSeq))
    assertTrue("failure on input Array(-43, -19, -13)",
               runningSum(Array(-43, -19, -13)).toSeq equals (Array(-43, -62, -75).toSeq))
    assertTrue("failure on input Array(-60, 13, 83)",
               runningSum(Array(-60, 13, 83)).toSeq equals (Array(-60, -47, 36).toSeq))
    assertTrue("failure on input Array(93, 26, -97)",
               runningSum(Array(93, 26, -97)).toSeq equals (Array(93, 119, 22).toSeq))
    assertTrue("failure on input Array(50, 25, -9, 24, 40)",
               runningSum(Array(50, 25, -9, 24, 40)).toSeq equals (Array(50, 75, 66, 90, 130).toSeq))
    assertTrue("failure on input Array(72, 59, -23, -45, -100)",
               runningSum(Array(72, 59, -23, -45, -100)).toSeq equals (Array(72, 131, 108, 63, -37).toSeq))
    assertTrue("failure on input Array(-14, -10, 68, -54, -63)",
               runningSum(Array(-14, -10, 68, -54, -63)).toSeq equals (Array(-14, -24, 44, -10, -73).toSeq))
    assertTrue("failure on input Array(5, 92, -80, -52, -80)",
               runningSum(Array(5, 92, -80, -52, -80)).toSeq equals (Array(5, 97, 17, -35, -115).toSeq))
    assertTrue("failure on input Array(-90, -19, -62, 35, 45)",
               runningSum(Array(-90, -19, -62, 35, 45)).toSeq equals (Array(-90, -109, -171, -136, -91).toSeq))
  }

  @Test def testTask18() {
    assertTrue("failure on input in = Array(-47), w = 1",
               movingSum(Array(-47), 1).toSeq equals (Array(-47).toSeq))
    assertTrue("failure on input in = Array(-12), w = 1",
               movingSum(Array(-12), 1).toSeq equals (Array(-12).toSeq))
    assertTrue("failure on input in = Array(-37), w = 1",
               movingSum(Array(-37), 1).toSeq equals (Array(-37).toSeq))
    assertTrue("failure on input in = Array(-77, 8, 70), w = 2",
               movingSum(Array(-77, 8, 70), 2).toSeq equals (Array(-69, 78).toSeq))
    assertTrue("failure on input in = Array(69, 60, -97), w = 1",
               movingSum(Array(69, 60, -97), 1).toSeq equals (Array(69, 60, -97).toSeq))
    assertTrue("failure on input in = Array(-43, -19, -13), w = 1",
               movingSum(Array(-43, -19, -13), 1).toSeq equals (Array(-43, -19, -13).toSeq))
    assertTrue("failure on input in = Array(-60, 13, 83), w = 2",
               movingSum(Array(-60, 13, 83), 2).toSeq equals (Array(-47, 96).toSeq))
    assertTrue("failure on input in = Array(93, 26, -97), w = 2",
               movingSum(Array(93, 26, -97), 2).toSeq equals (Array(119, -71).toSeq))
    assertTrue("failure on input in = Array(50, 25, -9, 24, 40), w = 1",
               movingSum(Array(50, 25, -9, 24, 40), 1).toSeq equals (Array(50, 25, -9, 24, 40).toSeq))
    assertTrue("failure on input in = Array(72, 59, -23, -45, -100), w = 4",
               movingSum(Array(72, 59, -23, -45, -100), 4).toSeq equals (Array(63, -109).toSeq))
    assertTrue("failure on input in = Array(-14, -10, 68, -54, -63), w = 4",
               movingSum(Array(-14, -10, 68, -54, -63), 4).toSeq equals (Array(-10, -59).toSeq))
    assertTrue("failure on input in = Array(5, 92, -80, -52, -80), w = 3",
               movingSum(Array(5, 92, -80, -52, -80), 3).toSeq equals (Array(17, -40, -212).toSeq))
    assertTrue("failure on input in = Array(-90, -19, -62, 35, 45), w = 1",
               movingSum(Array(-90, -19, -62, 35, 45), 1).toSeq equals (Array(-90, -19, -62, 35, 45).toSeq))
  }
}


