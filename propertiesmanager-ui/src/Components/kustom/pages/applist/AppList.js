import './AppList.css';

import React, { useState, useEffect, useContext } from 'react';
import { Link } from "react-router-dom";

import AppContext from "../../../AppContext";

import { useKeycloak } from '@react-keycloak/web';

import ApiDefinition from '../../api/ApiDefinition';
import { useTranslation } from 'react-i18next';

export default function AppList() {
	
  	const { t } = useTranslation();

        const { keycloak, } = useKeycloak();
        const { app } = useContext(AppContext);
	
	/* INIT */

	if (localStorage.appList_filterValue === undefined) {
		localStorage.setItem("appList_filterValue", "");
	}






	
	/* VARIABLES */

	const [envList, setEnvList] = useState();
	const [applications, setApplications] = useState();
	const [filteredApplications, setFilteredApplications] = useState();
	
	



	

	/* HOOKS */
	
        useEffect(() => {
                app!=undefined?
                        setEnvList(app.env)
                        :null;
        }, [app]);
	
	useEffect(() => {
		if (keycloak.authenticated && envList !== undefined) {
			refreshFilteredData();
			ApiDefinition.getApplications((data) => { setApplications(data); });
		}
	}, [envList, keycloak.authenticated]);
	
	useEffect(() => {
		document.getElementById('appList_searchInput').value = localStorage.appList_filterValue;
		refreshFilteredData();
	}, [applications]);
	
	
	


	
	
	
	/* HANDLERS */
		
	function updateFilteredApplicationsCallback(e) {
		localStorage.setItem("appList_filterValue", e.target.value);
		refreshFilteredData();
	}

	







	/* UTILS */

	function refreshFilteredData() {
		if (applications != undefined) {
			console.log("Filtering... (" + applications.length + " applications)");
			setFilteredApplications(applications?.filter(a => {
					if (localStorage.appList_filterValue.trim() === '') return true;
					let result = false;
					localStorage.appList_filterValue.split(' ').map(b => {
						if (!result && b.trim() != '') result = a.appLabel.toLowerCase().includes(b.toLowerCase())
									|| a.productOwner.toLowerCase().includes(b.toLowerCase());
					});
					return result;
				}).slice(0, 50))
		} else {
			console.log("Applications list not ready");
		}
	}
	
	
	
	
	
	

	return (
		<div className="apps">
			<h1>{t('applist.title')}</h1>
			<input id="appList_searchInput" onChange={updateFilteredApplicationsCallback} className="search-input" type="text" placeholder={t('applist.search.placeholder')}></input>
			<ApplicationLineTitle envList={envList} />
			<div className="applicationsPlaceholder">{
				filteredApplications === undefined || filteredApplications?.length <= 0? 
							<span className="no-data">{t('applist.noapplication')}</span>
						 : filteredApplications?.map(a => <ApplicationLine key={a.appId} envList={envList} application={a} />)
			}</div>
		</div>
	);
}

function ApplicationLineTitle(props) {
	
  	const { t } = useTranslation();
  	
	return (
		<div className="search-result-title" key="-1" >
			<span className="title">{t('applist.application.name')}</span>
			<span className="productOwner">{t('applist.application.productowner')}</span>
			{
				props.envList?.map((env) => {
						return <span key={env + "_title"} className="envColumn">{env.toUpperCase()}</span>
					})
			}
		</div>
	);
}

function ApplicationLine(props) {
	return (
		<div className="search-result" to={"/app/" + props.application?.appId}>
			<span className="title">{props.application?.appLabel}</span>
			<span className="productOwner">{props.application?.productOwner}</span>
			{
				props.envList?.map((env) => {
						return <ApplicationLineEnvColumn key={env} appId={props.application?.appId} version={props.application?.versions?.[env]} date={props.application?.lastReleaseDates?.[env]} />
					})
			}
		</div>
	);
}

function ApplicationLineEnvColumn(props) {
	
  	const { t } = useTranslation();
  	
	return (
		props.version!=null?(
			<Link className="envColumn" to={"/app/" + props.appId + "/version/" + props.version}>
				<span title={props.date!=null?formatDate(props.date):t('applist.application.unknowlastdeliverydate')}>{props.version}</span>
			</Link>
		):(
			<span className="envColumn" title={t('applist.application.unknowlastdeliverydate')}></span>
		)
	)
}

function formatDate(t) {
	return new Date(t).toLocaleString();
}
