package com.example.thymeleafproductmng.controller;

import com.example.thymeleafproductmng.model.Product;
import com.example.thymeleafproductmng.service.IProductService;
import com.example.thymeleafproductmng.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping
    public String index(Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap model){
        model.addAttribute("product", new Product());
        return "create";
    }

    @PostMapping("/save")
    public String createProduct(Product product) {
        productService.create(product);
        return "redirect:/products";
    }

}
