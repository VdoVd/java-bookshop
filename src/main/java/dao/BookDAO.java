package dao;

import org.apache.ibatis.annotations.Param;
import pojo.Book;

import java.util.List;

public interface BookDAO {

    List<Book> getListByCategoryId(@Param("start") int start, @Param("count") int count, @Param("bookType") int bookType, @Param("cid") int cid);

    /**
     * 根据图片Id获取上传者Id
     * @param id 图书Id
     * @return 上传者Id
     */
    int getUserId(int id);

    /**
     * 根据图书Id获取具体Book对象
     * @param id 图书Id
     * @return 对应的Book对象
     */
    Book get(int id);

    // 添加
    void add(Book book);

    // 更新
    void update(Book book);

    // 计算书的总数
    int count();

    // 删除
    void delete(int id);

    // 获取所有图书并放入List
    List<Book> list();

    // 根据书的类型（图书信息1/求书信息0）获取所有图书
    List<Book> listByBookType(int bookType);

    /**
     * 根据条件获取所有图书
     * @param uid 用户Id
     * @param bookType 书的类型（图书信息1/求书信息0）
     * @return 相应的List
     */
    List<Book> getListByUserId(@Param("uid") int uid, @Param("bookType") int bookType);
}
