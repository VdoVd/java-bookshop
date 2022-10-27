package controller;

import common.Result;
import common.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pojo.User;
import service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    private static final Logger log=Logger.getLogger(UserController.class);

    @RequestMapping("")
    public ModelAndView login(){return new ModelAndView("login");}

    @RequestMapping(value = "/sessions",method = RequestMethod.POST)
    @ResponseBody
    public Result checkLogin(@RequestBody User user, HttpServletRequest request){
        boolean flag=userService.checkUser(user);
        log.info("request: user/login , user: "+user.toString());
        if(flag){
            Map data=new HashMap();
            data.put("currentUser",user);

            request.getSession().setAttribute("user",userService.getByStudentid(user.getStudentid()));
            return ResultGenerator.genSuccessResult(data);
        }else {
            return ResultGenerator.genFailResult("学号或密码输入错误!");
        }
    }

    @RequestMapping(value = "/sessions",method = RequestMethod.DELETE)
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return ResultGenerator.genSuccessResult();
    }
}
