package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection connectBank;
	
	public DepartmentDaoJDBC(Connection connectBank) {
		this.connectBank = connectBank;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		
		try {
			ps = connectBank.prepareStatement("INSERT INTO department "
											+ "(Name) "	
					                        + "VALUES " 
					                        + "(?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, obj.getName());
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
				throw new DbException("Unexpected error!No row affected.");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		
		try {
			ps = connectBank.prepareStatement("Update department "
											+ "SET Name = ? "	
					                        + "WHERE Id = ?");
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
		    int affectedRow = ps.executeUpdate();
			
			if(affectedRow == 0) {
				throw new DbException("Unexpected error!No row affected.");
			}
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = connectBank.prepareStatement("DELETE FROM department "
					                        + "WHERE Id = ?");
			ps.setInt(1, id);
		    int affectedRow = ps.executeUpdate();
			
			if(affectedRow == 0) {
				throw new DbException("Unexpected error!No row affected.");
			}
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = connectBank.prepareStatement("SELECT department.* "
											  + "FROM department " 
											  + "WHERE Id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			Department department = null;
			if(rs.next()) {
				department = createDepartment(rs);
			}	
				
			return department;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = connectBank.prepareStatement("SELECT department.* "
											  + "FROM department " 
											  + "ORDER BY Name");
			rs = ps.executeQuery();
			List<Department> departmentList = new ArrayList<>();
			while(rs.next()) {
				departmentList.add(createDepartment(rs));
			}
			return departmentList;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	
	public static Department createDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(rs.getInt("Id"),rs.getString("Name"));
		return dep;
	}
	
}
