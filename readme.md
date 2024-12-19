<p>Upload file</p>
<ul>
<li>Create Resource Bundle in /resources as upload_file</li>
<li>file-upload = D:\\image\\ will be output of file uploaded</li>
<li>In AppConfiguration on top of @ComponentScan: @PropertySource("classpath:upload_file.properties"): say it will output that path</li>
<li>In AppConfiguration add @Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/image/**")
            .addResourceLocations("file:" + upload);
} => register and create a static uri</li>
<li>In AppConfiguration add @Bean(name = "multipartResolver")
public CommonsMultipartResolver getResolver() {
    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    resolver.setMaxUploadSizePerFile(52428800);
    return resolver;
}: the Bean to do the whole process and limit the file size</li>
<li>Create ProductForm in model, change type of image to MultipartFile</li>
<li>In index.html has a place to show output uploaded file: <td><img width="100" height="100" th:src="@{'/image/' + ${product.image}}" alt="" src=""></td></li>
<li>In create.html and controller: <p>
    <a th:href="@{/product/create}">
       Add new product
    </a>
</p>
- Trong class ProductController bổ sung phương thức showCreateForm() như sau:

@GetMapping("/create")
public ModelAndView showCreateForm() {
ModelAndView modelAndView = new ModelAndView("/create");
modelAndView.addObject("productForm", new ProductForm());
return modelAndView;
}</li>
<li>Also form submit in create.html has this enctype="multipath/form-data</li>
<li>Now map it all together: @Value("${file-upload}")
private String fileUpload;
- Thêm phương thức POST saveProduct() trong ProductController:

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
}</li>
</ul>
