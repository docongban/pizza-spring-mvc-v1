package com.docongban.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "product")
@Data
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private long price;
	private String thumbnail;
	private String content;
	private Date createdAt;
	private Date updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "id_category")
	private Category category;
}
