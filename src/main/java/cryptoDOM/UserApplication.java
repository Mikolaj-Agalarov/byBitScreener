package cryptoDOM;

import com.google.gson.Gson;
import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;


@SpringBootApplication
@EnableScheduling
public class UserApplication {
    private static final String TICKER_DATA_API_URL = "https://openapi-v2.kucoin.com/api/v1/market/allTickers";

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return flyway;
    }
    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class, args);

        }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }



}


