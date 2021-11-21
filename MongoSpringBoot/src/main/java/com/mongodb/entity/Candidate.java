package com.mongodb.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="candidate")
public class Candidate {

	//@ApiModelProperty(name="id",required=true,value="")
	@Id
	private String id;
	
	//@ApiModelProperty(name="name",required=true,value="")
	//@ApiParam("name")
	private String name;
	
	//@ApiModelProperty(name="experience",required=true,value="")
	//@ApiParam("experience")
	private double experience;
	
	//@ApiModelProperty(name="email",required=true,value="")
	//@ApiParam("email")
	
	@Indexed(unique=false)
	private String email;
	
	private List<Skill> skills;
	

}
