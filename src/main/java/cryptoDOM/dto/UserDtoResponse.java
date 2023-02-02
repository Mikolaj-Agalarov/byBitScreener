package cryptoDOM.dto;

import lombok.Data;


@Data
public class UserDtoResponse {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String role;
}
