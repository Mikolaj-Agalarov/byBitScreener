package cryptoDOM;


import cryptoDOM.dto.kuCoinDtos.DOMDto.GlassInstance;
import cryptoDOM.model.User;
import cryptoDOM.service.UserService;
import cryptoDOM.service.byBitServices.ByBitService;
import cryptoDOM.service.kuCoinServices.KuCoinService;
import repository.UserRepository;

import java.util.List;

public class main {
    private static repository.UserRepository UserRepository;

    public static void main(String[] args) throws Exception {
        UserService userService = new UserService(UserRepository);
        User newUser = new User();
        newUser.setRole("user");
        newUser.setPassword("user1");
        newUser.setUsername("kolia");
        userService.saveUser(newUser);
    }

}
