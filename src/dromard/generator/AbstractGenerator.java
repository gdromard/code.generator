package dromard.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class AbstractGenerator {
	private StringBuffer log = new StringBuffer();
	private StringBuffer files = new StringBuffer();
	
	public AbstractGenerator() {
		log.append("<h2>Logs</h2>");
		files.append("<h2>File Generation</h2>");
	}

	public String getLog() {
		try {
			return files.append(files).append(log).toString();
		} finally {
			// Clear logs
			log = new StringBuffer();
			files = new StringBuffer();
			log.append("<h2>Logs</h2>");
			files.append("<h2>File Generation</h2>");
		}
	}
	
	protected void logStep(final String step) {
		System.out.println("[DEBUG] ------------ " + step);
		files.append("<h3>" + step + "</h3>");
		log.append("<span class='step'>----------- " + step + "</span><br/>");
	}

	private void warmTemplateNotFound(String templateName) {
		//System.err.println("[WARNING] Resource '" + templateName + ".tmpl" + "' has not been found");
		log.append("<span class='warning' style='color: orange'>[WARNING] Resource '" + templateName + ".tmpl" + "' has not been found</span><br/>");
	}

	private void warmSkippingFile(final File file) {
		//System.err.println("[WARNING] File " + file.getAbsolutePath() + " already existing skipping !");
		log.append("<span class='warning' style='color: orange'>[SKIPPED] " + file.getAbsolutePath() + ".</span><br/>");
		files.append("<span class='warning' style='color: orange'>[SKIPPED] " + file.getAbsolutePath() + ".</span><br/>");
	}

	private void warmKeyIsNull(String key) {
		System.err.println("[WARNING] key value for " + key + " is NULL");
		log.append("<span class='warning' style='color: orange'>[WARNING] key value for " + key + " is NULL</span><br/>");
	}

	private void logErrorOccured(String result, String key, String value, Exception ex) {
		System.err.println("[ERROR] An error occured while replacing ${" + key + "} with: \n" + value + "\n\tin:\n" + result);
		log.append("<span class='error' style='color: darkred'>[ERROR] An error occured while replacing ${" + key + "} with: \n" + value + "\n\tin:\n" + result + "</span><br/>");
	}

	private void logGeneratedFile(final File file) {
		//System.out.println("[SUCCESS] File " + file.getAbsolutePath() + " successfully generated.");
		files.append("<span class='success' style='color: green'>[GENERATED] " + file.getAbsolutePath() + ".</span><br/>");
	}

	/**
	 * Retrieve the content of a template.
	 * @param templateName The template name.
	 * @param alternative  The alternative of the template (suffix)
	 * @return The content of the template
	 */
	public String getTemplate(String templateName, String alternative) {
		StringBuffer code = new StringBuffer();
		InputStream resource = null;
		if (alternative != null) {
			resource = AbstractGenerator.class.getResourceAsStream("templates/" + templateName + capitalize(alternative) + ".tmpl");
			//if (resource != null) System.out.println("[DEBUG] templates/" + templateName + capitalize(alternative) + ".tmpl");
		}
		if (resource == null) {
			resource = AbstractGenerator.class.getResourceAsStream("templates/" + templateName + ".tmpl");
			//if (resource != null) System.out.println("[DEBUG] templates/" + templateName + capitalize(alternative) + ".tmpl");
		}
		if (resource == null) {
			warmTemplateNotFound(templateName);
		} else {
			try {
				int len;
		        byte[] b = new byte[1024];
				while ((len = resource.read(b)) != -1) code.append(new String(b, 0, len, "UTF8"));
		        resource.close();
				return code.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

    /**
     * Write string output.
     * @param output   The Output stream, where to write data
     * @param content  The content string to be written
     * @param overide Set it to true if you want to override the file if it already exists
     * @throws IOException While creating the file - if override is true - or while
     */
	protected void writeToFile(final File output, final String content, final boolean overide) throws IOException {
        if (!output.exists() || overide) {
        	if (!output.exists()) {
        		output.getParentFile().mkdirs();
        		output.createNewFile();
        	}
        	Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF8"));
        	try {
	        	out.append(content);
	        	out.flush();
        	} finally {
        		try { out.close(); } catch(Exception e) {}
        	}
            logGeneratedFile(output);
        } else {
        	warmSkippingFile(output);
        }
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected String transform(final String string, final Map<String, Object> params) {
		String result = string;
		for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			Object objValue = params.get(key);
			String value = (objValue != null?objValue.toString():""); 
			if (objValue == null && result.indexOf("${"+key+"}") > -1) warmKeyIsNull(key);

			try {
        		result = replaceAll(result, "${" + key + "}", value);
        		result = replaceAll(result, "$" + key + "", value);
	        } catch(Exception ex) {
	        	logErrorOccured(result, key, value, ex);
	        	ex.printStackTrace();
	        	throw new RuntimeException(ex);
	        }
		}
		for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			Object objValue = params.get(key);
			String value = (objValue != null?objValue.toString():""); 
			if (objValue == null && result.indexOf("${"+key+"}") > -1) warmKeyIsNull(key);

			try {
        		while (result.indexOf("|" + key + "}}") > 0) {
        			String templateName = "";
        			templateName = result.substring(0, result.indexOf("|" + key + "}}"));
        			templateName = templateName.substring(templateName.lastIndexOf("{{")+2);
        			String snippletId = "{{" + templateName + "|" + key + "}}";
        			String snippletCode = "";
        			String[] tmp = templateName.split("\\|");
        			String alternative = null;
        			if (tmp.length == 2) {
        				templateName = tmp[0];
        				alternative = tmp[1];
        				snippletId = "{{" + templateName + "|" + alternative + "|" + key + "}}";
        			}
    				Map<String, Object> snippletParams = new HashMap<String, Object>(params);
    				Object objHandler = params.get(key + "Handler");
    				if (objHandler != null || !(objValue instanceof String)) {
						if (objHandler != null) {
							ParamHandler handler = (ParamHandler) objHandler;
							if (objValue instanceof List) {
								List list = (List) objValue;
								for (Object obj: list) {
									handler.handle(obj, snippletParams);
			            			String template = getTemplate(templateName, capitalize(snippletParams.get(alternative)!=null?snippletParams.get(alternative).toString():alternative));
			            			if (template != null) snippletCode += transform(template, snippletParams);
								}
							} else {
								handler.handle(objValue, snippletParams);
		            			String template = getTemplate(templateName, capitalize(snippletParams.get(alternative)!=null?snippletParams.get(alternative).toString():alternative));
		            			if (template != null) snippletCode += transform(template, snippletParams);
							}
    					} else {
	            			String template = getTemplate(templateName, capitalize(snippletParams.get(alternative)!=null?snippletParams.get(alternative).toString():alternative));
	            			if (template != null) snippletCode += transform(template, snippletParams);
    					}
    				} else {
    					if (!isEmpty(value)) {
    						try {
    							JSONObject json = new JSONObject(value);
    							Iterator<String> it = json.keys();
    							while (it.hasNext()) {
    								String k = it.next();
    								snippletParams.put(k, json.getString(k));
    							}
    	            			String template = getTemplate(templateName, capitalize(snippletParams.get(alternative)!=null?snippletParams.get(alternative).toString():alternative));
    	            			if (template != null) snippletCode += transform(template, snippletParams);
    						} catch (JSONException e) {
    							try {
    								JSONArray json = new JSONArray(value);
    								for (int i=0; i<json.length(); ++i) {
    									JSONObject jsonObj = json.getJSONObject(i);
    									Iterator<String> it = jsonObj.keys();
    									while (it.hasNext()) {
    										String k = it.next();
    										snippletParams.put(k, jsonObj.getString(k));
    									}
    	    	            			String template = getTemplate(templateName, capitalize(snippletParams.get(alternative)!=null?snippletParams.get(alternative).toString():alternative));
    	    	            			if (template != null) snippletCode += transform(template, snippletParams);
    								}
    							} catch (JSONException ex) {
    								System.err.println("[DEBUG] Value '"+value+"' seams not be of JSON type ! (ex: "+ex.getMessage()+")");
    								//ex.printStackTrace();
    								System.err.println("[WARNING] Don't know how to template value '"+value+"' with template '"+templateName+"' !!!!!! ");
    							}
    						}
    					}
        			}
        			result = replaceAll(result, snippletId, snippletCode);
        		}
	        } catch(IllegalArgumentException ex) {
	        	logErrorOccured(result, key, value, ex);
	        	throw ex;
	        }
        }
        return result;
    }

    /**
     * Replace all substring (pattern) found in string (original) with a given string (value).
     * All parameters are expected (must not be null)
     * @param original The original string to parse.
     * @param pattern The pattern to match.
     * @param value The string that will replace the pattern.
     * @return The string with pattern replaced by value.
     * @since 11/03/2005
     */
	protected static String replaceAll(final String original, final String pattern, final String value) {
        StringBuffer copy = new StringBuffer(original);
        int index = -1;
        while ((index = copy.indexOf(pattern, index)) > -1) {
            copy.replace(index, index + pattern.length(), value);
            index += value.length();
        }
        return copy.toString();
    }
	
	protected static String cleanSlashes(String path) {
		return path.replaceAll("^/", "").replaceAll("/$", "").replaceAll("//", "/");
	}

    protected static String capitalize(final String original) {
        if (original == null || original.length() == 0) return original;
        return Character.toTitleCase(original.charAt(0))+original.substring(1);
        
    }

    protected static boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}
}
