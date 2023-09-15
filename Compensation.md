# Task 2
## This is the documentation for Compensation

### CompensationController.java

The controller is where the rest endpoints will be included in which the instructions specified creating a new resource
of type Compensation and retrieving the Compensation by employeeId. The business logic was written on a service layer, so
the __CompensationService__ class will be injected via __@Autowired__ annotation. __POST__ and __GET__ HTTP methods will
be used as they correspond to the create and read operation respectively.

### How to use
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/compensation
    * PAYLOAD: Compensation
    * RESPONSE: Compensation
* READ
    * HTTP Method: GET
    * URL: localhost:8080/compensation/{id}
    * RESPONSE: Compensation
```

### Compensation.java
As per instructions, we are to create a Compensation class in which it wants the following properties: __Employee__, __salary__,
and __effectiveDate__. The said properties are implemented as well as the getters and setters:

```
    private Employee employee;
    private BigDecimal salary;
    private LocalDate effectiveDate;
```

__BigDecimal__ is declared for salary so there would be no loss of precision and __LocalDate__ is declared 
for the exact month, day, and year of the effective date of an employee.

### CompensationServiceImpl.java
This is where the business logic was written for the following methods: __create__ and __read__.

> __public Compensation create(Compensation compensation)__

This method is responsible for creating a Compensation type for a given employee.
In order to validate the employee, we need to check if the employee can be found by the associated id by calling the
__read__ method from __EmployeeService__ which has to be injected and was done so via __@Autowired__. __Read__ will 
call on the __findByEmployeeId__ method from the __EmployeeRepository__ which should return an Employee type if the
employee exists. For that reason, we can ensure that we can persist and query the data correctly from the persistence layer.

> __public Compensation read(String id)__

This method is responsible for generating a Compensation type based off of the employee id. Again, we need to check if the
employeeId given is valid with __read__ method from the __EmployeeService__. If it is valid, we also need to ensure that a __Compensation__ 
type exists as well for the same employee from __CompensationRepository__ which has also been injected, by calling
__findByEmployee__ method. If this is also valid, we can then retrieve the data from the persistence layer.

### CompensationRepository.java
As the Compensation needed to be queried and persisted, we will have the __CompensationRepository__ to be responsible for
both as this class will communicate with the database.

> __Compensation findByEmployee(Employee employee)__

This method is used to validate an existing Employee that has a Compensation type.


