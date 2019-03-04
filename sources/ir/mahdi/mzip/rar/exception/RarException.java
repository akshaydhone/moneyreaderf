package ir.mahdi.mzip.rar.exception;

public class RarException extends Exception {
    private static final long serialVersionUID = 1;
    private RarExceptionType type;

    public enum RarExceptionType {
        notImplementedYet,
        crcError,
        notRarArchive,
        badRarArchive,
        unkownError,
        headerNotInArchive,
        wrongHeaderType,
        ioError,
        rarEncryptedException
    }

    public RarException(Exception e) {
        super(RarExceptionType.unkownError.name(), e);
        this.type = RarExceptionType.unkownError;
    }

    public RarException(RarException e) {
        super(e.getMessage(), e);
        this.type = e.getType();
    }

    public RarException(RarExceptionType type) {
        super(type.name());
        this.type = type;
    }

    public RarExceptionType getType() {
        return this.type;
    }

    public void setType(RarExceptionType type) {
        this.type = type;
    }
}
