package pro.sky.shelter.exception;

public class DateNotFoundException extends RuntimeException {
    private final long id;
    public DateNotFoundException(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
