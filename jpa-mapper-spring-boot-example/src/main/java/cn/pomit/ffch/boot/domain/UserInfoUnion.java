package cn.pomit.ffch.boot.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Table;

import org.apache.ibatis.mapping.FetchType;

import cn.pomit.jpamapper.core.annotation.Many;


/**
 * The persistent class for the user_info database table.
 * 
 */
@Table(name="user_info")
public class UserInfoUnion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=64)
	private String userName;

	@Column(length=20)
	private String mobile;

	@Column(length=64)
	private String name;

	@Column(nullable=false, length=32)
	private String passwd;

	@Column(length=2)
	private String valid;
	
	@Many(fetchType=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="userName",referencedColumnName="userName"),
		@JoinColumn(name="mobile",referencedColumnName="phone")
	})
	private List<UserRole> userRole;

	public UserInfoUnion() {
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return this.mobile;
	}
	
	public String[] getTables() {
		if(this.mobile.startsWith("1")){
			return new String[]{"user_info", "user_info_copy1", "user_info_copy2"};
		}else if(this.mobile.startsWith("2")){
			return new String[]{"user_info_copy1", "user_info_copy2"};
		}
		return new String[]{"user_info", "user_info_copy1", "user_info_copy2"};
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getValid() {
		return this.valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}
	
	

	public List<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<UserRole> userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", mobile=" + mobile + ", name=" + name + ", passwd=" + passwd
				+ ", valid=" + valid + "]";
	}
}