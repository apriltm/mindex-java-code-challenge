package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    private Map<String, Integer> directReportsCounter = new HashMap<>();

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting structure under employee " + id);

        Employee employee = employeeService.read(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure structureOfEmployees = new ReportingStructure();

        structureOfEmployees.setEmployee(employee);
        structureOfEmployees.setNumberOfReports(numberOfReports(employee.getEmployeeId()));

        List<Employee> directReports = employee.getDirectReports();

        if (directReports != null) {
            for (Employee directEmployee : directReports) { //building the structure of employees within the direct reports of an employee to avoid nulls
                String directEmployeeID = directEmployee.getEmployeeId();
                employee = employeeService.read(directEmployeeID);

                directEmployee.setEmployeeId(employee.getEmployeeId());
                directEmployee.setFirstName(employee.getFirstName());
                directEmployee.setLastName(employee.getLastName());
                directEmployee.setPosition(employee.getPosition());
                directEmployee.setDepartment(employee.getDepartment());

                if (employee.getDirectReports() != null) { //if the employees within the direct reports have direct
                                                            // reports, the structure for these employees will also be built
                    for (Employee subEmployee : employee.getDirectReports()) {
                        String subEmployeeID = subEmployee.getEmployeeId();
                        Employee searchSubEmployee = employeeService.read(subEmployeeID);

                        subEmployee.setEmployeeId(searchSubEmployee.getEmployeeId());
                        subEmployee.setFirstName(searchSubEmployee.getFirstName());
                        subEmployee.setLastName(searchSubEmployee.getLastName());
                        subEmployee.setPosition(searchSubEmployee.getPosition());
                        subEmployee.setDepartment(searchSubEmployee.getDepartment());
                    }

                    directEmployee.setDirectReports(employee.getDirectReports());
                }

            }
        }

        return structureOfEmployees;
    }

    public int numberOfReports(String id) {
        LOG.debug("Generating number of reports under given employee id " + id);

        Employee employee = employeeService.read(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        List<Employee> directReportsList = employee.getDirectReports();


        int totalNumberOfReports = 0; //using this to calculate number of reports for the employee that may have direct
                                        // report employee(s) that may also have direct report employees as well

        if (directReportsList != null) {
            totalNumberOfReports += directReportsList.size();

            for (Employee directReport : directReportsList) {
                String employeeID = directReport.getEmployeeId();
                Employee employeeInDirectReport = employeeService.read(employeeID);

                if (employeeInDirectReport.getDirectReports() != null) {

                    totalNumberOfReports += employeeInDirectReport.getDirectReports().size(); //we are also getting the employees that also have direct reports, so we can get the total amount
                    directReportsCounter.put(employeeID, directReportsCounter.getOrDefault(employeeID, 0) + 1);
                    numberOfReports(employeeID); //recursive call to check if employee within direct reports also has direct reports
                } else {
                    directReportsCounter.put(employeeID, 0); //if employee in direct reports has no direct reports, assign 0
                }

            }
        }

            directReportsCounter.put(id, totalNumberOfReports); //assign the total the employee overall has of the amount of direct reports that are inclusive of the employees also
                                                                //having direct reports


        return directReportsCounter.get(id);
    }
}
