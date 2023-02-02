package cryptoDOM.dto;

import lombok.Data;

@Data
public class UserDtoRequest {
    private String username;
    private String email;
    private String password;
}
