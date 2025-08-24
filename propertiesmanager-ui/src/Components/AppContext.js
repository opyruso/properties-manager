export default {
	app: undefined,
	lastErrorMessage: ''
}

/*

	app content exemple (location : config/config.json) :

	{
		"API_ROOT_URL": "http://localhost:10000/pm-api",
		"lang": "fr-FR",
		"env": [
				"dev",
				"tst",
				"rec",
				"prepro",
				"pro"
			],
		"keycloak_init_options" : {
			"onLoad": "check-sso",
			"silentCheckSsoRedirectUri": "/silent-check-sso.html",
			"checkLoginIframe": false
		}
	}
*/
