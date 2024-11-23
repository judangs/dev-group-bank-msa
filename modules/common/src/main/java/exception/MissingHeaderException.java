package exception;

public class MissingHeaderException extends RuntimeException {

    public MissingHeaderException() {
        super("부적절한 헤더 요청입니다.");
    }

    public MissingHeaderException(String message) {
        super(message);
    }
}
