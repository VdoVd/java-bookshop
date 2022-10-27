package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.rmi.UnexpectedException;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler({UnexpectedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView processUnauthenticatedException(NativeWebRequest request,UnexpectedException e){
        ModelAndView mv=new ModelAndView();
        mv.addObject("ex",e);
        mv.setViewName("unauthorized");
        return mv;
    }
}
