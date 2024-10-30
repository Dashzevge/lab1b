package edu.miu.cse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Main {

    private static final ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException{
        List<Employee> employees = loadEmployeeData();

        printAllEmployeeInJson(employees);
        printUpcomingEnrolleesInJson(employees);
    }

    private static void printAllEmployeeInJson(List<Employee> employees)  throws JsonProcessingException{
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getYearlySalary, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonOutput = mapper.writeValueAsString(sortedEmployees);
        System.out.println("All Employees in JSON format:");
        System.out.println(jsonOutput);
    }
    private static void printUpcomingEnrolleesInJson(List<Employee> employees) throws JsonProcessingException {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfNextMonth = today.plusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfNextMonth = firstDayOfNextMonth.withDayOfMonth(firstDayOfNextMonth.lengthOfMonth());

        List<Employee> upcomingEnrollees = employees.stream()
                .filter(employee -> !employee.hasPensionPlan())
                .filter(employee -> employee.isEligibleForPension(firstDayOfNextMonth, lastDayOfNextMonth))
                .sorted(Comparator.comparing(Employee::getEmploymentDate))
                .collect(Collectors.toList());

        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonOutput = mapper.writeValueAsString(upcomingEnrollees);
        System.out.println("Monthly Upcoming Enrollees in JSON format:");
        System.out.println(jsonOutput);
    }

    private static List<Employee> loadEmployeeData() {
        List<Employee> employees = new ArrayList<>();

        Employee emp1 = new Employee(1, "Daniel", "Agar", LocalDate.of(2018, 1, 17), 105945.50);
        emp1.setPensionPlan(new PensionPlan("EX1089", LocalDate.of(2023, 1, 17), 100.00));

        Employee emp2 = new Employee(2, "Benard", "Shaw", LocalDate.of(2019, 4, 3),197750.00);

        Employee emp3 = new Employee(3, "Carly", "Agar", LocalDate.of(2014, 5, 16), 842000.75);
        emp3.setPensionPlan(new PensionPlan("SM2307", LocalDate.of(2019, 11, 4), 1555.50));

        Employee emp4 = new Employee(4, "Wesley", "Schneider", LocalDate.of(2019, 9, 2), 74500.00);

        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);
        employees.add(emp4);

        return employees;
    }
}