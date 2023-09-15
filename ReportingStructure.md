# Task 1
## This is the documentation for Reporting Structure

### ReportingStructureController.java
The controller is where the rest endpoint will be included in which the instructions specified accepting an employee id
and returning the reporting structure for the given id. Since the business logic was written on a service layer,
the __ReportingStructureService__ class will be injected and __GET__ HTTP method will be used as it corresponds to the read
operation.

### How to use
```
* READ
    * HTTP Method: GET
    * URL: localhost:8080/reportingStructure/{id}
    * RESPONSE: ReportingStructure
```

### ReportingStructure.java
Per instructions, ReportingStructure wants to have the properties __Employee__ class and the __number of reports__ under a given Employee. 
For that reason, both are implemented as such and also include getters and setters:

```
    private Employee employee;
    private int numberOfReports;
```

### ReportingStructureServiceImpl.java
Here the business logic was written for two methods: to build a reporting structure for the given employee id and to determine
the number of reports by calculating the direct reports under the mentioned employee id, including the employees that also
have direct reports.

> __public ReportingStructure read(String id)__

This method is responsible for generating a reporting structure under a given employee id, if the employee id is valid. We
check the validity of the id by calling the __read__ method. In order to do so, the __EmployeeService__ has to be
injected, which is done by the __@Autowired__ annotation. If the employee id is valid, the __read__ will return a type
Employee which we will use to build the reporting structure by creating an instance of __ReportingStructure__, calling the 
method __setEmployee(Employee employee)__, and setting the return type of Employee to ReportingStructure Employee. We will iterate
through the entries of directReports list if there are any to also further construct the reporting structure, including any employee
within the directReports, if they also have any additional directReports. Additionally, we will call the 
__numberOfReports(String id)__ method to get the total amount of directReports under the given employee id and assign it to the 
__numberOfReports__ property.

> __public int numberOfReports(String id)__

This method is responsible for doing the calculations for direct reports. To ensure that the employee is a valid employee,
we search by id using the __read__ method from __EmployeeService__ that would return a type Employee.
If the id is valid, we would be able to access the __directReports__ property in the __Employee__ class. If the directReports
under the given employee id is not null, we will simply iterate through every entry within the list of direct reports. 
Because we want to keep track of the total amount of direct reports per employee if the directReports is not null and when we
recursively call the same method, we will use a hashmap with the key as String and the value as Integer, which represents 
each unique employee id that will have the number of reports if there are any. If there are no directReports (meaning if 
it is null) present under a particular employee, then we will simply assign the value of 0 with the particular employee id as key.

