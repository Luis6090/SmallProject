package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				Department dep = instantDepartment(rs);
				Seller sel = instantSeller(rs, dep);
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

	@Override
	public List<Seller> findByDepartment(Integer departmentId) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
										"FROM seller INNER JOIN department " + 
										"ON seller.DepartmentId = department.Id " +
										"WHERE DepartmentId = ? " +
										"ORDER BY Name");
			ps.setInt(1, departmentId);
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer,Department> map = new HashMap<>();
			
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller sel = instantSeller(rs,dep);
				list.add(sel);
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	private Seller instantSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller(rs.getInt("Id"),rs.getString("Name"),rs.getString("Email"),rs.getDate("BirthDate").toLocalDate(),rs.getDouble("BaseSalary"),dep);
		return seller;
	}

	private Department instantDepartment(ResultSet rs) throws SQLException {
		Department department = new Department(rs.getInt("DepartmentId"),rs.getString("DepName"));
		return department;
	}

}
