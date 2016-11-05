package UserLoginSystem.controller;

import UserLoginSystem.util.URLs;
import UserLoginSystem.util.Views;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * File name  : hw5.LoginController.java
 * Programmer : EOF (Jason Leaster)
 * Date       : 2016/10/24
 * Email      : jasonleaster@gmail.com
 * Descirption:
 */

@Controller
public class LoginController {

    @RequestMapping(value = {URLs.LOGIN, URLs.ROOT}, method = RequestMethod.GET)
    public String loginGet(){
        return Views.LOGIN;
    }

//    @RequestMapping(value = {URLs.LOGIN, URLs.ROOT}, method = RequestMethod.POST)
//    public String loginPost(LoginForm form, Model model,
//                            HttpServletRequest request, HttpServletResponse response){
//
//        User user = form.toUser();
//        User userInDB = userService.getById(user.getEmail());
//
//        if (userInDB == null){
//            model.addAttribute(AttributesKey.MODEL_ATTRIBUTES_ERR_MSG, "The user does not exist.");
//            return Views.REGISTER;
//        }else if (! userInDB.getPassword().equals(user.getPassword())){
//            model.addAttribute(AttributesKey.MODEL_ATTRIBUTES_ERR_MSG, "The password is not correct! Please try again!");
//            return Views.LOGIN;
//        }
//
//        if(form.isRememberMe()){
//            WebCookie.addCookie(response, WebCookie.COOKIE_NAME, userInDB.getEmail(), WebCookie.MAX_AGE);
//        }
//
//        HttpSession session = request.getSession();
//        session.setAttribute(AttributesKey.SESSION_ATTRIBUTES_USER, userInDB);
//
//        if(userInDB.isAdministrator()){
//            session.setAttribute(AttributesKey.SESSION_ATTRIBUTES_ADMIN, userInDB);
//        }else{
//            session.setAttribute(AttributesKey.SESSION_ATTRIBUTES_ADMIN, null);
//        }
//
//        List<Book> books = bookService.getPopularBook(BOOK_SHOW_IN_HOMEPAGE);
//
//        model.addAttribute(AttributesKey.MODEL_ATTRIBUTES_BOOKS, books);
//
//        return Views.HOME;
//    }
//
//
//    @RequestMapping(value = URLs.LOGOUT)

}
