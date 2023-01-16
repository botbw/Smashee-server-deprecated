package server.controllers.user.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.user.impl.account.RegisterService;
import server.service.user.impl.account.impl.RegisterServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/user/account")
public class RegisterController {
    private RegisterService registerService;

    public RegisterController(@Autowired RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String usrname = body.get("usrname");
        String pwd = body.get("pwd");
        String confirmedPwd = body.get("confirmedPwd");
        return registerService.register(usrname, pwd, confirmedPwd);
    }
}
