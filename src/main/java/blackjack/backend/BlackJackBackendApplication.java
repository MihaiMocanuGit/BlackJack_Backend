package blackjack.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BlackJackBackendApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(BlackJackBackendApplication.class, args);
    }

}
