import './Profile.css';

import React, { useState, useEffect, useContext } from 'react';

import { useKeycloak } from '@react-keycloak/web';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil, faEye, faCheck, faXmark } from '@fortawesome/free-solid-svg-icons';

import AppContext from '../../../AppContext';
import Keycloak from '../../../../Components/Keycloak';

import ApiDefinition from '../../../kustom/api/ApiDefinition';
import { useTranslation } from 'react-i18next';

export default function Profile() {

        const { keycloak, } = useKeycloak();
        const { app } = useContext(AppContext);

	const [envList, setEnvList] = useState();

	const [applicationsFilter, setApplicationsFilter] = useState("");
	const [adminApplicationsFilter, setAdminApplicationsFilter] = useState("");
	
	const [applications, setApplications] = useState();
	const [rightDemands, setRightDemands] = useState([]);
	const [filteredApplications, setFilteredApplications] = useState();

	const [adminRightDemands, setAdminRightDemands] = useState([]);

	const [users, setUsers] = useState([]);
	const [userFilter, setUserFilter] = useState();
	const [selectedUser, setSelectedUser] = useState();
	const [selectedUserData, setSelectedUserData] = useState();
	
	



	

	/* HOOKS */
	
        useEffect(() => {
                app!=undefined?
                        setEnvList(app.env)
                        :null;
        }, [app]);
	
	useEffect(() => {
		if (keycloak.authenticated) {
			ApiDefinition.getApplications((data) => {
				setApplications(data);
			});
			ApiDefinition.getRightDemands(keycloak.tokenParsed.sub, (data) => {
				setRightDemands(data);
			});
			if (Keycloak.securityAdminCheck()) {
				ApiDefinition.getAdminRightDemands((data) => {
					setAdminRightDemands(data);
				});
			}
		}
	}, [envList, keycloak.authenticated]);
	
	useEffect(() => {
		if (userFilter === undefined || userFilter.length < 3) {
			setSelectedUser(undefined);
			setSelectedUserData(undefined);
		} else {
			if (keycloak.authenticated) {
				if (Keycloak.securityAdminCheck()) {
					ApiDefinition.getAdminUsers(userFilter, (data) => {
						setUsers(data);
					});
				}
			}
		}
	}, [userFilter]);
	
	useEffect(() => {
		console.log(applicationsFilter, applications);
		if (applications == undefined || applications == null
				|| applicationsFilter == undefined || applicationsFilter == null || applicationsFilter.length == 0) {
			setFilteredApplications();
			return;
		} 
		let localFilteredApplications = [... applications];
		applicationsFilter.split(" ").map(v => {
			localFilteredApplications = localFilteredApplications?.filter(
				a => (
					a.appId.toLowerCase().includes(v.toLowerCase())
					|| a.appLabel.toLowerCase().includes(v.toLowerCase())
					|| a.productOwner.toLowerCase().includes(v.toLowerCase())
					|| (v.toLowerCase().startsWith(":") && Keycloak.securityCheck(a.appId, v.toLowerCase().slice(1), "r"))
					|| (v.toLowerCase().startsWith(":") && Keycloak.securityCheck(a.appId, v.toLowerCase().slice(1), "w"))
				)
			)	
		})
		setFilteredApplications(localFilteredApplications
			.sort((a, b) => {return a.appId.localeCompare(b.appId)})
			.slice(0, 15));
	}, [applications, applicationsFilter]);
	
	useEffect(() => {
		if (applications == undefined || applications == null
				|| adminApplicationsFilter == undefined || adminApplicationsFilter == null || adminApplicationsFilter.length == 0) {
			setFilteredApplications();
			return;
		} 
		let localFilteredApplications = [... applications];
		adminApplicationsFilter.split(" ").map(v => {
			localFilteredApplications = localFilteredApplications?.filter(
				a => (
					a.appId.toLowerCase().includes(v.toLowerCase())
					|| a.appLabel.toLowerCase().includes(v.toLowerCase())
					|| a.productOwner.toLowerCase().includes(v.toLowerCase())
					|| (v.toLowerCase().startsWith(":") && Keycloak.securityCheck(a.appId, v.toLowerCase().slice(1), "r", selectedUserData?.rights))
					|| (v.toLowerCase().startsWith(":") && Keycloak.securityCheck(a.appId, v.toLowerCase().slice(1), "w", selectedUserData?.rights))
				)
			)	
		})
		setFilteredApplications(localFilteredApplications
			.sort((a, b) => {return a.appId.localeCompare(b.appId)})
			.slice(0, 15));
	}, [applications, adminApplicationsFilter]);
	

	
	
	


	
	
	
	/* HANDLERS */
		
	function updateApplicationsFilterCallback(e) {
		setApplicationsFilter(e.target.value);
	}
		
	function updateAdminApplicationsFilterCallback(e) {
		setAdminApplicationsFilter(e.target.value);
	}
		
	function updateUserFilterCallback(e) {
		if (e.target.value === undefined || e.target.value.leng < 3) {
			setUserFilter("");
		} else {
			setUserFilter(e.target.value);
			setSelectedUser(undefined);
			setSelectedUserData(undefined);
		}
	}
		
	function selectUserCallback(user) {
		if (!Keycloak.securityAdminCheck()) return;
		console.log("selectUserCallback : ", user.userId);
		setSelectedUser(user.userId);
		setSelectedUserData(user);
	}

		
	function askRightCallback(appId, env, level) {
		if (alreadyInDemandList(appId, env, level)) return;
		if (Keycloak.securityCheck(appId, env, level)) return;

		console.log("askRightCallback : ", appId, env, level);
		ApiDefinition.demandNewRight(keycloak.tokenParsed.sub, appId, env, level,
			(data) => {
				console.log("askNewRight success", data);
				let localRightDemands = [... rightDemands];
				if (data.result === true) {
					console.log("refreshing token");
					keycloak.updateToken(10000000).then(() => {
						ApiDefinition.getApplications((data) => {
							setApplications(data);
						});
					});
				} else if (data.result === false) {
					console.log("changing UI only");
					localRightDemands.push({
						"appId": appId,
						"envId": env,
						"level": level
					});
				}
				setRightDemands(localRightDemands);
			});
	}
		
	function acceptDemandCallback(userId, appId, envId, level) {
		if (!Keycloak.securityAdminCheck()) return;
		console.log("acceptDemandCallback : ", userId, appId, envId, level);
		
		ApiDefinition.acceptRightDemand(userId, appId, envId, level,
			() => {
				console.log("acceptDemandCallback success");
				refreshAdminRightDemands();
				refreshUsers();
			});
	}
		
	function refuseDemandCallback(userId, appId, envId, level) {
		if (!Keycloak.securityAdminCheck()) return;
		console.log("refuseDemandCallback : ", userId, appId, envId, level);
		
		ApiDefinition.refuseRightDemand(userId, appId, envId, level,
			() => {
				console.log("refuseDemandCallback success");
				refreshAdminRightDemands();
				refreshUsers();
			});
	}
		
	function adminToggleRightCallback(appId, envId, level, userId) {
		if (!Keycloak.securityAdminCheck()) return;
		console.log("adminToggleRightCallback : ", userId, appId, envId, level);
		
		ApiDefinition.adminToggleRight(userId, appId, envId, level,
			(data) => {
				console.log("adminToggleRight success", data);
				refreshUsers();
			});
	}

	







	/* UTILS */
	function alreadyInDemandList(appId, envId, level) {
		let result = false;
		for(let i = 0; i < rightDemands?.length; i++) {
			if (rightDemands[i].appId == appId
					&& rightDemands[i].envId == envId
					&& rightDemands[i].level == level) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	function refreshUsers() {
		if (userFilter !== undefined && userFilter !== null && userFilter.length >= 3) ApiDefinition.getAdminUsers(userFilter, (data) => {
			setUsers(data);
			data.map((userData) => {
				if (userData.userId == selectedUserData?.userId) setSelectedUserData(userData);
			});
		});
	}
	
	function refreshAdminRightDemands() {
		ApiDefinition.getAdminRightDemands((data) => {
			setAdminRightDemands(data);
		});
	}
	
	
	
	


	return (
		<div className="profil">
			<ProfilGeneralInformationPanel token={keycloak.tokenParsed} />
			{
				Keycloak.securityAdminCheck()?
				<AdminPanel 
						adminRightDemands={adminRightDemands}
						acceptDemandCallback={acceptDemandCallback}
						refuseDemandCallback={refuseDemandCallback}
						
						updateUserFilterCallback={updateUserFilterCallback}
						users={users}
						userFilter={userFilter}
						selectedUser={selectedUser}
						envList={envList}
						selectedUserData={selectedUserData}
						selectUserCallback={selectUserCallback}
						
						adminToggleRightCallback={adminToggleRightCallback}
						
						updateApplicationsFilterCallback={updateAdminApplicationsFilterCallback} 
						filteredApplications={filteredApplications} />
				:<ProfilApplicationRightsPanel
						updateApplicationsFilterCallback={updateApplicationsFilterCallback} 
						envList={envList} 
						alreadyInDemandList={alreadyInDemandList} 
						askRightCallback={askRightCallback} 
						filteredApplications={filteredApplications} />
			}
		</div>
	);
}

export function ProfilGeneralInformationPanel(props) {
	
  	const { t } = useTranslation();

	return (
		<div className="general-information">
			<div className="profil-title">
				<span className="label">{t('profile.global.username')}</span>
				<span className="value" title={props.token?.sub}>{props.token?.preferred_username}</span>
			</div>
			<div className="profil-data">
				<span className="label">{t('profile.global.email')}</span>
				<span className="value">{ props.token?.email }</span>
			</div>
			<div className="profil-data">
				<span className="label">{t('profile.global.firstName')}</span>
				<span className="value">{ props.token?.given_name }</span>
			</div>
			<div className="profil-data">
				<span className="label">{t('profile.global.lastName')}</span>
				<span className="value">{ props.token?.family_name }</span>
			</div>
		</div>
		)	
}

export function ProfilApplicationRightsPanel(props) {
	
  	const { t } = useTranslation();

	console.log(props.adminUserId);
	return (
		<div className="">
			<div className="profil-title profil-title-rights">
				<input id="appList_searchInput" onChange={props.updateApplicationsFilterCallback} className="search-input" type="text" placeholder={t('profile.application.search.placeholder')}></input>
			</div>
			<div className="profil-tab">
				<ProfilApplicationLineTitle envList={props.envList} alreadyInDemandList={props.alreadyInDemandList} askRightCallback={props.askRightCallback}
						forceUserRights={props.forceUserRights}
						adminUserId={props.adminUserId} />
				{props.filteredApplications?.map((app) => {
					return <ProfilApplicationLine key={"ApplicationLine_" + app.appId} appId={app.appId} envList={props.envList} alreadyInDemandList={props.alreadyInDemandList} askRightCallback={props.askRightCallback}
						forceUserRights={props.forceUserRights}
						adminUserId={props.adminUserId} />
				})}
			</div>
		</div>
	)	
}

export function AdminPanel(props) {
	return (
		<div className="admin-user">
			<AdminDemandsPanel adminRightDemands={props.adminRightDemands}
					acceptDemandCallback={props.acceptDemandCallback}
					refuseDemandCallback={props.refuseDemandCallback} />
			<AdminUserDetailsPanel 
					envList={props.envList}
					updateUserFilterCallback={props.updateUserFilterCallback}
					users={props.users}
					userFilter={props.userFilter}
					selectedUser={props.selectedUser}
					selectedUserData={props.selectedUserData}
					selectUserCallback={props.selectUserCallback}
					
					updateApplicationsFilterCallback={props.updateApplicationsFilterCallback} 
					filteredApplications={props.filteredApplications}
						
					adminToggleRightCallback={props.adminToggleRightCallback} />
		</div>
	)	
}

export function AdminDemandsPanel(props) {
	
  	const { t } = useTranslation();

	return (
		<div className="admin-demands">
			<div className="profil-title profil-title-rights">
				<span className="label">{t('profile.admindemand.title')}</span>
			</div>
			{
				props.adminRightDemands?.length <= 0?<div className="profil-tab">
					<span>{t('profile.admindemand.empty')}</span>
				</div>:null
			}
			{props.adminRightDemands?.map((demand) => {
				return <AdminDemand key={"AdminDemandsPanel" + "_" + demand.userId + "_" + demand.appId + "_" + demand.envId + "_" + demand.level}
						demand={demand}
						acceptDemandCallback={props.acceptDemandCallback}
						refuseDemandCallback={props.refuseDemandCallback}
					 />
			})}
		</div>
	)	
}

export function AdminUserDetailsPanel(props) {
	
  	const { t } = useTranslation();

	return (
		<div className="admin-user-details">
			<div className="search-user">
				<div className="user-list">
					<div className="profil-title profil-title-rights">
						<span className="label">{t('profile.adminuser.filter.title')}</span>
					</div>
					<input className="user-search-input" id="user_searchInput" onChange={props.updateUserFilterCallback} type="text" placeholder={t('profile.adminuser.filter.placeholder')}></input>
				{
					props.userFilter?.length>=3?(props.users?.map((user) => {
						return <span className={user.userId==props.selectedUser?' selected-user':null} key={"userList_" + user.userId} onClick={user.userId!=props.selectedUser?()=>{props.selectUserCallback(user)}:null}>{user.name}</span>
					})):<span>{t('profile.adminuser.filter.minimumadvise')}</span>
				}
					<div className="spacer" />
				</div>
				<div className="user-details">
					{props.selectedUser!==undefined?<ProfilApplicationRightsPanel
						updateApplicationsFilterCallback={props.updateApplicationsFilterCallback} 
						envList={props.envList}
						askRightCallback={props.adminToggleRightCallback} 
						filteredApplications={props.filteredApplications}
						adminUserId={props.selectedUser}
						forceUserRights={props.selectedUserData?.rights} />:null}
				</div>
			</div>
		</div>
	)	
}

export function AdminDemand(props) {
	return (
		<div className="admin-user-demand">
			<span className="label-user-id" title={props.demand.userId}>{props.demand.username}</span>
			<span className="label-app-id">{props.demand.appId}</span>
			<span className="label-env-id">{props.demand.envId}</span>
			<span className="label-level">{props.demand.level}</span>
			<span className="action button-ok"
					onClick={(_e)=>{props.acceptDemandCallback(props.demand.userId, props.demand.appId, props.demand.envId, props.demand.level)}} >
				<FontAwesomeIcon icon={faCheck} className="" />
			</span>
			<span className="action button-ko"
					onClick={(_e)=>{props.refuseDemandCallback(props.demand.userId, props.demand.appId, props.demand.envId, props.demand.level)}} >
				<FontAwesomeIcon icon={faXmark} className="" />
			</span>
		</div>
	)	
}

export function ProfilApplicationLineTitle(props) {
	
  	const { t } = useTranslation();

	return (
		<div className="profil-tab-row">
			<span className="profil-tab-row-title">{t('profile.application.title')}</span>
			{
				props.envList?.map((env) => {
					return <div key={"ApplicationLineTitle_" + env} className="profil-tab-row-title">
							<span>{env}</span>
							<hr />
							<FontAwesomeIcon icon={faEye} className={Keycloak.securityCheck('all_app', env, 'r', props.forceUserRights)?'level-active':'level-inactive'+(props.alreadyInDemandList!==undefined&&props.alreadyInDemandList('all_app', env, 'r', props.forceUserRights)?'-waiting':'')}
								 onClick={(_e)=>{props.askRightCallback('all_app', env, 'r', props.adminUserId)}} />
							<FontAwesomeIcon icon={faPencil} className={Keycloak.securityCheck('all_app', env, 'w', props.forceUserRights)?'level-active':'level-inactive'+(props.alreadyInDemandList!==undefined&&props.alreadyInDemandList('all_app', env, 'w', props.forceUserRights)?'-waiting':'')}
								 onClick={(_e)=>{props.askRightCallback('all_app', env, 'w', props.adminUserId)}} />
						</div>
						
				})
			}
		</div>
		);
}

export function ProfilApplicationLine(props) {
	return (
		<div key={"ApplicationLine_" + props.appId} className="profil-tab-row">
			<span className="profil-tab-row-value">{props.appId}</span>
			{props.envList.map((env) => {
				return (<span key={"ApplicationLine_" + props.appId + "_" + env} className="profil-tab-row-value">
					<FontAwesomeIcon icon={faEye} className={Keycloak.securityCheck(props.appId, "all_env", 'r', props.forceUserRights)||Keycloak.securityCheck(props.appId, env, 'r', props.forceUserRights)?'level-active':'level-inactive'+(props.alreadyInDemandList!==undefined&&props.alreadyInDemandList(props.appId, env, 'r', props.forceUserRights)?'-waiting':'')}
				 		onClick={(_e)=>{props.askRightCallback(props.appId, env, 'r', props.adminUserId)}} />
					<FontAwesomeIcon icon={faPencil} className={Keycloak.securityCheck(props.appId, "all_env", 'w', props.forceUserRights)||Keycloak.securityCheck(props.appId, env, 'w', props.forceUserRights)?'level-active':'level-inactive'+(props.alreadyInDemandList!==undefined&&props.alreadyInDemandList(props.appId, env, 'w', props.forceUserRights)?'-waiting':'')}
				 		onClick={(_e)=>{props.askRightCallback(props.appId, env, 'w', props.adminUserId)}} />
				</span>)
			})}
		</div>
		);
}



