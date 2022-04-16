package com.docongban.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "order_account")
@Data
public class OrderAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String accountFullname;
	private String accountPhone;
	private String accountEmail;
	private String accountAddress;
	private int orderStatus;
	private Date orderDate;
}
