import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import { useKeycloak } from '@react-keycloak/web';

import Error, { ForbiddenError, NotFoundError } from "../kernel/error/Error";
import Copyright from "../kernel/copyright/Copyright";

import Profile from "./pages/user/Profile";
import AppList from './pages/applist/AppList';
import AppDetails from "./pages/appdetails/AppDetails";
import ConfigHelper from "./pages/confighelper/ConfigHelper";
import GlobalVariables from "./pages/globalvariables/GlobalVariables";
import AppContext from "../AppContext";

export default function AppRouter() {

	const { keycloak, } = useKeycloak();

	return (
			<Routes>
				<Route exact path="/" element={globalCondition(security(<Navigate to="/apps" />))} />
				
				<Route exact path="/apps" element={globalCondition(security(<AppList />))} />
				<Route exact path="/app/:appId/version/:version" element={globalCondition(security(<AppDetails />))} />
				<Route exact path="/config-helper" element={globalCondition(security(<ConfigHelper />))} />
				<Route exact path="/global-variables" element={globalCondition(security(<GlobalVariables />))} />
				
				<Route exact path="/user/profil" element={globalCondition(security(<Profile />))} />

				<Route exact path="/copyright" element={globalCondition(<Copyright />)} />
				
				<Route path="/error" element={<Error errNum="500" />} />
				<Route path="*" element={globalCondition(<NotFoundError errNum="404" />)} />
			</Routes>
	);
	
	function globalCondition(action) {
		return AppContext.app!==undefined?action:<Error errNum="500" />;
	}
	
	function security(action) {
		return keycloak.authenticated?action:<ForbiddenError errNum="403" />;
	}
}
