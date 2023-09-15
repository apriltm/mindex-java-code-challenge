package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {

        // creating Compensation for employee John Lennon

        Employee testEmployee = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        Compensation compensation = new Compensation();
        compensation.setEmployee(testEmployee);
        compensation.setSalary(new BigDecimal(245000));
        compensation.setEffectiveDate(LocalDate.now());

        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();

        assertEmployeeEquivalence(createdCompensation.getEmployee(), compensation.getEmployee()); //checking to see if the employee data matches the compensation employee data

        assertEquals(createdCompensation.getEmployee().getEmployeeId(), compensation.getEmployee().getEmployeeId());
        assertEquals(createdCompensation.getSalary(), compensation.getSalary());
        assertEquals(createdCompensation.getEffectiveDate(), compensation.getEffectiveDate());


        //Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();

        assertEmployeeEquivalence(readCompensation.getEmployee(), createdCompensation.getEmployee()); //checking to see if the retrieved compensation data matches the compensation employee data

        assertEquals(readCompensation.getEmployee().getEmployeeId(), createdCompensation.getEmployee().getEmployeeId());
        assertEquals(readCompensation.getSalary(), createdCompensation.getSalary());
        assertEquals(readCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
