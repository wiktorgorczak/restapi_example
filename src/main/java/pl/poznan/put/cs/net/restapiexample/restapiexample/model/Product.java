package pl.poznan.put.cs.net.restapiexample.restapiexample.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	private String name;
	
	private String description;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Opinion> opinions;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<Opinion> getOpinions() {
		return opinions;
	}
	
	public void setOpinions(Set<Opinion> opinions) {
		this.opinions = opinions;
	}
}
