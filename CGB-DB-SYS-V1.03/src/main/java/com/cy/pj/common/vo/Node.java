package com.cy.pj.common.vo;

import java.io.Serializable;

import lombok.Data;
/** value object 值对象 
    ps:定义Node封装,用于封装菜单节点树的信息
  */
@Data
public class Node implements Serializable{
	private static final long serialVersionUID = 2048083156365694892L;
	private Integer id;
	private String name;
	private Integer parentId;
}
