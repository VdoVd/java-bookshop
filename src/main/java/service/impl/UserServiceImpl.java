package service.impl;

import dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.User;
import service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean checkUser(User user) {
        int flag = userDAO.checkPassword(user);
        return flag==1;
    }

    @Override
    public User get(int id) {
        return userDAO.get(id);
    }

    @Override
    public User getByStudentid(String studnetid) {
        return userDAO.getByStudentid(studnetid);
    }

}
