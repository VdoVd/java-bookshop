package service;

import pojo.Book;
import pojo.Category;

import java.util.List;
import java.util.Map;

public interface BookService {
    Book get(int id);

    int getUserId(int id);

    void add(Book book);

    int count();

    void delete(int id);

    List<Book> list();

    List<Book>listByUserId(int uid,int bookType);

    List<Book>listByCategoryId(int bookType,int cid);

    List<Book>listByBookType(int bookType);

    Map<Category,List<Book>> listByCategory();

    void update(Book book);
}
