package service;

import pojo.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<Category>list();
    Category get(int id);
    void update(Category category);
    void delete(int id);

    int count();

    Map<Integer,String>listByMap();
}
