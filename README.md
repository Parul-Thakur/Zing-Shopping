# 🛍️ E-Commerce Website ZING SHOPPING (Spring Boot & Thymeleaf)

## 📌 Project Overview
This is a full-featured **E-Commerce Website** built using **Java Spring Boot** and **Thymeleaf**. The platform allows users to browse products, filter categories, manage a shopping cart. An **Admin Panel** is also included for managing products, categories, orders, and users.

## 🚀 Features

### 🧑‍💼 User Features
- **User Registration & Login** 
- **Forgot Password** (Send email to reset password)
- **Browse Products** (Filter by categories)
- **Add to Cart** (Increase/decrease quantity)
- **Checkout & Payment** 

### 🔑 Admin Features
- **Admin Login** (Only admins can access)
- **Manage Products** (Add, edit, remove products)
- **Manage Categories** (Add, remove categories)
- **Track Orders** (View and change order status)
- **Manage Users** (Add/remove users)

## 🛠️ Tech Stack
- **Backend**: Java, Spring Boot, Spring Security
- **Frontend**: Thymeleaf, Bootstrap
- **Database**: MySQL
- **Authentication**: Spring Security, JWT
- **Email Service**: JavaMailSender

## 📦 Installation & Setup
1. **Clone the repository**
   ```sh
   git clone https://github.com/Parul-thakur/Zing-Shopping.git
   ```

2. **Configure Database** (MySQL)
   - Update `application.properties` with your database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

3. **Run the Application**
   ```sh
   mvn spring-boot:run
   ```

4. **Access the Application**
   - **User Portal**: `http://localhost:8080/`
   - **Admin Panel**: `http://localhost:8080/admin`

## 📂 Folder Structure
```
/ecommerce-springboot
 ├── /src/main/java/com/example/ecommerce
 │   ├── /config
 │   ├── /controllers
 │   ├── /models
 │   ├── /services
 │   ├── /repositories
 │   ├── /utill
 ├── /src/main/resources
 │   ├── /templates (Thymeleaf views)
 │   ├── /static (CSS, JS, images)
 ├── pom.xml
 ├── README.md
```

## 🎮 Usage Guide
### User
1. Register or log in.
2. Browse and filter products.
3. Add products to cart and proceed to checkout.
4. Complete payment and track order status.

### Admin
1. Log in as admin (`/admin`).
2. Manage products and categories.
3. View and track orders.
4. Add or remove users.

## 🚀 Deployment
You can deploy this project on:
- **Heroku**
- **AWS EC2**
- **DigitalOcean**

## 🤝 Contributing
1. Fork the repository.
2. Create a new feature branch (`feature-new`).
3. Commit changes and push to the branch.
4. Open a Pull Request.

## 📜 License
This project is licensed under the MIT License.

## 📧 Contact
For questions or support, contact me at **paru.thakur11oct@gmail.com**.

