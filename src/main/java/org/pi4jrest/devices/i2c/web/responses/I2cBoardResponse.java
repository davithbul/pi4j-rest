package org.pi4jrest.devices.i2c.web.responses;

public class I2cBoardResponse {
    private final int boardNumber;
    private final int boardAddress;

    public I2cBoardResponse(int boardNumber, int boardAddress) {
        this.boardNumber = boardNumber;
        this.boardAddress = boardAddress;
    }

    public int getBoardNumber() {
        return boardNumber;
    }

    public int getBoardAddress() {
        return boardAddress;
    }
}
