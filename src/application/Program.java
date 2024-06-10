package application;

import java.time.LocalDate;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("\t====Test 1: findById====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\t====Test 2: findByDepartment====");
		List<Seller> sellerList = sellerDao.findByDepartment(2);
		for(Seller list : sellerList) {
			System.out.println(list);
		}
		
		System.out.println("\t====Test 3: findByDepartment====");
		sellerList = sellerDao.findAll();
		for(Seller list : sellerList) {
			System.out.println(list);
		}

		System.out.println("\t====Test 4: insert====");
		Seller newSeller = new Seller(null,"Fabiana","fabiana@gmail.com",LocalDate.now(),4500.00,new Department(3,"Eletronics"));
		sellerDao.insert(newSeller);
		System.out.println("New Id: " + newSeller.getId());
		
		System.out.println("\t====Test 5: update====");
		seller = sellerDao.findById(9);
		seller.setName("Dionisio");
		seller.setEmail("dionisio@gmail.com");
		sellerDao.update(seller);
		System.out.println("Update Success!");
		
		System.out.println("\t====Test 6: delete====");
		sellerDao.deleteById(10);
		System.out.println("Delete sucess!");
		

	}

}
