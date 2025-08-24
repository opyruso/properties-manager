import React, {useEffect, useState} from 'react';
import { Link } from "react-router-dom";

import { useKeycloakInstance } from '../../../Keycloak';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faShieldHalved } from '@fortawesome/free-solid-svg-icons';



export default function StreamerMenu() {

const { keycloak } = useKeycloakInstance();
	
	const [username, setUsername] = useState();

	if (localStorage.streamer_mod === undefined) {
		localStorage.setItem("streamer_mod", "0");
	}

useEffect(() => {
console.log("Streamer Menu : ", keycloak?.authenticated);
setUsername(keycloak?.authenticated && keycloak.tokenParsed?.email!==undefined?keycloak.tokenParsed?.preferred_username:"");
}, [keycloak?.authenticated, keycloak.tokenParsed?.email, keycloak.tokenParsed?.preferred_username]);
	
	

	
	
	


	
	
	
	/* HANDLERS */
		
	function switchStremerMod() {
		if (parseInt(localStorage.streamer_mod) == 1) {
			if (!confirm("Disable Security Mod ?")) return;
		}
		console.log("switchStremerMod : ", localStorage.streamer_mod);
		localStorage.setItem("streamer_mod", (parseInt(localStorage.streamer_mod) + 1) % 2);
	}
	
	
	
	
	
	
	
	


	return (
		(parseInt(localStorage.streamer_mod) == 1) ? (
			<React.Fragment>
				<a href="#" id="streamerModSwitcher" class="streamer_link streamer_link_on" style={{color:'#FF0000'}} onClick={switchStremerMod}><FontAwesomeIcon icon={faShieldHalved} /> Disable Security Mod</a>
			</React.Fragment>
		) : (
			<React.Fragment>
				<a href="#" id="streamerModSwitcher" class="streamer_link streamer_link_off" onClick={switchStremerMod}><FontAwesomeIcon icon={faShieldHalved} /> Enable Security Mod</a>
			</React.Fragment>
		)
	);

}
 