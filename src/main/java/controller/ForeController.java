package controller;

import com.github.pagehelper.PageHelper;
import org.apache.taglibs.standard.extra.spath.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pojo.Book;
import pojo.Category;
import pojo.Page;
import pojo.User;
import service.BookService;
import service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class ForeController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CategoryService categoryService;
    @RequestMapping("/home.do")
    public ModelAndView goHome(){
        ModelAndView mav=new ModelAndView("home");
        Map<Integer,String>categories=categoryService.listByMap();
        Map<Category, List<Book>>booksMap=bookService.listByCategory();
        mav.addObject("categories",categories);
        mav.addObject("booksMap",booksMap);
        return mav;
    }

    @RequestMapping("/myBookself.do")
    public ModelAndView goMyBookshelf(HttpServletRequest request){
        ModelAndView mav=new ModelAndView("myBookshelf");
        User user=(User) request.getSession().getAttribute("user");
        List<Book> books=bookService.listByUserId(user.getId(),1);
        List<Book>askBooks=bookService.listByUserId(user.getId(),0);
        mav.addObject("books",books);
        mav.addObject("askBooks",askBooks);
        return mav;
    }

    @RequestMapping("/goUpload.do")
    public ModelAndView upload(Book book){
        String path=book.getBookType()==1?"uploadSell":"uploadAsk";
        ModelAndView mav=new ModelAndView(path);
        Map<Integer,String>categories=categoryService.listByMap();
        mav.addObject("categories",categories);
        return mav;
    }

    @RequestMapping("/goBookStore.do")
    public ModelAndView goBookStore(Page page, Category category){
        ModelAndView mav=new ModelAndView("bookStore");
        Map<Integer,String>categories=categoryService.listByMap();
        Category curCategory=category.getId()!=0?categoryService.get(category.getId()):new Category();
        String categoryName= curCategory.getName()==null?"???????????????":curCategory.getName();
        int total=bookService.count();
        page.calculateEnd(total);
        if(page.getStart()<0){
            page.setStart(0);
        }else if(page.getStart()>total){
            page.setEnd(page.getEnd());
        }
        PageHelper.offsetPage(page.getStart(),16);
        List<Book>books=curCategory.getId()==0?bookService.listByBookType(1):bookService.listByCategoryId(1,curCategory.getId());
        mav.addObject("categoryName",categoryName);
        mav.addObject("books",books);
        mav.addObject("categories",categories);
        return mav;
    }

    @RequestMapping("/goAskBookStore.do")
    public ModelAndView goAskBookStore(Page page){
        ModelAndView mav=new ModelAndView("askBookStore");
        int total=bookService.count();
        page.calculateEnd(total);
        if(page.getStart()<0){
            page.setStart(0);
        }else  if(page.getStart()>total){
            page.setEnd(page.getEnd());
        }
        PageHelper.offsetPage(page.getStart(),16);
        List<Book>books=bookService.listByBookType(0);
        mav.addObject("books",books);
        return mav;
    }


}
