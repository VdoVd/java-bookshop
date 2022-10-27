package service;

import pojo.User;

public interface UserService {
    boolean checkUser(User user);
    User get(int id);
    User getByStudentid(String studentid);
}
