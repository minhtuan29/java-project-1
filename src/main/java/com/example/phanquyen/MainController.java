package com.example.phanquyen;

import com.example.phanquyen.converter.EmployeeConverter;
import com.example.phanquyen.model.EmployeeInputModel;
import com.example.phanquyen.model.EmployeeSearchInputModel;
import com.example.phanquyen.repository.DatabaseLoad;
import com.example.phanquyen.repository.entity.Employee;
import com.example.phanquyen.repository.jparepo.CustomizedEmployeeRepo;
import com.example.phanquyen.repository.jparepo.EmployeeRepo;
import com.example.phanquyen.service.EmployeeService;
import com.example.phanquyen.service.SelectPageMenu;
import com.example.phanquyen.utils.FileUltis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;




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



    //===== GET METHOD == BUILD VIEW CLIENT =======

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @GetMapping("admin")
    public String admin(){
        return "adminmenu";
    }

    @GetMapping("admin/add-employee")
    public String addNewEmployee(){
        return "addemployee";
    }






    // ============= POST METHOD - GET REQUEST INPUT FORM ==============
    @PostMapping("/adminlogin")
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        if("admin".equals(userName) && "123".equals(password)){
            request.getSession();
            response.sendRedirect("admin");
            return "adminmenu";
        }else{
            return "login";
        }
    }




    @PostMapping("/admin/add-employee")
    public ModelAndView addNewEmployee(EmployeeInputModel employeeInputModel){
        Employee employee = employeeConverter.convertToEntity(employeeInputModel);
        int errCase = 0;
        // các case xảy ra :
        // - Lưu thành công thông tin employee vào DB và lưu ảnh thành công  : báo thành công : errMsg không có gì : case0
        // - lưu thành công thông tin employee vào DB nhưng lưu ảnh bất thành : báo đã lưu, nhưng lưu ảnh bị lỗi : errMsg nhận lưu ảnh thất bại : case1
        // - lưu thất bại thông tin employee, do đó cũng không lưu ảnh : báo thất bại : errMsg nhận lưu DB thất bại: case2
        try{
            employeeRepo.save(employee);
            // nếu lưu thông tin thành công vào DB thì mới tiếp tục lưu file ảnh
            // tiến hành lưu ảnh
            try{
                if ( employee.getImgFileName() != null ){
                    FileUltis.saveFile(employeeInputModel.getImgFile(), "photos", employee.getImgFileName());
                }
            }catch (Exception e){
                e.printStackTrace(); // lưu ảnh vào disk thất bại, gặp trục trặc disk, hết bộ nhớ hoặc gì đó
                errCase = 1;
            }
        }catch (Exception e){
            System.err.println("\n Lưu thông tin vào DB thất bại, DB hoặc server gặp trục trặc");
            e.printStackTrace();
            errCase = 2;
        }


        if(employeeInputModel.getName().equals("demo error case1")){
            errCase = 1;
        }
        if(employeeInputModel.getName().equals("demo error case2")){
            errCase = 2;
        }

        // user : muốn biết khi có lỗi thì lỗi vì sao ?  nên fix thế nào ?
        // trong các case này thì do hệ thống bị lỗi, user không cần nên biết về hệ thống, ta báo lỗi chung chung
        // tách biệt FE, BE nên quăng cho FE là successfully hoặc failed
        ModelAndView modelAndView = new ModelAndView("addemployee");
        if(errCase == 0){
            modelAndView.addObject("msg", "successfully");
        }else if (errCase == 1){
            modelAndView.addObject("msg", "Đã lưu nhân viên, nhưng quá trình lưu ảnh thất bại do hệ thống gặp sự cố. Hãy cập nhật ảnh khi hệ thống khôi phục");
        }else{
            modelAndView.addObject("msg", "failed");
        }
        return modelAndView;
    }





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





    @GetMapping("/admin/delete-employee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id, HttpServletResponse response, RedirectAttributes redirectAttrs){
        if(employeeRepo.existsById(id)){
            try{
                employeeRepo.deleteById(id);
                redirectAttrs.addFlashAttribute("msg", "successfully"); // khi redir thì không dùng được Model để truyền dữ liệu, phải dùng cái này
            }catch (Exception e){
                redirectAttrs.addFlashAttribute("msg", "failed");
            }
        }
        return "redirect:/admin/list-employee"; // giống sendRedir trong servlet respone z, hơn 1 cái là đỡ phải try catch
    }




    @GetMapping("/admin/edit-employee/{id}")
    public String editEmployee(@PathVariable("id") Long id, RedirectAttributes redirectAttrs, Model model){
        if(employeeRepo.existsById(id)){
            model.addAttribute("employee", employeeConverter.convertToEmployeeOuputModel(employeeRepo.getReferenceById(id)));
            return "editemployee";
        }else{
            redirectAttrs.addFlashAttribute("deletedEmpErrMsg", "Xin lỗi, có vẻ nhân viên đó đã quản trí viên khóa xóa rồi");
            return "redirect:/admin/list-employee";
        }
    }





    @PostMapping("/admin/edit-employee/{id}")
    public String editNewEmployee(EmployeeInputModel employeeInputModel, @PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        int errCase = 0;
        boolean toUseNewImg = false;

        if( employeeRepo.existsById(id)) { // nếu còn tồn tại trong DB mới thực hiện
            Employee employeeToSave = employeeConverter.convertToEntity(employeeInputModel);
            employeeToSave.setId(id);
            if(employeeToSave.getImgFileName() == null) { // nếu dùng lại ảnh cũ
                Employee employeeDatabase = employeeRepo.getReferenceById(id);
                employeeToSave.setImgFileName(employeeDatabase.getImgFileName()); // thì liên kết với ảnh cũ
            }else{
                toUseNewImg = true; // ngược lại thì dùng ảnh mới
            }

            try {
                employeeRepo.save(employeeToSave);
                // nếu lưu thành công bên DB, tiếp tục tiến hành lưu ảnh
                if( toUseNewImg){ // Lưu ảnh : nếu dùng ảnh mới thì mới lưu ảnh
                    try {
                        FileUltis.saveFile(employeeInputModel.getImgFile(), "photos", employeeToSave.getImgFileName());
                    }catch (Exception e){
                        errCase = 1; // lưu thành công nhưng lưu ảnh thất bại
                    }
                }
            }catch (Exception e){
                errCase = 2; // hệ thống gặp sự cố, vui lòng tới dịp sau
            }
        }else { // không tồn tại trong DB, không thực hiện gì hết
            errCase = 3;
        }

        ModelAndView modelAndView = new ModelAndView("addemployee");
        switch (errCase){
            case 1:
                redirectAttributes.addFlashAttribute("msgEdit", "Đã lưu nhân viên, nhưng quá trình lưu ảnh thất bại do hệ thống gặp sự cố. Hãy cập nhật ảnh khi hệ thống khôi phục");
                break;
            case 2:
                redirectAttributes.addFlashAttribute("msgEdit", "failed");
                break;
            case 3:
                redirectAttributes.addFlashAttribute("msgEdit", "Không tồn tại nhân viên đó, có lẽ quản trị viên khác đã xóa");
                break;
            default:
                redirectAttributes.addFlashAttribute("msgEdit", "successfully");
        }

        return "redirect:/admin/list-employee";
    }


    @GetMapping("search")
    public String demo(){
        EmployeeSearchInputModel employeeSearchInputModel = new EmployeeSearchInputModel();
        employeeSearchInputModel.setMinAge(12);
        employeeSearchInputModel.setMaxAge(30);
        String[] skills = new String[2];
        skills[0] = "english";
        skills[1] = "present";
        Integer[] outputNumPages = new Integer[1];
        employeeSearchInputModel.setSkills(skills);
        var employeeEntities = customizedEmployeeRepo.findByCondition(employeeSearchInputModel, 10, 1, outputNumPages);
        for(var e : employeeEntities){
            System.out.println(e.getName());
        }
        return "index";
    }




    @GetMapping("/admin/list-employee/search")
    public ModelAndView showEmployeePageSearch(
            @RequestParam("page") Optional<Integer> userPageInput
            , EmployeeSearchInputModel employeeSearchInputModel
    ){
        int databasePage = userPageInput.map(x -> x - 1).orElse(FIRST_TIME_ACCESS_NULL_TO_FIRST_IDX);

        Integer[] outputNumPages = new Integer[1];
        var outputEmployeeEntites = customizedEmployeeRepo.findByCondition(employeeSearchInputModel, ROW_PAGE_COUNT, databasePage, outputNumPages);

        int pageNumberRsp = userPageInput.orElse(1); // user begin 1 default, we begin 0
        var pagerRsp = new SelectPageMenu(outputNumPages[0], pageNumberRsp);
        var employeeOutputModels = outputEmployeeEntites.stream().map(employeeConverter::convertToEmployeeOuputModel).toList();

        var modelAndView = new ModelAndView("employeelist");
        modelAndView.addObject("employees", employeeOutputModels); // records
        modelAndView.addObject("pager", pagerRsp); // first and last page to choose in menu of user view
        modelAndView.addObject("pageNum", pageNumberRsp); // cur page user choose
        modelAndView.addObject("maxPageNum", outputNumPages[0]); // last page to access
        return modelAndView;
    }


}


