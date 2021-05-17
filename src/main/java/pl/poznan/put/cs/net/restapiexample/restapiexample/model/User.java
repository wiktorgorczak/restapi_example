package pl.poznan.put.cs.net.restapiexample.restapiexample.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	
	@Id
	private String login;
	private String email;
	private String name;
	private String surname;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Product> products;
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<Opinion> opinions;
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public Set<Product> getProducts() {
		return products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	public Set<Opinion> getOpinions() {
		return opinions;
	}
	
	public void setOpinions(Set<Opinion> opinions) {
		this.opinions = opinions;
	}
}