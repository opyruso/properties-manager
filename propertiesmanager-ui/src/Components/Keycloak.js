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

  securityCheck(appId, env, level) {
    if (this.keycloak?.hasRealmRole('admin') || this.keycloak?.hasResourceRole('admin', 'propertiesmanager-app')) {
      return true;
    }
    const role = `env_${env}_${level === 'r' ? 'read' : 'write'}`;
    return this.keycloak?.hasResourceRole(role, 'propertiesmanager-app');
  }

  securityAdminCheck() {
    return (
      this.keycloak?.hasRealmRole('admin') ||
      this.keycloak?.hasResourceRole('admin', 'propertiesmanager-app')
    );
  }
}

export function useKeycloakInstance() {
  const { keycloak } = useReactKeycloak();
  return keycloak;
}

export default new KeycloakService();
