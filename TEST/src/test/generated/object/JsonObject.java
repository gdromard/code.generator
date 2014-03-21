package test.generated.object;

import java.util.Date;

public class JsonObject {
	private Long id = null;
	private String nom = null;
	private String prenom = null;
	private String email = null;
	private int age;
	private Date dateDeNaissance = null;

	private Date modified = null;
	private Date created = null;
	private boolean deleted = false;

	public JsonObject() { /* Empty JavaBean constructor */ }

	public JsonObject(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(String id) {
		this.id = Long.parseLong(id);
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	public Date getDateDeNaissance() {
		return dateDeNaissance;
	}

	public void setDateDeNaissance(Date dateDeNaissance) {
		this.dateDeNaissance = dateDeNaissance;
	}

	public void setDateDeNaissance(String dateDeNaissance) {
		if (dateDeNaissance != null && dateDeNaissance.trim().length()>0) 
			this.dateDeNaissance = new Date(Long.parseLong(dateDeNaissance));
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public void setModified(String modified) {
		if (modified != null && modified.trim().length()>0) 
			this.modified = new Date(Long.parseLong(modified));
	}
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setCreated(String created) {
		if (created != null && created.trim().length()>0) 
			this.created = new Date(Long.parseLong(created));
	}

	public boolean isDeleted() {
	    return deleted;
    }
	
	public void setDeleted(String deleted) {
		this.deleted = (deleted.equalsIgnoreCase("true")||deleted.equals("1"));
	}

	public void setDeleted(boolean deleted) {
	    this.deleted = deleted;
    }
	
	public String toString() {
		return "JsonObject: " +
			"Nom: " + nom + " | " +
			"Prenom: " + prenom + " | " +
			"Email: " + email + " | " +
			"Age: " + age + " | " +
			"DateDeNaissance: " + dateDeNaissance + " | " +

		"";
	}
}