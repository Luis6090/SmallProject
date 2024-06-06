package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
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

		
		

	}

}
