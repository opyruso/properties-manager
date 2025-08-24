import React from "react";

export const replaceJSX = (subject, find, replace) => {
	const result = [];
	if (Array.isArray(subject)) {
		for (let part of subject)
			result = [...result, replaceJSX(part, find, replace)]
		return result;
	} else if (typeof subject !== 'string')
		return subject;
	let parts = subject.split(find);
	for (let i = 0; i < parts.length; i++) {
		result.push(parts[i]);
		if ((i + 1) !== parts.length)
			result.push(replace);
	}
	return result;
}

export const replaceJSXRecursive = (subject, replacements) => {
	for (let key in replacements) {
		subject = replaceJSX(subject, key, replacements[key])
	}
	return subject;
}



export const underlineSearchAndReplace = (subject, find) => {
	if (find.trim().length === 0) return subject;
	
	const result = [];
	if (Array.isArray(subject)) {
		for (let part of subject)
			result = [...result, searchAndReplace(part, find)]
		return result;
	} else if (typeof subject !== 'string')
		return subject;
	let parts = subject.split(find);
	for (let i = 0; i < parts.length; i++) {
		result.push(parts[i]);
		if ((i + 1) !== parts.length)
			result.push(<span className="underline">{find}</span>);
	}
	if (result.length === 0) result.push(subject);
	return result;
}

export const underlineProperties = (subject, find) => {
	if (find.trim().length === 0) return subject;
	
	const result = [];
	if (Array.isArray(subject)) {
		for (let part of subject)
			result = [...result, underlineProperties(part, find)]
		return result;
	} else if (typeof subject !== 'string')
		return subject;

	let parts = subject.split("\n");
	for (let i = 0; i < parts.length; i++) {
		if (parts[i].trim() !== ""
			&& !parts[i].trim().startsWith("#")
			&& parts[i].includes("=")) {
			let f = parts[i].split(find);
			let equalsignFound = false;
			f.map((e) => {
					equalsignFound = equalsignFound || e.includes("=");
					(!equalsignFound && e !== f[f.length - 1])?result.push(e, <span className="underline">{find}</span>):result.push(e, (e !== f[f.length - 1])?find:null);
				})
		} else {
		result.push(parts[i]);
		}
		result.push("\n");
	}
	if (result.length === 0) result.push(subject);
	return result;
}



export const underlineXpath = (subject, find) => {
	if (find.trim().length === 0) return subject;

	function s4() {
	  return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
	}
	function guid() {
	  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
	}
	
	const result = [];
	if (Array.isArray(subject)) {
		for (let part of subject)
			result = [...result, underlineProperties(part, find)]
		return result;
	} else if (typeof subject !== 'string')
		return subject;

	try {
		var parser = new DOMParser();
		var printer = new XMLSerializer();
	
		var xml = parser.parseFromString(subject, 'text/xml');
		var nodes = xml.evaluate(find, xml, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
		var id = guid();
		for (var i = 0; i < nodes.snapshotLength; i++) {
			var node = nodes.snapshotItem(i);
			console.log("node.nodeValue VALUE : " + node.nodeValue, node.nodeValue);
			if (node.nodeValue === null) {
				node.parentNode.insertBefore(xml.createComment('pmutilsunderliner_' + (i+1) + '_' + id), node);
				node.parentNode.insertBefore(xml.createComment('pmutilsunderliner_' + (i+1) + '_' + id), node.nextSibling);
			} else {
				node.nodeValue ='##pmutilsunderliner##' + node.nodeValue + '##pmutilsunderliner##';
			}
		}
		let tmp = printer.serializeToString(xml);
		let fmain = tmp.split(/<!--pmutilsunderliner_.*?-->/g);
		for (let i = 0; i < fmain.length; i = i + 2) {
			let fsecondary = fmain[i].split(/##pmutilsunderliner##/g);
			if (fsecondary.length > 1) {
				for (let i = 0; i < fsecondary.length; i = i + 2) {
					result.push(fsecondary[i], <React.Fragment>
							<span className="underline">{fsecondary[i+1]}</span>
						</React.Fragment>)
				}	
			} else {
				result.push(fmain[i], <React.Fragment>
						<span className="underline">{fmain[i+1]}</span>
					</React.Fragment>)	
			}
		}
			
	} catch (e) {
		console.error("not an XPATH expression", e);
		result.push(subject);
	}

	if (result.length === 0) result.push(subject);
	return result;
}


import {JSONPath} from 'jsonpath-plus';
export const underlineJsonPath = (subject, find) => {
	if (find.trim().length === 0) return subject;

	
	const result = [];
	if (Array.isArray(subject)) {
		for (let part of subject)
			result = [...result, underlineProperties(part, find)]
		return result;
	} else if (typeof subject !== 'string')
		return subject;

	try {
		
		let callback = (payload, type, obj) => {
			if (type == 'value') {
				obj.parent[obj.parentProperty] = '##pmutilsunderliner##' + obj.parent[obj.parentProperty] + '##pmutilsunderliner##';
			}
			return payload;
		};
	
		let json = JSON.parse(subject);
		JSONPath({path: find, json: json, callback: callback, resultType: "all", wrap: false});
		console.log("tmp", json);
		let jsonAsString = JSON.stringify(json, null, 2);
	
		let f = jsonAsString.split(/##pmutilsunderliner##/g);
			if (f.length > 1) {
				for (let i = 0; i < f.length; i = i + 2) {
					result.push(f[i], <React.Fragment>
							<span className="underline">{f[i+1]}</span>
						</React.Fragment>)
				}	
			}
			
	} catch (e) {
		console.error("not an XPATH expression", e);
		result.push(subject);
	}
	if (result.length === 0) result.push(subject);
	return result;
}

















