package pro.sky.shelter.exception;

public class VolunteerNotFoundException extends RuntimeException{
    private final long id;
    public VolunteerNotFoundException(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
