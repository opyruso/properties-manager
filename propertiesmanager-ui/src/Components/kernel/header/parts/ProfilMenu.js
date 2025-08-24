import React, { useEffect, useState } from 'react';
import { Link } from "react-router-dom";

import { useKeycloakInstance } from '../../../Keycloak';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faRightToBracket, faRightFromBracket } from '@fortawesome/free-solid-svg-icons';

export default function ProfilMenu() {

const { keycloak } = useKeycloakInstance();

        const [username, setUsername] = useState('');

        useEffect(() => {
                console.log("Profil Menu : ", keycloak?.authenticated);
                if (keycloak?.authenticated && keycloak.tokenParsed?.email !== undefined) {
                        setUsername(keycloak.tokenParsed?.preferred_username);
                } else {
                        setUsername('');
                }
        }, [keycloak?.authenticated, keycloak?.tokenParsed?.email, keycloak?.tokenParsed?.preferred_username]);

        return (
                (keycloak?.authenticated) ? (
                        <React.Fragment>
                                <Link to="/user/profil">{username}</Link>
                                <Link to="#" onClick={() => keycloak?.logout?.()}><FontAwesomeIcon className="text-icon" icon={faRightFromBracket} /></Link>
                        </React.Fragment>
                ) : (
                        <React.Fragment>
                                <Link to="#" onClick={() => keycloak?.login?.()}><FontAwesomeIcon className="text-icon" icon={faRightToBracket} /></Link>
                        </React.Fragment>
                )
        );

}
 