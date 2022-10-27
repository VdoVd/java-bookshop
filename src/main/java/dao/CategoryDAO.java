package dao;

import pojo.Category;

import java.util.List;

public interface CategoryDAO {
    void add(Category category);

    void delete(int id);

    Category get(int id);

    void update(Category category);

    // 获取所有Category
    List<Category> list();

    int count();

}
