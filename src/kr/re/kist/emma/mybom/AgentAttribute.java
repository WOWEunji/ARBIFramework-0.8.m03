package kr.re.kist.emma.mybom;

import java.util.HashMap;
import java.util.Map;

public class AgentAttribute {
	private String mAlias;
	private String mName;
	private String mVersion;
	private String mAuthor;
	private String mDescription;
	private Map<String, String> mMap;

	public AgentAttribute() {
		mAlias = null;
		mName = null;
		mVersion = null;
		mDescription = null;
		mMap = new HashMap<String, String>();
	}

	public AgentAttribute(String alias, String name, String version, String author, String description) {
		mAlias = alias;
		mName = name;
		mVersion = version;
		mAuthor = author;
		mDescription = description;
		mMap = new HashMap<String, String>();
	}

	public AgentAttribute(String alias, String author) {
		mAlias = alias;
		mName = author;
		mVersion = null;
		mAuthor = null;
		mDescription = null;
		mMap = new HashMap<String, String>();
	}

	public AgentAttribute(AgentAttribute attr) {
		mAlias = attr.mAlias;
		mName = attr.mName;
		mVersion = attr.mVersion;
		mAuthor = attr.mAuthor;
		mDescription = attr.mDescription;
		mMap = new HashMap<String, String>(attr.mMap);
	}

	protected void put(String key, String value) {
		String lowerKey = key.toLowerCase();
		switch (lowerKey) {
		case "alias":
			mAlias = value;
			break;
		case "name":
			mName = value;
		case "version":
			mVersion = value;
		case "author":
			mAuthor = value;
		case "description":
			mDescription = value;
		}
		
		mMap.put(lowerKey ,value);
	}

	public String alias() {
		return mAlias;
	}

	public String name() {
		return mName;
	}

	public String version() {
		return mVersion;
	}

	public String author() {
		return mAuthor;
	}

	public String description() {
		return mDescription;
	}

	public String get(String key) {
		return mMap.get(key);
	}
	
	@Override
	public String toString() {
		return mAlias+","+mName+","+mVersion+","+mAuthor;
	}
}
