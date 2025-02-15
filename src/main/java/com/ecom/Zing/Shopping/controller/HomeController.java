package com.ecom.Zing.Shopping.controller;

import com.ecom.Zing.Shopping.model.Category;
import com.ecom.Zing.Shopping.model.Product;
import com.ecom.Zing.Shopping.model.User;
import com.ecom.Zing.Shopping.service.CartService;
import com.ecom.Zing.Shopping.service.CategoryService;
import com.ecom.Zing.Shopping.service.ProductService;
import com.ecom.Zing.Shopping.service.UserService;
import com.ecom.Zing.Shopping.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void getUserDetails(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User userDetail = userService.getUserByEmail(email);
            model.addAttribute("user", userDetail);
            Integer countCart = cartService.getCountCart(userDetail.getId());
            model.addAttribute("countCart", countCart);
        }
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/products")
    public String product(Model model, @RequestParam(value = "category", defaultValue = "") String category) {
        System.out.println(category);
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProducts(category);
        System.out.println("Categories: " + categories);

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("paramValue", category);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable int id, Model m) {
        Product productById = productService.getProductById(id);
        m.addAttribute("product", productById);
        return "viewProduct";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        User savedUser = userService.saveUser(user);
        if (!ObjectUtils.isEmpty(savedUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "user" + File.separator + file.getOriginalFilename());
                System.out.println(path);
                savedUser.setProfileImage(path.toString());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("success", " User saved successfully");
        } else {
            session.setAttribute("error", "Something wrong on server");

        }
        return "redirect:/register";
    }

    //    Forgot password code-----------------------------------------------
    @GetMapping("/forgot-password")
    public String showForgotPassword() {
        return "forgot_password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User userByEmail = userService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(email)) {
            session.setAttribute("errorMessage", "Invalid email");
        } else {
            String resetToken = UUID.randomUUID().toString();
            userService.updateUserResetToken(email, resetToken);
            String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;
            Boolean sendMail = commonUtil.sendMail(url, email);
            if (sendMail) {
                session.setAttribute("success", "Please check your mail");
            } else {
                session.setAttribute("errorMessage", "Please check your mail");
            }
        }
        return "redirect:/forgot-password";
    }


    @GetMapping("/reset-password")
    public String showResetPassword(@RequestParam String token, HttpSession session, Model model) {
     User userByToken =    userService.getUserByToken(token);
     if(userByToken == null){
         model.addAttribute("msg", "Your Link is invalid or expired");
         return "message";
     }
        model.addAttribute("token", token);
        return "reset_password";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,@RequestParam String password, HttpSession session, Model model) {
        User userByToken =    userService.getUserByToken(token);
        if(userByToken == null){
            model.addAttribute("msg", "Your Link is invalid or expired");
            return "message";
        }else{
            userByToken.setPassword(bCryptPasswordEncoder.encode(password));
            userByToken.setResetToken(null);
            userService.updateUser(userByToken);
            session.setAttribute("success", "Password changed successfully");
            model.addAttribute("msg","Password changed successfully");
            return "message";
        }
    }
}
