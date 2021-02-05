package game.io

import java.io.IOException
import java.io.Reader
import game.Board
import game.Game
import game._

/**********************************************************************
 * 
 *   This file is returned in Exercise 3.1
 * 
 *   The idea is to read a file written automatically by
 *   a program. This is a chunked format like PNG, TIF, MPEG, etc.
 *   explained in the exercise description
 * 
 **********************************************************************/

object ChunkIO {

    def loadGame(input: Reader): Game = {

        /**
         * This is the game object this method will fill with data. The object
         * is returned when the END chunk is reached.
         */

        val board = new Board()
        val game  = new Game(board)

        
        /*
         * Use these variables for reading all the file header, date and chunk headers.
         * 
         * HINT: check the helper methods in the end of this class, a few lines below we read the header
         *       as an example
         */

        var header = new Array[Char](8)
        var date   = new Array[Char](8)
        var chunkHeader = new Array[Char](5)

        try {

            // Read the file header and the save date
            
            Helpers.readFully(header, input);
            Helpers.readFully(date, input);

            // Process the data we just read.
            // NOTE: To test the line below you must test the class once with a broken header

            if (!header.mkString.startsWith("CHESS")) {
                throw new CorruptedChessFileException("Unknown file type");
            }

            // The version information and the date are not used in this
            // exercise
            val vek1 = Vector('a','b','c','d','e','f','g','h')
            val vek2 = Vector('K', 'Q', 'R', 'B', 'N')
            val vek3 = Vector('1','2','3','4','5','6','7','8','9')
            var whiteName = Vector[String]()
            var blackName = Vector[String]()
            var endRead = false
            var blackPieces = Vector[String]()
            var whitePieces = Vector[String]()
            
            
            while(endRead != true) {
            
              Helpers.readFully(chunkHeader, input)
            
              val tag = Helpers.extractChunkName(chunkHeader)
              val chunkSize = Helpers.extractChunkSize(chunkHeader)
              
              
              var repo = new Array[Char](chunkSize)
              
              
              Helpers.readFully(repo, input)
            
              var info = repo.take(2)
              repo = repo.drop(2)
            
              if(tag == "PLR") {
              
                if(info(0) == 'B') {  
                  blackName = blackName :+ repo.take(info(1).asDigit).mkString
                  repo = repo.drop(info(1).asDigit)
                  var jono = repo.mkString
                 
                  while(jono.size != 0) {
                
                    if(vek1.exists(_ == jono(0)) && vek3.exists(_ == jono(1))) { // CHEECKING IF PAWN
                      blackPieces = blackPieces :+ jono.take(2)
                      jono = jono.drop(2)
                    }
                    else if(vek2.exists(_ == jono(0)) && vek1.exists(_ == jono(1)) && vek3.exists(_ == jono(2))) {
                      blackPieces = blackPieces :+ jono.take(3)
                      jono = jono.drop(3)
                    }
                    else throw new CorruptedChessFileException("Unknown file type")
                  }
                }
                else {
                  whiteName = whiteName :+ repo.take(info(1).asDigit).mkString
                  repo = repo.drop(info(1).asDigit)
                  var jono = repo.mkString
                
                  while(jono.size != 0) {
                
                    if(vek1.exists(_ == jono(0)) && vek3.exists(_ == jono(1))) {
                      whitePieces = whitePieces :+ jono.take(2)
                      jono = jono.drop(2)
                    }
                    else if(vek2.exists(_ == jono(0)) && vek1.exists(_ == jono(1)) && vek3.exists(_ == jono(2))) {
                      whitePieces = whitePieces :+ jono.take(3)
                      jono = jono.drop(3)
                    }
                    else throw new CorruptedChessFileException("Unknown file type")
                  }
                }
                         
              }
            else if(tag == "END") {
                endRead = true
                input.close()
              // END OF WHILE LOOP
            } 
                
          }
            
          if(whiteName.isEmpty || blackName.isEmpty) throw new CorruptedChessFileException("Unknown file type")
          
          val blackPlayer = new Player(blackName(0), Black)
          val whitePlayer = new Player(whiteName(0), White)
            
          for(pieceInfo <- blackPieces) {
            if(pieceInfo.size == 2 && board.isFree(pieceInfo(0)-'a', pieceInfo(1)-'1')) {
            	board.setPiece(new Pawn(blackPlayer), pieceInfo(0)-'a', pieceInfo(1)-'1')
            }
            else {
              if(pieceInfo(0) == 'K' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new King(blackPlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'Q' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Queen(blackPlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'R' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Rook(blackPlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'B' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Bishop(blackPlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'N' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Knight(blackPlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else throw new CorruptedChessFileException("Unknown file type")
            }
          }
          for(pieceInfo <- whitePieces) {
            if(pieceInfo.size == 2 && board.isFree(pieceInfo(0)-'a', pieceInfo(1)-'1')) {
            	board.setPiece(new Pawn(whitePlayer), pieceInfo(0)-'a', pieceInfo(1)-'1')
            }
            else {
              if(pieceInfo(0) == 'K' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new King(whitePlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'Q' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Queen(whitePlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'R' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Rook(whitePlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'B' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Bishop(whitePlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else if(pieceInfo(0) == 'N' && board.isFree(pieceInfo(1)-'a', pieceInfo(2)-'1')) board.setPiece(new Knight(whitePlayer), pieceInfo(1)-'a', pieceInfo(2)-'1')
              else throw new CorruptedChessFileException("Unknown file type")
            }
          }
          
          game.addPlayer(whitePlayer)
          game.addPlayer(blackPlayer)
            
                              
          game
          
            

            // *************************************************************
            //
            // EXERCISE
            //
            // ADD CODE HERE FOR READING THE
            // DATA FOLLOWING THE MAIN HEADERS
            //
            //
            // *************************************************************
            
            // If we reach this point the Game-object should now have the proper players and
            // a fully set up chess board. Therefore we might as well return it.
            
           game;

        } catch {
            case e:IOException =>
        

            // To test this part the stream would have to cause an
            // IOException. That's a bit complicated to test. Therefore we have
            // given you a "secret tool", class BrokenReader, which will throw
            // an IOException at a requested position in the stream.
            // Throw the exception inside any chunk, but not in the chunk header.
            
            val chessExc = new CorruptedChessFileException("Reading the chess data failed.")

            // Append the information about the initial cause for use in
            // debugging. Otherwise the programmer cannot know the method or
            // line number causing the problem.

            chessExc.initCause(e)

            throw chessExc
        }
    }
    
    object Helpers {
    // HELPER METHODS -------------------------------------------------------

    /**
     * Given a chunk header (an array of 5 chars) will return the size of this
     * chunks data.
     * 
     * @param chunkHeader
     *            a chunk header to process
     * @return the size of this chunks data
     */

    def extractChunkSize(chunkHeader: Array[Char]):Int = {
			10 * chunkHeader(3).asDigit + chunkHeader(4).asDigit
    }

    /**
     * Given a chunk header (an array of 5 chars) will return the name of this
     * chunk as a 3-letter String.
     * 
     * @param chunkHeader
     *            a chunk header to process
     * @return the name of this chunk
     */
    def extractChunkName(chunkHeader: Array[Char]): String = {
        chunkHeader.take(3).mkString
    }

    /**
     * The read-method of the Reader class will occasionally read only part of
     * the characters that were requested. This method will repeatedly call read
     * to completely fill the given buffer. The size of the buffer tells the
     * algorithm how many bytes should be read.
     * 
     * @param result
     *            The result of the reading will be stored in this array.
     * @param input
     *            The character stream to read from
     * @throws IOException
     * @throws CorruptedChessFileException
     */
    def readFully(result: Array[Char], input: Reader) = {
        var cursor = 0

        while (cursor != result.length) {
            var numCharactersRead = input.read(result, cursor, result.length - cursor)

            // If the file end is reached before the buffer is filled
            // an exception is thrown.
            
            if (numCharactersRead == -1) {
                throw new CorruptedChessFileException("Unexpected end of file.")
            }

            cursor += numCharactersRead
        }

    }

}

}

