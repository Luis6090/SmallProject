package application;

import java.time.LocalDate;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		
		Department dp = new Department(1,"Books");
		Seller seller = new Seller(1,"Pedro","pedro@gmail.com", LocalDate.now(), 3000.0, dp);
		System.out.println(seller);

	}

}
