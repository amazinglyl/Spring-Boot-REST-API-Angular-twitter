package rest.twitter.domian;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class Dto {
    private long id;
    private String token;

    public Dto(long _id,String _token){
        this.id=_id;
        this.token=_token;
    }
}
