package pro.sky.shelter.exception;

public class AnimalNotFoundException extends RuntimeException{
    private final long id;
    public AnimalNotFoundException(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
}
