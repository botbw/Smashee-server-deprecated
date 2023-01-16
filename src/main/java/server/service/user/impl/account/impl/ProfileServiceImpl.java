package server.service.user.impl.account.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import server.pojos.User;
import server.service.user.impl.UserDetailsImpl;
import server.service.user.impl.account.ProfileService;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Override
    public Map<String, String> getProfile() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        Map<String, String > ret = new HashMap<>();
        ret.put("MSG", "succeed");
        ret.put("id", user.getUid().toString());
        ret.put("usrname", user.getUsrname());
        ret.put("avatar", user.getAvatar());

        return ret;
    }
}
