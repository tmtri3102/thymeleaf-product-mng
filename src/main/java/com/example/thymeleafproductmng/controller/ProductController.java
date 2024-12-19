package com.example.thymeleafproductmng.controller;

import com.example.thymeleafproductmng.model.Product;
import com.example.thymeleafproductmng.model.ProductForm;
import com.example.thymeleafproductmng.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping
    public String index(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/create")
    public String showCreateForm(ModelMap model) {
        model.addAttribute("product", new Product());
        return "create";
    }

    @Value("${file-upload}")
    private String fileUpload;

    @PostMapping("/save")
    public ModelAndView createProduct(@ModelAttribute ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(),
                productForm.getDescription(), productForm.getManufacturer(), fileName);
        productService.create(product);
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("message", "Created new product successfully !");
        return modelAndView;
    }

    @PostMapping("/submit")
    public String submitProduct(@ModelAttribute("product") Product product, Model model) {
        model.addAttribute("id", product.getId());
        model.addAttribute("name", product.getName());
        model.addAttribute("price", product.getPrice());
        model.addAttribute("description", product.getDescription());
        model.addAttribute("manufacturer", product.getManufacturer());
        return "redirect:/products";
    }

}
