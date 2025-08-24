import Keycloak from 'keycloak-js';
import { useKeycloak as useReactKeycloak } from '@react-keycloak/web';

class KeycloakService {
  constructor() {
    this.keycloak = undefined;
  }

  createInstance(config) {
    this.keycloak = new Keycloak(config);
    return this.keycloak;
  }

  instance() {
    return this.keycloak;
  }

  authenticated() {
    return !!this.keycloak?.authenticated;
  }

  eventLogger(event, error) {
    console.log(`onKeycloakEvent (authenticated : ${this.authenticated()})`, event, error);
  }

  tokenLogger(tokens) {
    console.log('onKeycloakTokens', tokens);
  }

  getUserGroups() {
    const groups = this.keycloak?.tokenParsed?.propertiesmanager_group;
    return groups || false;
  }

  securityCheck(appId, env, level, forceRights = undefined) {
    const rightsSource = forceRights ?? this.keycloak?.tokenParsed?.propertiesmanager_rights;
    if (!rightsSource) return false;

    const rightsTokens = Array.isArray(rightsSource) ? rightsSource : [rightsSource];
    for (const rights of rightsTokens) {
      if (
        rights.admin ||
        (appId === 'all_app' && rights.all_app?.[env]?.includes(level)) ||
        (env === 'all_env' && rights.app?.[appId]?.['all_env']?.includes(level)) ||
        (rights.all_app?.[env]?.includes(level)) ||
        (rights.app?.[appId]?.['all_env']?.includes(level)) ||
        (rights.app?.[appId]?.[env]?.includes(level))
      ) {
        return true;
      }
    }
    return false;
  }

  securityAdminCheck(forceRights = undefined) {
    const rightsSource = forceRights ?? this.keycloak?.tokenParsed?.propertiesmanager_rights;
    if (!rightsSource) return false;

    const rightsTokens = Array.isArray(rightsSource) ? rightsSource : [rightsSource];
    return rightsTokens.some((r) => r.admin);
  }
}

export function useKeycloakInstance() {
  const { keycloak } = useReactKeycloak();
  return keycloak;
}

export default new KeycloakService();
