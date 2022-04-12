package com.docongban.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int accountId;
	private int productId;
	private int quantiy;
	private Date orderDate;
}
