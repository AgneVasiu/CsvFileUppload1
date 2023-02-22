package com.CsvFileUppload.CsvFileUppload.Controller;

import com.CsvFileUppload.CsvFileUppload.H2api.EmployeeRepository;
import com.CsvFileUppload.CsvFileUppload.Model.Employee;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
public class CsvUpploding {
    @Autowired
    private EmployeeRepository repository;
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @PostMapping("/csv-file-uploading")
    public String csvFileUploading(@RequestParam("file") MultipartFile file, Model model){
        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
            // parse CSV file to create a list of `Employee` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Employee.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of employees
                List<Employee> employees = csvToBean.parse();

                // save employees list on model
                model.addAttribute("employees", employees);
                model.addAttribute("status", true);
                //save to h2 data base
                for (Employee i : employees) {
                    repository.save(new Employee());
                }
                return "Employees list saved";



            } catch (Exception exc) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        return "fileUpload";

    }
    @GetMapping("/getAllEmployees")
    public List<Employee> getAll(){
        return repository.findAll();}

}
