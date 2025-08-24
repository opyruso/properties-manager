import React from 'react';

const AppContext = React.createContext({
        app: undefined,
        setApp: () => {}
});

// Legacy fields to maintain compatibility
AppContext.app = undefined;
AppContext.lastErrorMessage = '';

export default AppContext;
