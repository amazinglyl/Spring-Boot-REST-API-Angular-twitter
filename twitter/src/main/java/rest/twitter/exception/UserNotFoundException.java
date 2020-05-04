package rest.twitter.exception;

import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException(long id){
        super("There is no User"+id);
    }

}
