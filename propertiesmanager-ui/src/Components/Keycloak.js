import Keycloak from 'keycloak-js';

const instance = new Keycloak('/config/keycloak.json');

export default {
	instance: () => {return instance},
	authenticated: () => {return instance?.authenticated},
	eventLogger : (event, error) => {
		console.log('onKeycloakEvent (authenticated : ' + instance?.authenticated + ')', event, error);
	},
	
	tokenLogger : (tokens) => {
	  console.log('onKeycloakTokens', tokens)
	},
	
	getUserGroups() {
		if (instance === undefined
			|| instance.tokenParsed === undefined
			|| instance.tokenParsed.propertiesmanager_rights === undefined) return false;
		let result = instance.tokenParsed.propertiesmanager_group;
		return result;
	},
	
	securityCheck(appId, env, level, forceRights = undefined) {
		if (instance === undefined
			|| instance.tokenParsed === undefined
			|| instance.tokenParsed.propertiesmanager_rights === undefined) return false;
		console.log("token claims : " + JSON.stringify(instance.tokenParsed.propertiesmanager_rights));
		let result = false;
		let rightsToken = instance.tokenParsed.propertiesmanager_rights;
		if (forceRights !== undefined) rightsToken = forceRights;
		if (rightsToken === undefined || rightsToken == null) rightsToken = {"admin":false};
		if (!Array.isArray(rightsToken)) rightsToken = [rightsToken];
		for (let i = 0; i < rightsToken.length; i++) {
			console.log("token details claims[" + (i + 1) + "/" + rightsToken.length + "] : " + JSON.stringify(rightsToken[i]));
			let rights = rightsToken[i];
			if (
				rights.admin
					|| (appId == "all_app" && rights.all_app?.[env]!=undefined && rights.all_app?.[env].includes(level))
					|| (env == "all_env" && rights.app?.[appId]?.["all_env"]!=undefined && rights.app[appId]["all_env"].includes(level))
					|| appId != "all_app" && env != "all_env" && 
						(rights.all_app?.[env]!=undefined && rights.all_app?.[env].includes(level))
						|| (rights.app?.[appId]!=undefined && rights.app[appId]["all_env"]!=undefined && rights.app[appId]["all_env"].includes(level))
						|| (rights.app?.[appId]!=undefined && rights.app[appId][env]!=undefined && rights.app[appId][env].includes(level))
			) {
				result = true;
				break;
			}
		}
		return result;
	},
	
	securityAdminCheck(forceRights = undefined) {
		if (instance === undefined
			|| instance.tokenParsed === undefined
			|| instance.tokenParsed.propertiesmanager_rights === undefined) return false;
//		console.log("token claims : " + JSON.stringify(instance.tokenParsed.propertiesmanager_rights));
		let result = false;
		let rightsToken = instance.tokenParsed.propertiesmanager_rights;
		if (forceRights !== undefined) rightsToken = forceRights;
		if (rightsToken === undefined || rightsToken == null) rightsToken = {"admin":false};
		if (!Array.isArray(rightsToken)) rightsToken = [rightsToken];
		for (let i = 0; i < rightsToken.length; i++) {
//			console.log("token details claims[" + (i + 1) + "/" + rightsToken.length + "] : " + JSON.stringify(rightsToken[i]));
			let rights = rightsToken[i];
			if (
				rights.admin
			) {
				result = true;
				break;
			}
		}
		return result;
	}
}
