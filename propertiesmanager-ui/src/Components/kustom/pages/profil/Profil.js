import './Profil.css';

import React, { useEffect, useState } from 'react';
import { useKeycloakInstance } from '../../../Keycloak';
import { useTranslation } from 'react-i18next';

export default function Profil() {
    const { t } = useTranslation();
    const { keycloak } = useKeycloakInstance();
    const [roles, setRoles] = useState([]);

    useEffect(() => {
        let tmp = [];
        if (keycloak?.tokenParsed?.realm_access?.roles) {
            tmp = tmp.concat(keycloak.tokenParsed.realm_access.roles);
        }
        if (keycloak?.tokenParsed?.resource_access?.['propertiesmanager-app']?.roles) {
            tmp = tmp.concat(keycloak.tokenParsed.resource_access['propertiesmanager-app'].roles);
        }
        setRoles(tmp);
    }, [keycloak?.tokenParsed]);

    return (
        <div className="profil">
            <h1>{t('profil.title')}</h1>
            <h2>{t('profil.roles')}</h2>
            <ul>
                {roles.map(r => <li key={r}>{r}</li>)}
            </ul>
        </div>
    );
}
