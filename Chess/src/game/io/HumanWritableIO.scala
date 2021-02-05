package game.io
import java.io.BufferedReader
import java.io.IOException
import java.io.Reader
import game.Board
import game.Game
import game._
/**********************************************************************
 *
 *   This file is returned in Exercise 3.2
 *
 *   The idea is to read a file written manually by
 *   someone. There can be useless whitespace all around the file.
 *
 **********************************************************************/
object HumanWritableIO {
    def loadGame(input: Reader): Game = {
        /**
         * This is the game object this method will fill with data. The object
         * is returned when the file ends and everything is ok.
         */
        val board = new Board()
        val game  = new Game(board)
        /*
        You might want to keep track of different required parts of the file
         if some of these are missing, the file is not valid and a CorruptedChessFileException should be thrown
         If you figure out something better, you don't have to keep these variables.
        */
        var infoRead = false
        var whiteRead = false
        var blackRead = false
        
        var whitePieces = Array[Array[String]]()
        var blackPieces = Array[Array[String]]()
        var whiteName = Vector[String]()
        var blackName = Vector[String]()
        var lastRead = Vector[String]("juu")
        // BufferedReader allows us to read line by line (readLine method)
        val lineReader = new BufferedReader(input)
        try {
            /*
             * Read the first line, i.e. the header.
             * You can also use this variable for reading all the section headers,
             * or you can do better and use only vals and get rid of this.
             */
            var currentLine = lineReader.readLine().trim.toLowerCase
            // Process the header we just read.
            // NOTE: To test the line below you must test the class once with a
            // broken header
            if (!((currentLine startsWith "chess") && (currentLine endsWith "save file"))) {
                throw new CorruptedChessFileException("Unknown file type")
            }
            // The version information and the date are not used in this
            // exercise
            // *************************************************************
            
          while(currentLine != null) {
            
            if(!currentLine.isEmpty) {
              currentLine = currentLine.trim
                
              if(currentLine.toLowerCase.startsWith("white")) {
                val white = currentLine.split(":").map(_.trim)
                whiteName = whiteName :+ white(1)
              }
              else if(currentLine.toLowerCase.startsWith("black")) {
                val black = currentLine.split(":").map(_.trim)
                blackName = blackName :+ black(1)
              }
              else if(currentLine.toLowerCase.startsWith("#white")) {
                whiteRead = true
                lastRead = lastRead :+ "white"
              }
              else if(currentLine.toLowerCase.startsWith("#black")) {
                blackRead = true
                lastRead = lastRead :+ "black"
              }
              else if(currentLine.toLowerCase.startsWith("#")) {
                lastRead = lastRead :+ currentLine.drop(1)
              }
              else if(lastRead.last == "white") {
                if(currentLine.toLowerCase.startsWith("pawn") || currentLine.toLowerCase.startsWith("knight") || currentLine.toLowerCase.startsWith("king") || currentLine.toLowerCase.startsWith("queen") || currentLine.toLowerCase.startsWith("bishop") || currentLine.toLowerCase.startsWith("rook")) {
                  val piece = currentLine.split(":").map(_.trim)
                  whitePieces = whitePieces :+ piece
                }
                else throw new CorruptedChessFileException("Unknown file type")
              }
              else if(lastRead.last == "black") {
                if(currentLine.toLowerCase.startsWith("pawn") || currentLine.toLowerCase.startsWith("knight") || currentLine.toLowerCase.startsWith("king") || currentLine.toLowerCase.startsWith("queen") || currentLine.toLowerCase.startsWith("bishop") || currentLine.toLowerCase.startsWith("rook")) {
                  val piece = currentLine.split(":").map(_.trim)
                  blackPieces = blackPieces :+ piece
                }
                else throw new CorruptedChessFileException("Unknown file type")
              }
            }
            currentLine = lineReader.readLine()
            
          }
              
              if(whiteRead == false || blackRead == false) throw new CorruptedChessFileException("Unknown file type")
              else {
              
            val vek1 = Vector('a','b','c','d','e','f','g','h')
            val vek2 = Vector('1','2','3','4','5','6','7','8')
            val blackPlayer = new Player(blackName(0), Black)
            val whitePlayer = new Player(whiteName(0), White)
            
            for(piece <- whitePieces) {
              if(!vek1.exists(n => n == piece(1)(0)) || !vek2.exists(n => n == piece(1)(1))) throw new CorruptedChessFileException("Unknown file type")
              else if(piece(0).toLowerCase == "pawn" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Pawn(whitePlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "king" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new King(whitePlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "queen" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Queen(whitePlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "bishop" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Bishop(whitePlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "rook" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Rook(whitePlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "knight" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Knight(whitePlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else throw new CorruptedChessFileException("Unknown file type")
            }
            
             for(piece <- blackPieces) {
              if(!vek1.exists(n => n == piece(1)(0)) || !vek2.exists(n => n == piece(1)(1))) throw new CorruptedChessFileException("Unknown file type")    
              else if(piece(0).toLowerCase == "pawn" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Pawn(blackPlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "king" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new King(blackPlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "queen" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Queen(blackPlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "bishop" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Bishop(blackPlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "rook" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Rook(blackPlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else if(piece(0).toLowerCase == "knight" && board.isFree(piece(1)(0)-'a', piece(1)(1)-'1')) board.setPiece(new Knight(blackPlayer), piece(1)(0)-'a', piece(1)(1)-'1')
              else throw new CorruptedChessFileException("Unknown file type")
            }
            
             
             game.addPlayer(whitePlayer)
             game.addPlayer(blackPlayer)
             
             game
              }
            
            
            
            
            
            
            
            
            
            
            
            // EXERCISE
            //
            // ADD CODE HERE FOR READING THE
            // DATA FOLLOWING THE MAIN HEADERS
            //
            //
            // *************************************************************
            // If we reach this point the Game-object should now have the proper
            // players and
            // a fully set up chess board. Therefore we might as well return it.
            // If there is something missing, throw a CorruptedChessFileException
           
        } catch {
            case e: IOException =>
            // To test this part the stream would have to cause an
            // IOException. That's a bit complicated to test. Therefore we have
            // given you a "secret tool", class BrokenReader, which will throw
            // an IOException at a requested position in the stream.
            // Throw the exception inside any chunk, but not in the chunk
            // header.
            val chessExc = new CorruptedChessFileException("Reading the chess data failed.")
            // Append the information about the initial cause for use in
            // debugging. Otherwise the programmer cannot know the method or
            // line number causing the problem.
            chessExc.initCause(e)
            throw chessExc
        }
    }
}