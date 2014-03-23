package dromard.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGenerator extends AbstractGenerator {
	
	private static String GENERATION_PATH = new File("TEST/src").getAbsolutePath();
	
	public TestGenerator() {
	}

	public static class TestObject {
		String packageName = "test.generated.object";
		String object = "Test";
		List<TestAttribute> attributes = new ArrayList<TestAttribute>();
		public String getPackageName() { return packageName; }
		public TestObject setPackageName(String packageName) { this.packageName = packageName; return this; }
		public String getObject() { return object; }
		public TestObject setObject(String object) { this.object = object; return this; }
		public List<TestAttribute> getProperties() { return attributes; }
		public TestObject addAttribute(TestAttribute attribute) { this.attributes.add(attribute); return this; }
	}

	public static class TestAttribute {
		String attribute = "Test";
		String type = "String";
		public String getType() { return type; }
		public TestAttribute setType(String type) { this.type = type; return this; }
		public String getAttribute() { return attribute; }
		public TestAttribute setAttribute(String attribute) { this.attribute = attribute; return this; }
	}
	
	public String generateCode() throws IOException {
		logStep("Construction des Objets");

		Map<String, Object> params = new HashMap<String, Object>();

		// construction des variables de generation sous forme d'objet
		List<TestObject> beans = new ArrayList<TestObject>();
		beans.add(
			new TestObject()
			.setObject("utilisateur")
			.addAttribute(new TestAttribute().setAttribute("nom").setType("String"))
			.addAttribute(new TestAttribute().setAttribute("prenom").setType("String"))
			.addAttribute(new TestAttribute().setAttribute("email").setType("String"))
			.addAttribute(new TestAttribute().setAttribute("telephone").setType("String"))
			.addAttribute(new TestAttribute().setAttribute("comment").setType("Blob"))
			.addAttribute(new TestAttribute().setAttribute("dateDeNaissance").setType("Date"))
		);
		beans.add(
			new TestObject()
			.setObject("adresse")
			.addAttribute(new TestAttribute().setAttribute("rue").setType("String"))
			.addAttribute(new TestAttribute().setAttribute("codePostal").setType("String"))
			.addAttribute(new TestAttribute().setAttribute("ville").setType("String"))
		);
		
		logStep("Generation du code");

		for (TestObject bean: beans) {
			logStep("Generating Object " + bean.getObject());
			// Definition of attributes (template variables)
			params.put("packageName", bean.getPackageName());
			params.put("object", bean.getObject());
			params.put("Object", capitalize(bean.getObject()));
			params.put("attributes", bean.getProperties());
			params.put("attributesHandler", new ParamHandler<TestAttribute>() {
				@Override
				public void handle(TestAttribute attribute, Map<String, Object> params) {
					params.put("attribute", attribute.getAttribute());
					params.put("Attribute", capitalize(attribute.getAttribute()));
					params.put("attributeType", attribute.getType());
				}
			});

			// Generate the file
			generateFile(params, 	// Params built just before
				"test/TestObject", 		// The template name
				GENERATION_PATH + "/" + bean.getPackageName().replace('.', '/') + "/" + capitalize(bean.getObject()) + ".java", // The file name to be generated 
				true  				// Do the file has to be overridden (or skipped if already existing) ?
			);
		}
		
		logStep("Generating Object Based on Json JSONObject");
		// Definition of attributes (template variables)
		TestObject bean = new TestObject().setObject("JsonObject");
		params.put("packageName", bean.getPackageName());
		params.put("object", bean.getObject());
		params.put("Object", capitalize(bean.getObject()));
		params.put("attributes", "["
				+ "{ attribute: 'nom', Attribute: 'Nom', attributeType: 'String' },"
				+ "{ attribute: 'prenom', Attribute: 'Prenom', attributeType: 'String' },"
				+ "{ attribute: 'email', Attribute: 'Email', attributeType: 'String' },"
				+ "{ attribute: 'age', Attribute: 'Age', attributeType: 'int' },"
				+ "{ attribute: 'dateDeNaissance', Attribute: 'DateDeNaissance', attributeType: 'Date' }"
				+ "]");
		params.put("attributesHandler", null);

		// Generate the file
		generateFile(params, 	// Params built just before
			"test/TestObject", 		// The template name
			GENERATION_PATH + "/" + bean.getPackageName().replace('.', '/') + "/" + capitalize(bean.getObject()) + ".java", // The file name to be generated 
			true  				// Do the file has to be overridden (or skipped if already existing) ?
		);

		
		return getLog();
	}

	private void generateFile(Map<String, Object> params, String template, String file, boolean overide) throws IOException {
		String code = getTemplate(template, null);
		if (code == null) {
			try {
				throw new Exception("Template: " + template + " has not been found");
			} catch(Exception e) {
				e.printStackTrace();
			}
			code = "";
		}
		writeToFile(new File(file), transform(code, params), true);
	}
}
