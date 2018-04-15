package org.pi4jrest.pins.exceptions;

import org.pi4jrest.pins.model.BoardType;

public class BoardPinProviderNotFoundException extends RuntimeException {

    private BoardType boardType;

    public BoardPinProviderNotFoundException(BoardType boardType) {
        super("Pin provider not found for boardType: " + boardType);
        this.boardType = boardType;
    }

    public BoardType getBoardType() {
        return boardType;
    }
}
