# Upload file

1.  Create Resource Bundle in `/resources` as `upload_file`.
2.  `file-upload = D:\\image\\` will be output of file uploaded.
3.  In `AppConfiguration` on top of `@ComponentScan`: `@PropertySource("classpath:upload_file.properties")`: say it will output that path.
4.  In `AppConfiguration` add:

    ```java
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:" + upload);
    }
    ```

    Register and create a static URI.

5.  In `AppConfiguration` add:

    ```java
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(52428800);
        return resolver;
    }
    ```

    The Bean to do the whole process and limit the file size.

6.  Create `ProductForm` in model, change type of image to `MultipartFile`.
7.  In `index.html` has a place to show output uploaded file:

    ```html
    <td><img width="100" height="100" th:src="@{'/image/' + ${product.image}}" alt="" src=""></td>
    ```

8.  In `create.html` and controller:

    ```html
    <p>
        <a th:href="@{/product/create}">
           Add new product
        </a>
    </p>
    ```

    Trong class `ProductController` bổ sung phương thức `showCreateForm()` như sau:

    ```java
    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("productForm", new ProductForm());
        return modelAndView;
    }
    ```

9.  Also form submit in `create.html` has this `enctype="multipath/form-data"`.
10. Now map it all together:

    ```java
    @Value("${file-upload}")
    private String fileUpload;
    ```

    Thêm phương thức POST `saveProduct()` trong `ProductController`:

    ```java
    @PostMapping("/save")
    public ModelAndView saveProduct(@ModelAttribute ProductForm productForm) {
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Product product = new Product(productForm.getId(), productForm.getName(),
                productForm.getDescription(), fileName);
        productService.save(product);
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("message", "Created new product successfully !");
        return modelAndView;
    }
    ```
