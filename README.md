# The first my personal Java web app (self learn, selft do)
  
  
### Repository
```java

@Repository
public class CustomizedEmployeeRepoImpl implements CustomizedEmployeeRepo {

    @PersistenceContext
    EntityManager entityManager;


    private static String get(String s){
        return "'"+s+"'";
    }

    private static String likeLeftRight(String s){
        return "'%"+s+"%'";
    }


    private static String getQuery(EmployeeSearchInputModel employeeSearchInputModel){
        StringBuilder queryBuilder = new StringBuilder( """
                select e from Employee e
                where 1 = 1
                """);

        // I dont choose Generalization the func below, otherwise it will be more complex for my project, not needed

        if(employeeSearchInputModel.getName() != null){
            // suppose that we have checked SQLinjection hack attack at Interceptor, now we just add this without valid
            queryBuilder.append("\n and e.name = " + get(employeeSearchInputModel.getName()));
        }

        if(employeeSearchInputModel.getMinAge() != null){
            queryBuilder.append("\n and e.age >= " + employeeSearchInputModel.getMinAge());
        }

        if(employeeSearchInputModel.getMaxAge() != null){
            queryBuilder.append("\n and e.age <= " + employeeSearchInputModel.getMaxAge());
        }

        if(employeeSearchInputModel.getPhone() != null){
            queryBuilder.append("\n and e.phone = " + get(employeeSearchInputModel.getPhone()));
        }

        if(employeeSearchInputModel.getDepartmentID() != null){
            queryBuilder.append("\n and e.departmentID = " + employeeSearchInputModel.getDepartmentID());
        }

        if(employeeSearchInputModel.getRoleID() != null){
            queryBuilder.append("\n and e.roleID = " + employeeSearchInputModel.getRoleID());
        }

        if(employeeSearchInputModel.getHometownID() != null){
            queryBuilder.append("\n and e.hometownID = " + employeeSearchInputModel.getHometownID());
        }

        if(employeeSearchInputModel.getSkills() != null){
            var skills = employeeSearchInputModel.getSkills();
            int SZ = skills.length;

            if(SZ == 1){
                queryBuilder.append("\n and e.skills LIKE " + likeLeftRight(skills[0]));
            }else {
                queryBuilder.append("\n and (");
                List<String> subQueryCondionList = new ArrayList<>();
                Arrays.stream(skills).forEach( e -> subQueryCondionList.add(" e.skills LIKE " + likeLeftRight(e)));
                queryBuilder.append(String.join(" or ", subQueryCondionList));
                queryBuilder.append(")");
            }
        }

        return queryBuilder.toString();
    }



    @Override
    public List<Employee> findByCondition(EmployeeSearchInputModel employeeSearchInputModel, Integer nRecordPerPage, Integer pageAt, Integer[] outputNumPages) {
        /*
            user : page  at 1 => database : page at 0
            user page 1: firstDBAt(0=0*10) nRecord(10)
            user page 2: firstDBAt(10=1*10) nRecord(10)
         */
        var queryAll = entityManager.createQuery(getQuery(employeeSearchInputModel), Employee.class);

        var queryPage = entityManager.createQuery(getQuery(employeeSearchInputModel), Employee.class);
        queryPage.setFirstResult(nRecordPerPage*pageAt);
        queryPage.setMaxResults(nRecordPerPage);
        outputNumPages[0] = (int) (Math.ceil( queryAll.getResultList().size() / nRecordPerPage));
        return queryPage.getResultList();
    }
}
```
  
  
### Converter
```java
    public Employee convertToEntity(EmployeeInputModel employeeInputModel){
        Employee employee = new Employee();

        employee.setName(employeeInputModel.getName());
        employee.setAge(employeeInputModel.getAge());
        employee.setPhone(employeeInputModel.getPhone());

        employee.setHometown(new Hometown());
        employee.getHometown().setId(employeeInputModel.getHometownID());

        employee.setDepartment(new Department());
        employee.getDepartment().setId(employeeInputModel.getDepartmentID());

        employee.setRole(new Role());
        employee.getRole().setId(employeeInputModel.getRoleID());

        if(employeeInputModel.getSkills() != null){
            employee.setSkills(   String.join(",", employeeInputModel.getSkills())  );
        }
        if(employeeInputModel.getImgFile().isEmpty()){
            employee.setImgFileName(null);
        }else{
            employee.setImgFileName(DateTimeUtils.getDateStrInFormat("yyMMddHHmmss-")+employeeInputModel.getImgFile().getOriginalFilename());
        }
        return employee;
    }
```  

### SELECT PAGE Service
```java
public class SelectPageMenu {
    private static int PAGES_NUM = 5;
    private static int BEFORE_AFTER = PAGES_NUM/2;
    private int startPage;
    private int endPage;

    public SelectPageMenu(int totalPages, int currentPage) {

        this.startPage = currentPage - BEFORE_AFTER;
        this.endPage = currentPage + BEFORE_AFTER;

        // if curpage = [1,2]
        if (currentPage < BEFORE_AFTER + 1) { // if over fist
            this.startPage = 1;
            this.endPage = PAGES_NUM;
        }
        if (currentPage + BEFORE_AFTER > totalPages) { // if over last
            this.endPage = totalPages;
            this.startPage = totalPages - PAGES_NUM+ 1;
        }

    }


    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
 ```
 
 ### Controller
 ```java
 @Controller
public class MainController {

    @Autowired
    ServletContext servletContext;

    @Autowired
    EmployeeConverter employeeConverter;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    DatabaseLoad databaseLoad;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomizedEmployeeRepo customizedEmployeeRepo;


    @Value("${spring.servlet.multipart.max-file-size}") String fileMaxSizeMBxx;
    @Value("${row.page.count}") Integer ROW_PAGE_COUNT;

    @PostConstruct
    private void loadDataToContext(){
        servletContext.setAttribute("filemaxsizeMB", Integer.valueOf(fileMaxSizeMBxx.substring(0,1)));
        servletContext.setAttribute("hometowns", databaseLoad.hometownList);
        servletContext.setAttribute("hometownMapStrInt", databaseLoad.hometownMapStrInt);
        servletContext.setAttribute("roles", databaseLoad.roleList);
        servletContext.setAttribute("departments", databaseLoad.departmentList);
    }


    .   .   .
    
    private static final int FIRST_TIME_ACCESS_NULL_TO_FIRST_IDX = 0;

    @GetMapping("/admin/list-employee")
    public ModelAndView showEmployeePage(
            @RequestParam("page") Optional<Integer> userPageInput
    ) {

        int databasePageNum = userPageInput.map(x -> x - 1)
                .orElse(FIRST_TIME_ACCESS_NULL_TO_FIRST_IDX);

        var employeeEntitiesRsp = employeeService.findAllPageable(PageRequest.of(databasePageNum, ROW_PAGE_COUNT));
        int pageNumberRsp = userPageInput.orElse(1); // user begin 1 default, we begin 0
        var pagerRsp = new SelectPageMenu(employeeEntitiesRsp.getTotalPages(), pageNumberRsp);

        var employeeOutputModels = employeeEntitiesRsp.stream().map(employeeConverter::convertToEmployeeOuputModel).toList();

        var modelAndView = new ModelAndView("employeelist");
        modelAndView.addObject("employees", employeeOutputModels); // record
        modelAndView.addObject("pager", pagerRsp); // first menu and last menu to choose page in menu
        modelAndView.addObject("pageNum", pageNumberRsp); // cur page user choose in view
        modelAndView.addObject("maxPageNum", employeeEntitiesRsp.getTotalPages()); //  last page to access
        return modelAndView;
    }

    . .  .
    
    other . . .
   

 ```
 
## Frontend 
- Java Thymeleaf server side render
- Javascript(data convert and input valid check) 
- HTML CSS  
 
 
 
About : [Frontend Resources](https://github.com/minhtuan29/java-project-1/tree/master/src/main/resources/templates)  
  
