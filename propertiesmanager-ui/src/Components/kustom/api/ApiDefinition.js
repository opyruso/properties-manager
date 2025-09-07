import ApiCallUtils from "../../kernel/utils/ApiCallUtils";

/* HOOK */

export default {
	
	/* ADMIN */

	getAdminUsers(filter, callback = (data) => {console.log("getAdminUsers default success log"), data}, callbackError = (e) => {console.error("getAdminUsers default err log", e)}) {
		try {
		true?
                                ApiCallUtils.getSecure('/admin/users/' + filter,
                                        (data) => {
                                                console.log("success getAdminUsers callback", data);
                                                callback(data);
                                        },
                                        (e) => {
                                                console.log("error getAdminUsers callback", e);
                                                callbackError(e);
                                        }
                                ):null
		} catch (e) {
                        console.error(e);
                        callbackError(e);
		}
	},


        addGlobalVariable(key, callback = (data) => {console.log("addGlobalVariable default success log"), data}, callbackError = (e) => {console.error("addGlobalVariable default err log", e)}) {
		try {
		key!=null?
				ApiCallUtils.postSecureNoContent('/globalvariables',
					{
						"key":  key
					},
					() => {
                                                console.log("success addGlobalVariable callback");
						callback();
					},
					(e) => {
                                                console.log("error addGlobalVariable callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
                        console.error(e);
                        callbackError(e);
		}
	},

        updateGlobalVariableValue(key, env, value, callback = (data) => {console.log("updateGlobalVariableValue default success log"), data}, callbackError = (e) => {console.error("updateGlobalVariableValue default err log", e)}) {
		try {
		key!=null&&env!=null&&value!=null?
				ApiCallUtils.putSecureNoContent('/globalvariables',
					{
						"key":  key,
						"env":  env,
						"value":  value
					},
					() => {
                                                console.log("success updateGlobalVariableValue callback");
						callback();
					},
					(e) => {
                                                console.log("error updateGlobalVariableValue callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
                        console.error(e);
                        callbackError(e);
		}
	},

        updateGlobalVariableProtection(key, env, isprotected, callback = (data) => {console.log("updateGlobalVariableProtection default success log"), data}, callbackError = (e) => {console.error("updateGlobalVariableProtection default err log", e)}) {
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
                                                console.log("success updateGlobalVariableProtection callback");
						callback();
					},
					(e) => {
                                                console.log("error updateGlobalVariableProtection callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
                        console.error(e);
                        callbackError(e);
		}
	},

        deleteGlobalVariable(key, callback = (data) => {console.log("deleteGlobalVariable default success log"), data}, callbackError = (e) => {console.error("deleteGlobalVariable default err log", e)}) {
		try {
		key!=null?
				ApiCallUtils.deleteSecureNoContent('/globalvariables',
					{
						"key":  key
					},
					() => {
                                                console.log("success deleteGlobalVariable callback");
						callback();
					},
					(e) => {
                                                console.log("error deleteGlobalVariable callback", e);
						callbackError(e);
					}
				):null
		} catch (e) {
                        console.error(e);
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
                        console.error(e);
                        callbackError(e);
		}
	},

	
	
	
	
	
	
	
	
	
	

        getApplications(callback = (data) => {console.log("getApplications default success log"), data}, callbackError = (e) => {console.error("useApplications default err log", e)}) {
                try {
                const archives = localStorage.getItem('withArchives') === 'true';
                ApiCallUtils.getSecure('/apps' + (archives ? '?archives=true' : ''),
                                (data) => {
                                        data.sort((a,b) => (a.appLabel > b.appLabel) ? 1 : ((b.appLabel > a.appLabel) ? -1 : 0));
                                        callback(data);
                                },
                                (error) => callbackError(error))
		} catch (e) {
                        console.error(e);
                        callbackError(e);
		}
	},
	
        getApplicationVersions(appId,
                        callback = (data) => {console.log("getApplicationVersions default success log"), data},
                        callbackError = (e) => {console.error("getApplicationVersions default err log", e)}) {
                try {
                const archives = localStorage.getItem('withArchives') === 'true';
                ApiCallUtils.getSecure('/app/' + appId + '/versions' + (archives ? '?archives=true' : ''),
                                (data) => {
                                        callback(data);
                                },
                                (error) => callbackError(error))
                } catch (e) {
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
                        callbackError(e);
		}
	},
	
        getApplicationDetails(appId, version,
                        callback = (data) => {console.log("getApplicationDetails default success log"), data},
                        callbackError = (e) => {console.error("getApplicationDetails default err log", e)}) {
                try {
                const archives = localStorage.getItem('withArchives') === 'true';
                ApiCallUtils.getSecure('/app/' + appId + '/version/' + version + (archives ? '?archives=true' : ''),
                                (data) => {
                                        data.properties?.sort((a,b) => (a.filename+a.propertyKey > b.filename+b.propertyKey) ? 1 : ((b.filename+b.propertyKey > a.filename+a.propertyKey) ? -1 : 0));
                                        callback(data);
                                },
                                (error) => callbackError(error))
		} catch (e) {
                        console.error(e);
                        callbackError(e);
		}
	},

        updateApplication(appId, productOwner, status,
                        callback = (data) => {console.log("updateApplication default success log"), data},
                        callbackError = (e) => {console.error("updateApplication default err log", e)}) {
                try {
                if (productOwner!==undefined&&productOwner!=null) {
                                ApiCallUtils.putSecureNoContent('/app/' + appId,
                                        {
                                                "productOwner": productOwner,
                                                "status": status
                                        },
                                        () => {
                                                console.log("success updateApplication callback");
                                                callback();
                                        },
                                        (e) => {
                                                console.log("error updateApplication callback", e);
                                                callbackError(e);
                                        }
                                );
                } else if (status !== undefined && status != null) {
                                ApiCallUtils.putSecureNoContent('/app/' + appId,
                                        {
                                                "status": status
                                        },
                                        () => {
                                                console.log("success updateApplication callback");
                                                callback();
                                        },
                                        (e) => {
                                                console.log("error updateApplication callback", e);
                                                callbackError(e);
                                        }
                                );
                }
                } catch (e) {
                        console.error(e);
                        callbackError(e);
                }
        },

        archiveApplication(appId,
                        callback = () => {console.log("archiveApplication default success log")},
                        callbackError = (e) => {console.error("archiveApplication default err log", e)}) {
                this.updateApplication(appId, null, 'ARCHIVED', callback, callbackError);
        },

        unarchiveApplication(appId,
                        callback = () => {console.log("unarchiveApplication default success log")},
                        callbackError = (e) => {console.error("unarchiveApplication default err log", e)}) {
                this.updateApplication(appId, null, 'ACTIVE', callback, callbackError);
        },

        archiveVersion(appId, version,
                        callback = () => {console.log("archiveVersion default success log")},
                        callbackError = (e) => {console.error("archiveVersion default err log", e)}) {
                try {
                        ApiCallUtils.putSecureNoContent('/app/' + appId + '/version/' + version + '/archive', {}, callback, callbackError);
                } catch (e) {
                        console.error(e);
                        callbackError(e);
                }
        },

        unarchiveVersion(appId, version,
                        callback = () => {console.log("unarchiveVersion default success log")},
                        callbackError = (e) => {console.error("unarchiveVersion default err log", e)}) {
                try {
                        ApiCallUtils.putSecureNoContent('/app/' + appId + '/version/' + version + '/unarchive', {}, callback, callbackError);
                } catch (e) {
                        console.error(e);
                        callbackError(e);
                }
        },

        createApplication(appLabel,
                        callback = () => {console.log("createApplication default success log")},
                        callbackError = (e) => {console.error("createApplication default err log", e)}) {
                try {
                appLabel!=null?
                                ApiCallUtils.putSecureNoContent('/connector/app',
                                        {
                                                "appId":  appLabel,
                                                "appLabel":  appLabel,
                                                "productOwner":  ""
                                        },
                                        () => {
                                                console.log("success createApplication callback");
                                                callback();
                                        },
                                        (e) => {
                                                console.log("error createApplication callback", e);
                                                callbackError(e);
                                        }
                                ):null
                } catch (e) {
                        console.error(e);
                        callbackError(e);
                }
        },

        addVersion(appId, numVersion,
                        callback = () => {console.log("addVersion default success log")},
                        callbackError = (e) => {console.error("addVersion default err log", e)}) {
                try {
                appId!=null&&numVersion!=null?
                                ApiCallUtils.putSecureNoContent('/connector/app/' + appId + '/version/' + numVersion,
                                        {
                                                "filename":  "",
                                                "contentAsBase64":  ""
                                        },
                                        () => {
                                                console.log("success addVersion callback");
                                                callback();
                                        },
                                        (e) => {
                                                console.log("error addVersion callback", e);
                                                callbackError(e);
                                        }
                                ):null
                } catch (e) {
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
                        callbackError(e);
                }
        },

        removeAllProperties(appId, version,
                        callback = (data) => {console.log("removeAllProperties default success log"), data},
                        callbackError = (e) => {console.error("removeAllProperties default err log", e)}) {
                try {
                appId!=null&&version!=null?
                                ApiCallUtils.deleteSecureNoContent('/app/' + appId + '/version/' + version + '/properties',
                                        {
                                                "appId": appId,
                                                "numVersion": version
                                        },
                                        () => {
                                                console.log("success removeAllProperties callback");
                                                callback();
                                        },
                                        (e) => {
                                                console.log("error removeAllProperties callback", e);
                                                callbackError(e);
                                        }
                                ):null
                } catch (e) {
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
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
                        console.error(e);
                        callbackError(e);
                }
        },

        addSnapshotVersion(appId,
                        callback = (data) => {console.log("addSnapshotVersion default success log"), data},
                        callbackError = (e) => {console.error("addSnapshotVersion default err log", e)}) {
                try {
                appId!=null?
                                ApiCallUtils.postSecureNoContent('/app/' + appId + '/snapshot',
                                        {},
                                        () => {
                                                console.log("success addSnapshotVersion callback");
                                                callback();
                                        },
                                        (e) => {
                                                console.log("error addSnapshotVersion callback", e);
                                                callbackError(e);
                                        }
                                ):null
                } catch (e) {
                        console.error(e);
                        callbackError(e);
                }
        },
	
	
	
	
	
	
	
	
	
	
	
	
        searchValues(value, callback = (data) => {console.log("searchValues default success log"), data}, callbackError = (e) => {console.error("searchValues default err log", e)}) {
                try {
                        ApiCallUtils.getSecure('/search?value=' + encodeURIComponent(value),
                                (data) => {
                                        console.log("success searchValues callback", data);
                                        callback(data);
                                },
                                (e) => {
                                        console.log("error searchValues callback", e);
                                        callbackError(e);
                                }
                        );
                } catch (e) {
                        console.error(e);
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
                        console.error(e);
                        callbackError(e);
		}
	},
	
}
