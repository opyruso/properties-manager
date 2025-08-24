import AppContext from '../../AppContext';
import { publish } from '../../AppStaticData';
import Keycloak from '../../Keycloak';

export default {
	
	getSecureNoContent(url, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'GET',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				}
			})
			.then(() => {
				AppContext.lastErrorMessage = '';
				console.log("Response getSecure success (no content)");
				callback();
				publish('endLoadingEvent');
				return;
			})
			.catch((e) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response getSecure error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	},
	
	getSecure(url, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'GET',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				}
			})
			.then(res => res.json())
			.then((data) => {
				AppContext.lastErrorMessage = '';
				console.log("Response getSecure success : ", data);
				callback(data);
				publish('endLoadingEvent');
				return data;
			})
			.catch((e) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response getSecure error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	},
	
	putSecureNoContent(url, request, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'PUT',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				},
				body: JSON.stringify(request)
			})
			.then(() => {
				AppContext.lastErrorMessage = '';
				console.log("Response putSecure success (no content)", );
				callback();
				publish('endLoadingEvent');
				return;
			})
			.catch((e = null) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response putSecure error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	},
	
	putSecure(url, request, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'PUT',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				},
				body: JSON.stringify(request)
			})
			.then(res => res.json())
			.then((data) => {
				AppContext.lastErrorMessage = '';
				console.log("Response putSecure success : ", data);
				callback(data);
				publish('endLoadingEvent');
				return data;
			})
			.catch((e = null) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response putSecure error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	},
	
	postSecureNoContent(url, request, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'POST',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				},
				body: JSON.stringify(request)
			})
			.then(() => {
				AppContext.lastErrorMessage = '';
				console.log("Response putSecure success (no content)", );
				callback();
				publish('endLoadingEvent');
				return;
			})
			.catch((e = null) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response putSecure error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	},
	
	postSecure(url, request, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'POST',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				},
				body: JSON.stringify(request)
			})
			.then(res => res.json())
			.then((data) => {
				AppContext.lastErrorMessage = '';
				console.log("Response postSecure success : ", data);
				callback(data);
				publish('endLoadingEvent');
				return data;
			})
			.catch((e = null) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response postSecure error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	},
	
	deleteSecureNoContent(url, request, callback, callbackError) {
		publish('startLoadingEvent');
		fetch(AppContext.app.API_ROOT_URL + url, {
				method: 'DELETE',
	            headers: {
	                'authorization': 'Bearer ' + Keycloak.instance().token,
	                'Content-Type': 'application/json',
	                'Accept': 'application/json'
				},
				body: JSON.stringify(request)
			})
			.then(() => {
				AppContext.lastErrorMessage = '';
				console.log("Response deleteSecureNoContent success (no content)", );
				callback();
				publish('endLoadingEvent');
				return;
			})
			.catch((e = null) => {
				AppContext.lastErrorMessage = 'error.api';
				console.error("Response deleteSecureNoContent error : ", e);
				callbackError(e);
				publish('endLoadingEvent');
				return e;
			});
	}
	
}