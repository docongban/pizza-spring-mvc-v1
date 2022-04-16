package com.docongban.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "order_detail")
@Data
public class OrderDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String productTitle;
	private String productContent;
	private String productThumbnail;
	private long productPrice;
	private int orderQuantity;
	private int orderAccountId;
}
