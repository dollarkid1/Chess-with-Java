package game.pieces;

import game.components.board.Board;
import game.components.board.Floor;
import game.gameExceptions.InvalidMoveException;
import game.properties.Colour;
import game.properties.Position;

import static game.properties.Colour.*;

public class Pawn extends Piece {

    private boolean hasMadeFirstMove;

    public Pawn(Colour colour, Position defaultPosition) {
        super(colour, defaultPosition);
    }

    public Pawn(Colour colour, Floor floor) {
        super(colour, floor);

    }

    @Override
    public void move(Floor destinationFloor, Board board) {
        boolean oneStepMove = isOneStepMove(destinationFloor);
        boolean twoStepMove = isTwoStepMove(destinationFloor);
        boolean occupiedFloor = destinationFloor.getCurrentOccupant() != null;
        boolean validMove = (hasMadeFirstMove && oneStepMove) || (!hasMadeFirstMove && (oneStepMove || twoStepMove))
                && !occupiedFloor;

        Piece enemy = null;
        boolean validCaptureMove = false;
        boolean matchingColours;

        if(occupiedFloor) {
            enemy = destinationFloor.getCurrentOccupant();
            matchingColours = enemy.getColour() == getColour();
            validCaptureMove = isValidCaptureMove(destinationFloor, matchingColours);
        }

        validateMove(validCaptureMove, validMove);

        if(!hasMadeFirstMove) {
            hasMadeFirstMove = true;
        }
        if(validCaptureMove){
            capture(enemy);
        }
        updateFloorsStatus(destinationFloor);
    }

    private void validateMove(boolean validCaptureMove, boolean validMove) {
        if(!validMove && !validCaptureMove) {
            throw new InvalidMoveException("Invalid move");
        }
    }

    private boolean isValidCaptureMove(Floor destinationFloor, boolean matchingColours) {
        boolean validCaptureMove;
        validCaptureMove = destinationFloor.getRank() - getCurrentFloor().getRank() == 1 &&
                (Math.abs(destinationFloor.getFile() - getCurrentFloor().getFile()) == 1)
                && !matchingColours;
        return validCaptureMove;
    }

    private boolean isTwoStepMove(Floor destinationFloor) {
        if(getColour() == BLACK) {
            return (destinationFloor.getRank() - getCurrentFloor().getRank() == 2) &&
                    (destinationFloor.getFile() - getCurrentFloor().getFile() == 0);
        }else{
            return (getCurrentFloor().getRank() - destinationFloor.getRank() == 2) &&
                    (destinationFloor.getFile() - getCurrentFloor().getFile() == 0);
        }
    }

    private boolean isOneStepMove(Floor destinationFloor) {
        if(getColour() == BLACK) {
            return (destinationFloor.getRank() - getCurrentFloor().getRank() == 1) &&
                    (destinationFloor.getFile() - getCurrentFloor().getFile() == 0);
        }else{
            return (getCurrentFloor().getRank() - destinationFloor.getRank() == 1) &&
                    (destinationFloor.getFile() - getCurrentFloor().getFile() == 0);
        }
    }


    public boolean hasMadeFirstMove(){
        return hasMadeFirstMove;
    }

    @Override
    public String toString() {
        return "Pawn\n" + super.toString();
    }

    @Override
    public String getPseudoName() {
        if(getColour() == Colour.BLACK)
            return "\u265F";
        else
            return "\u2659";
    }
}
