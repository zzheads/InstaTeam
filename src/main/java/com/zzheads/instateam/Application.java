package com.zzheads.instateam;//

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


//    DONE: Download and extract the project’s ZIP file. Included in the archive are the following:
//    DONE: HTML files to serve as examples for all required views - open index.html to browse. These files can be used as a starting point for writing Thymeleaf templates
//    DONE: a static directory containing all static assets (e.g. CSS) to be used in your project
//
//    To complete this project, follow the instructions below. If you get stuck, ask a question in the community.
//
//    DONE: In the IDE of your choice, create a Gradle project. Add dependencies for Spring Boot with Thymeleaf, Spring ORM, Hibernate, Apache DBCP, and H2. Create the directory and package structure of the application. Save all static assets into the proper project directory.
//    DONE: Create the configuration files for Hibernate and an H2 database connection.
//    DONE: Create a Java class for starting the application as a SpringApplication and a Spring configuration class with two @Bean methods:
//    DONE: Method for initializing a LocalSessionFactoryBean
//    DONE: Method for initializing a DataSource
//    DONE: Add JPA annotations to all model classes.
//    DONE: Create a DAO interface and implementation for each model class.
//    DONE: Create a service interface and implementation for each model class.
//    DONE: Create the RoleController and Thymeleaf views necessary for viewing, adding, and editing roles.
//    DONE: Create the CollaboratorController and Thymeleaf views necessary for viewing, adding, and editing collaborators.
//    DONE: Create the ProjectController and Thymeleaf views necessary for viewing, adding, and editing projects, without including the ability to assign each role to a specific collaborator.
//    DONE: Add the methods to ProjectController, and the Thymeleaf views necessary for assigning and unassigning collaborators to and from a project’s needed roles.
//
//    DONE: Extract the common code of each DAO implementation to an abstract class that the DAO implementations extend.
//    DONE: Add the ability to delete projects, roles, and contractors and ensure data integrity for all relationships. For example, when a collaborator is deleted, make sure that all roles previously assigned to this collaborator become unassigned.
//    DONE: Include a start date on projects, and sort chronologically by start date on the project index view.
