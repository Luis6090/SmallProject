package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		DepartmentDao departmentDao = DaoFactory.createDepatmentDao();
		
		System.out.println("\t====Test 1: findById====");
		Department department = departmentDao.findById(2);
		System.out.println(department);
		
		System.out.println("\t====Test 2: findAll====");
		List<Department> departmentList = departmentDao.findAll();
		for(Department list: departmentList) {
			System.out.println(list);
		}
		
		System.out.println("\t====Test 3: insert====");
		Department newDepartment = new Department(null,"Games");
		departmentDao.insert(newDepartment);
		System.out.println("New id: " + newDepartment.getId());
		
		System.out.println("\t====Test 4: update====");
		department = departmentDao.findById(5);
		department.setName("TV");
		departmentDao.update(department);
		System.out.println("Update success!");
		
		System.out.println("\t====Test 5: delete====");
		departmentDao.deleteById(3);
		System.out.println("Delete success!");
		
		
	}

}
