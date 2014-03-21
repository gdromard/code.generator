package test.generated.object;

import java.util.Date;

public class Adresse {
	private Long id = null;
	private String rue = null;
	private String codePostal = null;
	private String ville = null;

	private Date modified = null;
	private Date created = null;
	private boolean deleted = false;

	public Adresse() { /* Empty JavaBean constructor */ }

	public Adresse(Long id) {
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


	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
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
		return "Adresse: " +
			"Rue: " + rue + " | " +
			"CodePostal: " + codePostal + " | " +
			"Ville: " + ville + " | " +

		"";
	}
}