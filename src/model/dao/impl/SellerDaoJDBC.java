package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
										"FROM seller INNER JOIN department " + 
										"ON seller.DepartmentId = department.Id " +
										"WHERE seller.Id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				Department dep = new Department(rs.getInt("DepartmentId"),rs.getString("DepName"));
				Seller sel = new Seller(rs.getInt("Id"),rs.getString("Name"),rs.getString("Email"),rs.getDate("BirthDate").toLocalDate(),rs.getDouble("BaseSalary"),dep);
				return sel;
			}
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
