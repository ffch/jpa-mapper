package cn.pomit.ffch.boot.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_role database table.
 * 
 */
@Table(name="user_role")
public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="JDBC")
	@Column()
	private Integer id;

	@Column(length=10)
	private String role;
	
	@Column(name ="userName")
	private String userName;
	
	@Column(name ="phone")
	private String phone;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserRole() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", role=" + role + ", userName=" + userName + "]";
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
}