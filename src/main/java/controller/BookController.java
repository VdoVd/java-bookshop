package controller;
import com.alibaba.fastjson.JSONObject;
import common.Result;
import common.ResultGenerator;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pojo.Book;
import pojo.BookImage;
import pojo.Category;
import pojo.User;
import service.BookImageService;
import service.BookService;
import service.CategoryService;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private BookImageService bookImageService;
    @Autowired
    private CategoryService categoryService;

    private static final Logger log=Logger.getLogger(BookController.class);

    @GetMapping(value = "/{id}")
    public ModelAndView getBookDetail(@PathVariable("id")String id){
        ModelAndView mav=new ModelAndView("bookDetail");
        int intId=Integer.parseInt(id);
        Book curBook=bookService.get(intId);
        curBook.setBookImage(bookImageService.getByBookId(intId));
        curBook.setUser(userService.get(bookService.getUserId(intId)));
        mav.addObject("book",curBook);
        return  mav;
    }

    @PostMapping(value = "")
    public Result uploadSell(HttpServletRequest request, Book book, @RequestParam(value = "image",required = false)MultipartFile file){
        User user=(User) request.getSession().getAttribute("user");
        try {
            if(file!=null&&book!=null){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long time=System.currentTimeMillis();
                String timeStr=sdf.format(time);
                book.setDate(timeStr);
                book.setUser(user);
                bookService.add(book);
                BookImage bookImage=new BookImage();
                bookImage.setBook(book);
                bookImageService.add(bookImage);
                String imageName=bookImage.getId()+".jpg";
                String imagePath=request.getServletContext().getRealPath("/img/book-list/article/");
                File filePath=new File(imagePath,imageName);
                if(!filePath.getParentFile().exists()){
                    filePath.getParentFile().mkdir();
                }
                file.transferTo(new File(imagePath+File.separator+imageName));
                log.info("request: book/upload , book: "+book.toString());
                return ResultGenerator.genSuccessResult();
            }else {
                return ResultGenerator.genFailResult("信息填写不完整或未上传图片!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("上传失败");
        }
    }

    @GetMapping(value = "/renewal/{id}")
    public ModelAndView goEditBook(@PathVariable String id){
        ModelAndView mav=new ModelAndView("editBook");
        int bookId=Integer.parseInt(id);
        Book curBook=bookService.get(bookId);
        log.info("request: book/update , book: "+curBook.toString());
        if(curBook!=null){
            curBook.setBookImage(bookImageService.getByBookId(bookId));
        }
        mav.addObject("book",curBook);
        Map<Integer,String>categories=categoryService.listByMap();
        mav.addObject("categories",categories);
        return mav;
    }
    @PostMapping(value = "/renewal")
    public Result editBook(HttpServletRequest request, Book book,
                           @RequestParam(value = "image" , required = false) MultipartFile file){
        try {
            bookService.update(book);
            if (file != null) {
                BookImage bookImage = bookImageService.getByBookId(book.getId());
                bookImage.setBook(book);
                bookImageService.update(bookImage);
                String imageName = bookImage.getId() + ".jpg";
                String imagePath = request.getServletContext().getRealPath("/img/book-list/article/");
                File filePath = new File(imagePath, imageName);
                if (!filePath.getParentFile().exists()) {
                    filePath.getParentFile().mkdir();
                }else if (filePath.exists()){
                    filePath.delete();
                }
                file.transferTo(new File(imagePath + File.separator + imageName));
            }
            log.info("request: book/update , book: " + book.toString());
            return ResultGenerator.genSuccessResult();
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("修改失败！");
        }
    }

    /**
     * 删除一本或多本图书
     * @param request 用于获取路径，删除图片
     * @param bookIds 要删除的图书ID数组
     */
    @RequestMapping(value = "",method = RequestMethod.DELETE)
    public Result deleteBook(HttpServletRequest request, @RequestParam(value = "bookIds", required = false) String[] bookIds){

        if (bookIds != null) {
            // 遍历每个ID
            for (String bookId : bookIds) {
                int id = Integer.parseInt(bookId);

                // 获取当前图书的图片名称与存放路径
                String imageName = bookImageService.getByBookId(id).getId() + ".jpg";
                String imagePath = request.getServletContext().getRealPath("/img/book-list/article/");
                File filePath = new File(imagePath, imageName);

                // 删除图片
                if (filePath.exists()){
                    filePath.delete();
                }

                // 删除数据库中的图书
                bookImageService.deleteByBookId(id);
                bookService.delete(id);
            }
            log.info("request: book/delete , bookIds: " + Arrays.toString(bookIds));
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败！未选中图书");
        }
    }
}
