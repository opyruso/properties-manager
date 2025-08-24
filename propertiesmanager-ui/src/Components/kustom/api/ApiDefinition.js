import ApiCallUtils from "../../kernel/utils/ApiCallUtils";

/* HOOK */

export default {
	
	/* ADMIN */

	getAdminUsers(filter, callback = (data) => {console.log("getAdminUsers default success log"), data}, callbackError = (e) => {console.error("getAdminUsers default err log", e)}) {
		try {
		true?
				ApiCallUtils.getSecure('/admin/users/' + filter,
					(data) => {
						console.log("success getAdminRightDemands callback", data);
						callback(data);
					},
					(e) => {
						console.log("error getAdminRightDemands callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	getAdminRightDemands(callback = (data) => {console.log("getAdminRightDemands default success log"), data}, callbackError = (e) => {console.error("getAdminRightDemands default err log", e)}) {
		try {
		true?
				ApiCallUtils.getSecure('/admin/right/demands',
					(data) => {
						console.log("success getAdminRightDemands callback", data);
						callback(data);
					},
					(e) => {
						console.log("error getAdminRightDemands callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	adminToggleRight(userId, appId, envId, level, callback = (data) => {console.log("adminToggleRight default success log"), data}, callbackError = (e) => {console.error("adminToggleRight default err log", e)}) {
		try {
		userId!=null&&appId!=null&&envId!=null&&level!=null?
				ApiCallUtils.getSecureNoContent('/admin/users/' + userId + '/app/' + appId + '/env/' + envId + '/level/' + level,
					() => {
						console.log("success adminToggleRight callback");
						callback();
					},
					(e) => {
						console.log("error adminToggleRight callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	acceptRightDemand(userId, appId, envId, level, callback = (data) => {console.log("acceptRightDemand default success log"), data}, callbackError = (e) => {console.error("acceptRightDemand default err log", e)}) {
		try {
		userId!=null&&appId!=null&&envId!=null&&level!=null?
				ApiCallUtils.getSecureNoContent('/admin/users/' + userId + '/app/' + appId + '/env/' + envId + '/level/' + level + '/accept',
					() => {
						console.log("success acceptRightDemand callback");
						callback();
					},
					(e) => {
						console.log("error acceptRightDemand callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	refuseRightDemand(userId, appId, envId, level, callback = (data) => {console.log("refuseRightDemand default success log"), data}, callbackError = (e) => {console.error("refuseRightDemand default err log", e)}) {
		try {
		userId!=null&&appId!=null&&envId!=null&&level!=null?
				ApiCallUtils.getSecureNoContent('/admin/users/' + userId + '/app/' + appId + '/env/' + envId + '/level/' + level + '/refuse',
					() => {
						console.log("success refuseRightDemand callback");
						callback();
					},
					(e) => {
						console.log("error refuseRightDemand callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	addGlobalVariable(key, callback = (data) => {console.log("refuseRightDemand default success log"), data}, callbackError = (e) => {console.error("refuseRightDemand default err log", e)}) {
		try {
		key!=null?
				ApiCallUtils.postSecureNoContent('/globalvariables',
					{
						"key":  key
					},
					() => {
						console.log("success refuseRightDemand callback");
						callback();
					},
					(e) => {
						console.log("error refuseRightDemand callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	updateGlobalVariableValue(key, env, value, callback = (data) => {console.log("refuseRightDemand default success log"), data}, callbackError = (e) => {console.error("refuseRightDemand default err log", e)}) {
		try {
		key!=null&&env!=null&&value!=null?
				ApiCallUtils.putSecureNoContent('/globalvariables',
					{
						"key":  key,
						"env":  env,
						"value":  value
					},
					() => {
						console.log("success refuseRightDemand callback");
						callback();
					},
					(e) => {
						console.log("error refuseRightDemand callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	updateGlobalVariableProtection(key, env, isprotected, callback = (data) => {console.log("refuseRightDemand default success log"), data}, callbackError = (e) => {console.error("refuseRightDemand default err log", e)}) {
		try {
		key!=null&&env!=null&&value!=null?
				ApiCallUtils.putSecureNoContent('/globalvariables',
					{
						"key":  key,
						"env":  env,
						"value":  value,
						"isprotected":  isprotected
					},
					() => {
						console.log("success refuseRightDemand callback");
						callback();
					},
					(e) => {
						console.log("error refuseRightDemand callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	deleteGlobalVariable(key, callback = (data) => {console.log("refuseRightDemand default success log"), data}, callbackError = (e) => {console.error("refuseRightDemand default err log", e)}) {
		try {
		key!=null?
				ApiCallUtils.deleteSecureNoContent('/globalvariables',
					{
						"key":  key
					},
					() => {
						console.log("success refuseRightDemand callback");
						callback();
					},
					(e) => {
						console.log("error refuseRightDemand callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},
	
	
	/* USER */

	getGlobalVariables(callback = (data) => {console.log("getGlobalVariables default success log"), data}, callbackError = (e) => {console.error("getGlobalVariables default err log", e)}) {
		try {
			ApiCallUtils.getSecure('/globalvariables',
					(data) => {
						console.log("success getGlobalVariables callback");
						data.keys?.sort((a,b) => (a.globalVariableKey > b.globalVariableKey) ? 1 : ((b.globalVariableKey > a.globalVariableKey) ? -1 : 0));
						callback(data);
					},
					(e) => {
						console.log("error getGlobalVariables callback", e);
						callbackError(e);
					}
				)
		} catch (e) {
			callbackError(e);
		}
	},

	demandNewRight(userId, appId, envId, level,
			callback = (data) => {console.log("demandNewRight default success log"), data}, callbackError = (e) => {console.error("demandNewRight default err log", e)}) {
		try {
		userId!=null&&appId!=null&&envId!=null&&level!=null?
				ApiCallUtils.putSecure('/user/' + userId + '/right/demand',
					{
						"appId": appId,
						"envId": envId,
						"level": level
					},
					(data) => {
						console.log("success demandNewRight callback", data);
						callback(data);
					},
					(e) => {
						console.log("error demandNewRight callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	getRightDemands(userId,
			callback = (data) => {console.log("getRightDemands default success log"), data}, callbackError = (e) => {console.error("getRightDemands default err log", e)}) {
		try {
		userId!=null?
				ApiCallUtils.getSecure('/user/' + userId + '/right/demands',
					(data) => {
						console.log("success getRightDemands callback", data);
						callback(data);
					},
					(e) => {
						console.log("error getRightDemands callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},
	
	
	
	
	
	
	
	
	
	

	getApplications(callback = (data) => {console.log("getApplications default success log"), data}, callbackError = (e) => {console.error("useApplications default err log", e)}) {
		try {
		ApiCallUtils.getSecure('/apps',
				(data) => {
					data.sort((a,b) => (a.appLabel > b.appLabel) ? 1 : ((b.appLabel > a.appLabel) ? -1 : 0));
					callback(data);
				},
				(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},
	
	getApplicationVersions(appId,
			callback = (data) => {console.log("getApplicationVersions default success log"), data},
			callbackError = (e) => {console.error("getApplicationVersions default err log", e)}) {
		try {
		ApiCallUtils.getSecure('/app/' + appId + '/versions',
				(data) => {
					callback(data);
				},
				(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},
	
	getFilenamesByAppIdAndVersion(appId, version,
			callback = (data) => {console.log("getFilenamesByAppIdAndVersion default success log"), data},
			callbackError = (e) => {console.error("getFilenamesByAppIdAndVersion default err log", e)}) {
		try {
		ApiCallUtils.getSecure('/app/' + appId + '/version/' + version + '/filenames',
				(data) => {
					callback(data);
				},
				(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},
	
	getFilesByAppIdAndVersion(appId, version,
			callback = (data) => {console.log("getFilesByAppIdAndVersion default success log"), data},
			callbackError = (e) => {console.error("getFilesByAppIdAndVersion default err log", e)}) {
		try {
		ApiCallUtils.getSecure('/app/' + appId + '/version/' + version + '/files',
				(data) => {
					callback(data);
				},
				(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},
	
	addOrUpdateFile(appId, version, filename, fileContent,
			callback = (data) => {console.log("getFilesByAppIdAndVersion default success log"), data},
			callbackError = (e) => {console.error("getFilesByAppIdAndVersion default err log", e)}) {
		try {
		ApiCallUtils.putSecureNoContent('/app/' + appId + '/version/' + version + '/file',
					{
						"filename":  filename,
						"contentAsBase64":  btoa(fileContent)
					},
				(data) => {
					callback(data);
				},
				(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},
	
	getApplicationDetails(appId, version,
			callback = (data) => {console.log("getApplicationDetails default success log"), data},
			callbackError = (e) => {console.error("getApplicationDetails default err log", e)}) {
		try {
		ApiCallUtils.getSecure('/app/' + appId + '/version/' + version,
				(data) => {
					data.properties?.sort((a,b) => (a.filename+a.propertyKey > b.filename+b.propertyKey) ? 1 : ((b.filename+b.propertyKey > a.filename+a.propertyKey) ? -1 : 0));
					callback(data);
				},
				(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},

	updateApplication(appId, productOwner,
			callback = (data) => {console.log("updateApplication default success log"), data},
			callbackError = (e) => {console.error("updateApplication default err log", e)}) {
		try {
		productOwner!==undefined&&productOwner!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId,
					{
						"productOwner": productOwner
					},
					() => {
						console.log("success updateApplication callback");
						callback();
					},
					(e) => {
						console.log("error updateApplication callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	updateProperty(appId, envId, version, filename, propertyKey, newValue,
			callback = (data) => {console.log("updateProperty default success log"), data},
			callbackError = (e) => {console.error("updateProperty default err log", e)}) {
		try {
		appId!=null&&envId!=null&&version!=null&&filename!=null&&propertyKey!=null&&newValue!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId + '/updateproperty',
					{
						"appId": appId,
						"envId": envId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey,
						"newValue": newValue
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	addProperty(appId, version, filename, newPropertyKey, newValue,
			callback = (data) => {console.log("addProperty default success log"), data},
			callbackError = (e) => {console.error("addProperty default err log", e)}) {
		try {
		appId!=null&&version!=null&&filename!=null&&newPropertyKey!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId + '/addproperty',
					{
						"appId": appId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": newPropertyKey,
						"newValue": newValue,
						"propertyType": "NEW"
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	switchPropertyStatus(appId, envId, version, filename, propertyKey, newStatus,
			callback = (data) => {console.log("switchPropertyStatus default success log"), data},
			callbackError = (e) => {console.error("switchPropertyStatus default err log", e)}) {
		try {
		appId!=null&&envId!=null&&version!=null&&filename!=null&&propertyKey!=null&&newStatus!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId + '/updateproperty',
					{
						"appId": appId,
						"envId": envId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey,
						"status": newStatus
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	switchPropertyIsProtected(appId, envId, version, filename, propertyKey, newProtected,
			callback = (data) => {console.log("switchPropertyIsProtected default success log"), data},
			callbackError = (e) => {console.error("switchPropertyIsProtected default err log", e)}) {
		try {
		appId!=null&&envId!=null&&version!=null&&filename!=null&&propertyKey!=null&&newProtected!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId + '/updateproperty',
					{
						"appId": appId,
						"envId": envId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey,
						"isProtected": newProtected
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	switchPropertyOperationType(appId, envId, version, filename, propertyKey, newOperationType,
			callback = (data) => {console.log("switchPropertyOperationType default success log"), data},
			callbackError = (e) => {console.error("switchPropertyOperationType default err log", e)}) {
		try {
		appId!=null&&envId!=null&&version!=null&&filename!=null&&propertyKey!=null&&newOperationType!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId + '/updateproperty',
					{
						"appId": appId,
						"envId": envId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey,
						"operationType": newOperationType
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	removePropertyAllEnv(appId, version, filename, propertyKey,
			callback = (data) => {console.log("removePropertyAllEnv default success log"), data},
			callbackError = (e) => {console.error("removePropertyAllEnv default err log", e)}) {
		try {
		appId!=null&&version!=null&&filename!=null&&propertyKey!=null?
				ApiCallUtils.deleteSecureNoContent('/app/' + appId + '/property/deleteall',
					{
						"appId": appId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	removePropertyPermanent(appId, version, filename, propertyKey,
			callback = (data) => {console.log("removePropertyPermanent default success log"), data},
			callbackError = (e) => {console.error("removePropertyPermanent default err log", e)}) {
		try {
		appId!=null&&version!=null&&filename!=null&&propertyKey!=null?
				ApiCallUtils.deleteSecureNoContent('/app/' + appId + '/property/deletepermanent',
					{
						"appId": appId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	addPropertyAllEnv(appId, version, filename, propertyKey,
			callback = (data) => {console.log("addPropertyAllEnv default success log"), data},
			callbackError = (e) => {console.error("addPropertyAllEnv default err log", e)}) {
		try {
		appId!=null&&version!=null&&filename!=null&&propertyKey!=null?
				ApiCallUtils.putSecureNoContent('/app/' + appId + '/property/addall',
					{
						"appId": appId,
						"numVersion": version,
						"filename": filename,
						"propertyKey": propertyKey
					},
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},

	replaceAllPropertiesByVersion(appId, fromVersion, toVersion,
			callback = (data) => {console.log("addPropertyAllEnv default success log"), data},
			callbackError = (e) => {console.error("addPropertyAllEnv default err log", e)}) {
		try {
		appId!=null&&fromVersion!=null&&toVersion!=null?
				ApiCallUtils.getSecureNoContent('/app/' + appId + '/version/' + toVersion + '/replaceby/' + fromVersion,
					() => {
						console.log("success updateProperty callback");
						callback();
					},
					(e) => {
						console.log("error updateProperty callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
			callbackError(e);
		}
	},
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	testFile(appId, version, filename, envId, fileContent,
			callback = (data) => {console.log("useApplicationVersions default success log"), data},
			callbackError = (e) => {console.error("useApplicationVersions default err log", e)}) {
		try {
			ApiCallUtils.postSecure('/test/process',
						{
							"appId": appId,
							"version": version,
							"filename": filename,
							"envId": envId,
							"fileContentAsBase64": btoa(fileContent)
						},
					(data) => {
						callback(
							atob(data.fileContentAsBase64),
							data.logs
							);
					},
					(error) => callbackError(error))
		} catch (e) {
			callbackError(e);
		}
	},
	
}
