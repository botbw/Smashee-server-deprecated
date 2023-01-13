package server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.pojos.User;

@SpringBootTest
class ServicesApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoded = passwordEncoder.encode("1019");
        System.out.println(encoded);
        System.out.println(passwordEncoder.matches("1019", "$2a$10$zxSvwC2qKYbmq7WsscPyOO0aFezzGWIpx.1YQdm5ohxzcGftIRGda"));
    }

}
