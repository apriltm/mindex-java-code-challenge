package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureIdUrl;

    @Autowired
    EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureIdUrl = "http://localhost:" + port + "reportingStructure/{id}";
    }

    @Test
    public void testRead() {

        //testing for John Lennon by retrieving the data and using it to build a reporting structure

        Employee testEmployee = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, testEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(reportingStructure.getEmployee(), testEmployee); //checking to see if the employee data matches the reporting structure employee data that was extracted
        assertEquals(reportingStructure.getEmployee().getEmployeeId(), testEmployee.getEmployeeId());
        assertEquals(reportingStructure.getNumberOfReports(), 4);


        //testing for Paul McCartney by retrieving the data and using it to build a reporting structure

        testEmployee = employeeService.read("b7839309-3348-463b-a7e3-5de1c168beb3");
        reportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, testEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(reportingStructure.getEmployee(), testEmployee); //checking to see if the employee data matches the reporting structure employee data that was extracted
        assertEquals(reportingStructure.getEmployee().getEmployeeId(), testEmployee.getEmployeeId());
        assertEquals(reportingStructure.getNumberOfReports(), 0); //since the employee had no direct reports, it is expected to return 0


        //testing for Ringo Starr by retrieving the data and using it to build a reporting structure

        testEmployee = employeeService.read("03aa1462-ffa9-4978-901b-7c001562cf6f");
        reportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, testEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(reportingStructure.getEmployee(), testEmployee); //checking to see if the employee data matches the reporting structure employee data that was extracted
        assertEquals(reportingStructure.getEmployee().getEmployeeId(), testEmployee.getEmployeeId());
        assertEquals(reportingStructure.getNumberOfReports(), 2); //the employee has two direct report employees while also being a part of a direct report for another employee
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

}
