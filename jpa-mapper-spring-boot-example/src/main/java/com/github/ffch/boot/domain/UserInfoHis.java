package com.github.ffch.boot.domain;

import java.io.Serializable;
import javax.persistence.*;

import com.github.ffch.jpamapper.core.annotation.ShardingKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The persistent class for the user_info database table.
 * 
 */
@Table(name="user_info_his")
public class UserInfoHis implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=64)
	private String userName;

	@Column(length=20)
	@ShardingKey(prefix="_", methodPrecis="getTable", methodRange = "getTables")
	private String mobile;

	@Column(length=64)
	private String name;

	@Column(nullable=false, length=32)
	private String passwd;

	@Column(length=2)
	private String valid;

	public UserInfoHis() {
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
	
	public static String getTable(Object mobile) {
		int index = Integer.parseInt(mobile.toString()) % 5;
		return String.valueOf(index);
	}
	
	public static String[] getTables(Object start, Object end) {
		Map<Integer, String> maps = new HashMap<>();
		int index = 0;
		for(int i = Integer.parseInt(start.toString()); i < Integer.parseInt(end.toString()); i++){
			if(index >= 5)break;
			maps.put(index, String.valueOf(i % 5));
			index++;	
		}
		
		List<String> mapValueList = new ArrayList<String>(maps.values()); 
		String[] arr = new String[mapValueList.size()];
		return mapValueList.toArray(arr);
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

	@Override
	public String toString() {
		return "UserInfo [userName=" + userName + ", mobile=" + mobile + ", name=" + name + ", passwd=" + passwd
				+ ", valid=" + valid + "]";
	}
}