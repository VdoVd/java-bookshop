package dao;

import org.apache.ibatis.annotations.Param;
import pojo.Book;
import pojo.User;

import java.util.List;

public interface UserDAO {
    User get(int id);

    void update(User user);

    int checkPassword(User user);

    User getByStudentid(String studentid);

}
