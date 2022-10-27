package dao;

import pojo.BookImage;

public interface BookImageDAO {
    BookImage getByBookId(int bid);
    void add(BookImage bookImage);
    void update(BookImage bookImage);
    void deleteByBookId(int bid);
}
