package portalcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import portalservice.impl.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    @RequestMapping("/logout")
    public String Logout(HttpServletRequest request){
        userService.logout(request);
        return "index";
    }

}
