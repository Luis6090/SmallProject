package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
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
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
					+ "VALUES " 
					+ "(?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, Date.valueOf(obj.getBirthdate()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5,obj.getDepartment().getId());
			
			int affectedRow = ps.executeUpdate();
			if(affectedRow > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected");
			}
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("UPDATE seller "
					 				  + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
									  + "WHERE Id = ?");
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, Date.valueOf(obj.getBirthdate()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5,obj.getDepartment().getId());
			ps.setInt(6, obj.getId());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			ps.setInt(1, id);

			int affectedRow = ps.executeUpdate();
			if(affectedRow == 0) {
				throw new DbException("This id don't exist!");
			}
		}	
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally{
			DB.closeStatement(ps);
		}
		
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
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
										"FROM seller INNER JOIN department " + 
										"ON seller.DepartmentId = department.Id " +
										"ORDER BY Name");
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
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
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
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
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
