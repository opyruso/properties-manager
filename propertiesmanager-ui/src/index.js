import './Components/kernel/themes/Dark.css'

import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom/client';

import { ReactKeycloakProvider } from '@react-keycloak/web'

import Keycloak from './Components/Keycloak';

import './index.css';
import App from './Components/App';

import './Components/kustom/i18n';
import { HashRouter } from 'react-router-dom';

function Root() {
        const [config, setConfig] = useState();
        const [keycloak, setKeycloak] = useState();

        useEffect(() => {
                Promise.all([
                        fetch('/config/config.json'),
                        fetch('/config/keycloak.json')
                ])
                .then(async ([confRes, kcRes]) => {
                        const data = await confRes.json();
                        if (data.keycloak_init_options?.silentCheckSsoRedirectUri?.startsWith('/')) {
                                data.keycloak_init_options.silentCheckSsoRedirectUri =
                                        window.location.origin + data.keycloak_init_options.silentCheckSsoRedirectUri;
                        }
                        setConfig(data);

                        const kcConfig = await kcRes.json();
                        if (kcConfig['auth-server-url'] && !kcConfig.url) {
                                kcConfig.url = kcConfig['auth-server-url'];
                                delete kcConfig['auth-server-url'];
                        }
                        if (kcConfig.resource && !kcConfig.clientId) {
                                kcConfig.clientId = kcConfig.resource;
                                delete kcConfig.resource;
                        }
                        if (kcConfig['public-client'] !== undefined && kcConfig.publicClient === undefined) {
                                kcConfig.publicClient = kcConfig['public-client'];
                                delete kcConfig['public-client'];
                        }
                        setKeycloak(Keycloak.createInstance(kcConfig));
                })
                .catch(e => {
                        console.error('Response config error : ', e);
                });
        }, []);

        if (!config || !keycloak) return null;

        return (
                <ReactKeycloakProvider
                        authClient={keycloak}
                        onEvent={Keycloak.eventLogger}
                        onTokens={Keycloak.tokenLogger}
                        initOptions={config.keycloak_init_options}
                        key={JSON.stringify(config.keycloak_init_options)}
                >
                        <React.StrictMode>
                                <HashRouter>
                                        <App />
                                </HashRouter>
                        </React.StrictMode>
                </ReactKeycloakProvider>
        );
}

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(<Root />);


var seq = ['ArrowUp',
				'ArrowUp',
				'ArrowDown',
				'ArrowDown',
				'ArrowLeft',
				'ArrowRight',
				'ArrowLeft',
				'ArrowRight',
				'b',
				'a',
				'Enter']
var input = []
var currentInterval = undefined
var colors = []
window.addEventListener("keydown", function(e) {
    input.push(e.key);

    for (var i = 0; i < seq.length; i++) {
        if (input[i] !== seq[i] && input[i] !== undefined) {
            input = []
        }
    }

    if (JSON.stringify(input) === JSON.stringify(seq)) {
		changeColorStyleRandlomly();
		if (currentInterval !== undefined) clearInterval(currentInterval);
		currentInterval = setInterval(changeColorStyleRandlomly, 100);
		input = []
    }
})
function toColor(r, g, b) {
    return "rgb(" + [r, g, b].join(",") + ")";
}
function getColor(i) {
	if (colors[i] === undefined) {
		colors[i] = [
			(Math.floor(Math.random() * 256)),
			(Math.floor(Math.random() * 256)),
			(Math.floor(Math.random() * 256)),
			(Math.floor(Math.random() * 10)) - 5,
			(Math.floor(Math.random() * 10)) - 5,
			(Math.floor(Math.random() * 10)) - 5
		];
	} else {
		colors[i][0] = (colors[i][0] + colors[i][3]) % 256;
		if (colors[i][0] <= 10) colors[i][3] = -1 * colors[i][3]
		else if (colors[i][0] >= 246) colors[i][3] = -1 * colors[i][3]
		
		colors[i][1] = (colors[i][1] + colors[i][4]) % 256;
		if (colors[i][1] <= 10) colors[i][4] = -1 * colors[i][4]
		else if (colors[i][1] >= 246) colors[i][4] = -1 * colors[i][4]
		
		colors[i][2] = (colors[i][2] + colors[i][5]) % 256;
		if (colors[i][2] <= 10) colors[i][5] = -1 * colors[i][5]
		else if (colors[i][2] >= 246) colors[i][5] = -1 * colors[i][5]
	}
	return toColor(colors[i][0], colors[i][1], colors[i][2]);
}
function changeColorStyleRandlomly() {
	let i = 0;
	const styles = {
	    '--background-title': getColor(i++),
	    '--background': getColor(i++),
	    '--font': getColor(i++),
	    
	    '--background-title-hover': getColor(i++),
	    '--background-hover': getColor(i++),
	    '--font-hover': getColor(i++),
	    
	    '--background-title-selected': getColor(i++),
	    '--background-selected': getColor(i++),
	    '--font-selected': getColor(i++),
	    
	    '--background-title-ok': getColor(i++),
	    '--background-ok': getColor(i++),
	    '--font-ok': getColor(i++),
	    
	    '--background-title-ko': getColor(i++),
	    '--background-ko': getColor(i++),
	    '--font-ko': getColor(i++),
	}
	for (var key in styles) { 
		document.documentElement.style.setProperty(key, styles[key]);
	}
}
