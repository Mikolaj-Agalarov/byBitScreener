package cryptoDOM;


import cryptoDOM.model.User;
import cryptoDOM.repository.UserRepository;
import cryptoDOM.service.UserService;
import cryptoDOM.service.kuCoinServices.KuCoinService;

public class main {

    public static void main(String[] args) throws Exception {
        KuCoinService kuCoinService = new KuCoinService();
        kuCoinService.getAllSpotTickersFromApiAndWriteThemToTxt();
    }

}
