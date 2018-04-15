package org.pi4jrest.pins.model;

public class RaspberryPiGpioPinDescription implements GpioPinDescription {
    private final int pinAddress;

    public RaspberryPiGpioPinDescription(int pinAddress) {
        this.pinAddress = pinAddress;
    }

    @Override
    public int getPinAddress() {
        return pinAddress;
    }

    @Override
    public BoardType getBoardType() {
        return BoardType.RASPBERRY_PI;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GpioPinDescription)) return false;

        GpioPinDescription that = (GpioPinDescription) o;

        if (getPinAddress() != that.getPinAddress()) return false;
        return getBoardType() == that.getBoardType();
    }

    @Override
    public int hashCode() {
        int result = getPinAddress();
        result = 31 * result + getBoardType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GpioPinDescription{" +
                "pinAddress=" + getPinAddress() +
                ", board=" + getBoardType() +
                '}';
    }
}
