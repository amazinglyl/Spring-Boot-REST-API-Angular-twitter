package rest.twitter.exception;

public class UserExistException extends RuntimeException{
    public UserExistException(){
        super("User already exists!");
    }
}
