import './AppList.css';

import React, { useState, useEffect, useContext } from 'react';
import { Link, useNavigate } from "react-router-dom";

import AppContext from "../../../AppContext";

import Keycloak, { useKeycloakInstance } from '../../../Keycloak';

import ApiDefinition from '../../api/ApiDefinition';
import { useTranslation } from 'react-i18next';
import { subscribe, unsubscribe, publish } from '../../../AppStaticData';

export default function AppList() {
	
  	const { t } = useTranslation();

const { keycloak } = useKeycloakInstance();
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
if (keycloak?.authenticated && envList !== undefined) {
                        refreshFilteredData();
                        ApiDefinition.getApplications((data) => { setApplications(data); });
                }
}, [envList, keycloak?.authenticated]);

useEffect(() => {
        const listener = () => {
                ApiDefinition.getApplications((data) => { setApplications(data); });
        };
        subscribe('archivesChangeEvent', listener);
        return () => unsubscribe('archivesChangeEvent', listener);
}, []);
	
	useEffect(() => {
		document.getElementById('appList_searchInput').value = localStorage.appList_filterValue;
		refreshFilteredData();
	}, [applications]);
	
	
	


	
	
	
	/* HANDLERS */
		
        function updateFilteredApplicationsCallback(e) {
                localStorage.setItem("appList_filterValue", e.target.value);
                refreshFilteredData();
        }

        function addApplicationCallback() {
                const name = prompt(t('applist.add.prompt'));
                if (name !== null && name.trim() !== '') {
                        ApiDefinition.createApplication(name.trim(), () => {
                                ApiDefinition.getApplications((data) => { setApplications(data); });
                        });
                }
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
                        {Keycloak.securityAdminCheck() ? <button onClick={addApplicationCallback}>{t('applist.add')}</button> : null}
                        <table>
                                <thead>
                                        <ApplicationLineTitle envList={envList} />
                                </thead>
                                <tbody>
                                        {
                                                filteredApplications === undefined || filteredApplications?.length <= 0 ?
                                                        <tr className="app-line">
                                                                <td className="no-data" colSpan={(envList?.length || 0) + 2}>{t('applist.noapplication')}</td>
                                                        </tr>
                                                        : filteredApplications?.map(a => <ApplicationLine key={a.appId} envList={envList} application={a} />)
                                        }
                                </tbody>
                        </table>
                </div>
        );
}

function ApplicationLineTitle(props) {
	
  	const { t } = useTranslation();
  	
        return (
                <tr className="app-line-title">
                        <th className="title">{t('applist.application.name')}</th>
                        <th className="productOwner">{t('applist.application.productowner')}</th>
                        {
                                props.envList?.map((env) => {
                                        return <th key={env + "_title"} className="envColumn">{env.toUpperCase()}</th>;
                                })
                        }
                        {Keycloak.securityAdminCheck() ? <th className="archive"></th> : null}
                </tr>
        );
}

function ApplicationLine(props) {
        const { t } = useTranslation();
        const navigate = useNavigate();

        function goToLatest(e) {
                e.preventDefault();
                ApiDefinition.getApplicationVersions(
                        props.application?.appId,
                        (data) => {
                                const latest = getLatestVersion(data);
                                navigate("/app/" + props.application?.appId + "/version/" + latest);
                        },
                        () => navigate("/app/" + props.application?.appId + "/version/snapshot")
                );
        }

        return (
                <tr className="app-line">
                        <td className="title"><Link to="#" onClick={goToLatest}>{props.application?.appLabel}</Link></td>
                        <td className="productOwner">{props.application?.productOwner}</td>
                        {
                                props.envList?.map((env) => {
                                        return <ApplicationLineEnvColumn key={env} env={env} appId={props.application?.appId} version={props.application?.versions?.[env]} date={props.application?.lastReleaseDates?.[env]} />;
                                })
                        }
                        {
                                Keycloak.securityAdminCheck() ? <td className="archive"><button onClick={() => {
                                        props.application?.status === 'ARCHIVED'
                                                ? ApiDefinition.unarchiveApplication(props.application?.appId, () => publish('archivesChangeEvent'))
                                                : ApiDefinition.archiveApplication(props.application?.appId, () => publish('archivesChangeEvent'));
                                }}>{props.application?.status === 'ARCHIVED' ? t('unarchive') : t('archive')}</button></td> : null
                        }
                </tr>
        );
}

function ApplicationLineEnvColumn(props) {

        const { t } = useTranslation();

        function openEnv() {
                localStorage.setItem("appDetails_env", JSON.stringify({ [props.env]: true }));
        }

        return (
                props.version != null ? (
                        <td className="envColumn">
                                <Link to={"/app/" + props.appId + "/version/" + props.version} onClick={openEnv} title={props.date != null ? formatDate(props.date) : t('applist.application.unknowlastdeliverydate')}>
                                        {props.version}
                                </Link>
                        </td>
                ) : (
                        <td className="envColumn" title={t('applist.application.unknowlastdeliverydate')}></td>
                )
        );
}

function formatDate(t) {
        return new Date(t).toLocaleString();
}

function compareVersions(a, b) {
        const pa = a.split('.').map(Number);
        const pb = b.split('.').map(Number);
        for (let i = 0; i < Math.max(pa.length, pb.length); i++) {
                const diff = (pa[i] || 0) - (pb[i] || 0);
                if (diff !== 0) return diff;
        }
        return 0;
}

function getLatestVersion(versions) {
        if (!versions || versions.length === 0) return 'snapshot';
        const filtered = versions.filter(v => v && v.toLowerCase() !== 'snapshot').sort(compareVersions);
        if (filtered.length > 0) return filtered[filtered.length - 1];
        return versions.includes('snapshot') ? 'snapshot' : versions[versions.length - 1];
}
