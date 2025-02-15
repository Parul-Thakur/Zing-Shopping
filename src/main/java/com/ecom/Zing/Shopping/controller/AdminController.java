package com.ecom.Zing.Shopping.controller;

import com.ecom.Zing.Shopping.model.Category;
import com.ecom.Zing.Shopping.model.Product;
import com.ecom.Zing.Shopping.model.ProductOrder;
import com.ecom.Zing.Shopping.model.User;
import com.ecom.Zing.Shopping.service.*;
import com.ecom.Zing.Shopping.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
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
        return "admin/index";
    }
//
//    @GetMapping("/category")
//    public String category(Model model) {
//        model.addAttribute("categories", categoryService.getAllCategory());
//        return "/admin/category";
//    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        return "/admin/add_product";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(
            @ModelAttribute Category category,
            @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        String imageName = file != null && !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        // Check if the category already exists
        if (categoryService.existCategory(category.getName())) {
            session.setAttribute("errorMessage", "Category name already exists");
            return "redirect:/admin/category"; // Stop further execution
        }

        // Save the category if it's unique
        Category savedCategory = categoryService.saveCategory(category);
        if (ObjectUtils.isEmpty(savedCategory)) {
            session.setAttribute("errorMessage", "Not saved! Internal error occurred.");
        } else {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator + file.getOriginalFilename());
            System.out.println(path);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            session.setAttribute("success", "Category saved successfully");
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        Boolean deleteCategory = categoryService.deleteCategory(id);
        if (deleteCategory) {
            session.setAttribute("success", "Category delete successfully");
            System.out.println("deleted category with ID: " + id);

        } else {
            session.setAttribute("error", "Something wrong on server");

        }
        return "redirect:/admin/category";
    }

    @GetMapping("/editCategory/{id}")
    public String editCategory(@PathVariable int id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));

        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();
        if (!ObjectUtils.isEmpty(category)) {
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }
        Category updatedCategory = categoryService.saveCategory(oldCategory);

        if (!ObjectUtils.isEmpty(updatedCategory)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator + file.getOriginalFilename());
                System.out.println(path);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            }
            session.setAttribute("success", "Category saved success");
        } else {
            session.setAttribute("error", "Something wrong on server");
        }
        return "redirect:/admin/editCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image, HttpSession session) throws IOException {
        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        System.out.println(imageName);
        Product product1 = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(product1)) {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator + image.getOriginalFilename());
            System.out.println(path);
            product1.setImage(path.toString());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            session.setAttribute("success", " Product saved success");
        } else {
            session.setAttribute("error", "Something wrong on server");

        }
        return "redirect:/admin/add-product";
    }

    @GetMapping("/products")
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/view_products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        Boolean deletedProduct = productService.deleteProduct(id);
        if (deletedProduct) {
            session.setAttribute("success", "Product delete successfully");
            System.out.println("deleted product with ID: " + id);

        } else {
            session.setAttribute("error", "Something wrong on server");

        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("error", "Invalid Discount");

        } else {
            Product oldProduct = productService.getProductById(product.getId());
            String imageName = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();
            if (!ObjectUtils.isEmpty(product)) {
                oldProduct.setTitle(product.getTitle());
                oldProduct.setPrice(product.getPrice());
                oldProduct.setStock(product.getStock());
                oldProduct.setDescription(product.getDescription());
                oldProduct.setImage(imageName);
                oldProduct.setIsActive(product.getIsActive());
                oldProduct.setDiscount(product.getDiscount());
                Double d = product.getPrice() * (product.getDiscount() / 100.0);
                Double discountPrice = product.getPrice() - d;
                oldProduct.setDiscountPrice(discountPrice);
            }
            Product updatedProduct = productService.saveProduct(oldProduct);

            if (!ObjectUtils.isEmpty(updatedProduct)) {
                if (!file.isEmpty()) {
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product" + File.separator + file.getOriginalFilename());
                    System.out.println(path);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                }
                session.setAttribute("success", "Product saved success");
            } else {
                session.setAttribute("error", "Something wrong on server");
            }
        }
        return "redirect:/admin/editProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getALlUsers(Model model) {
        List<User> users = userService.getUsers("ROLE_USER");
        model.addAttribute("users", users);
        users.forEach(user -> System.out.println("User details: " + user));
        return "/admin/users";
    }
@GetMapping("/updateStatus")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id, HttpSession session) {
        Boolean f = userService.updateAccountStatus(id, status);
        if(f){
            session.setAttribute("success", "Account Status updated");
        }else{
            session.setAttribute("errorMessage", "ASomething wrong on server");
        }
        return "redirect:/admin/users";
    }
    @GetMapping("/orders")
    public String getAllOrders(Model m) {
        List<ProductOrder> allOrders = orderService.getAllOrders();
        m.addAttribute("orders", allOrders);
        return "/admin/orders";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);


        if (ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("success", "Status Updated");
        } else {
            session.setAttribute("errorMessage", "status not updated");
        }
        return "redirect:/admin/orders";
    }

}