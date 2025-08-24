import './AppDetails.css';

import React, { useReducer, useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";

import Keycloak from '../../../Keycloak';
import AppContext from '../../../AppContext';

import { useKeycloak } from '@react-keycloak/web';

import ApiDefinition from '../../api/ApiDefinition';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil, faMinus, faPlus, faCheck, faXmark, faWarning, faLock, faTrash } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'react-i18next';


export default function AppDetails() {
	
  	const { t } = useTranslation();

	const { keycloak, } = useKeycloak();
	
	/* INIT */

	const [_, _forceUpdate] = useReducer((x) => x + 1, 0);
	
	/* VARIABLES */
	
	const { appId, version } = useParams();
	const navigate = useNavigate();
	const [envList, setEnvList] = useState();

	const [currentVersion, setCurrentVersion] = useState();
	const [copyVersion, setCopyVersion] = useState();

	const [localStorageEnvKey, setLocalStorageEnvKey] = useState();
	const [localStorageFilterKey, setLocalStorageFilterKey] = useState();

	const [currentEnvVisibility, setCurrentEnvVisibility] = useState();
	const [currentFilter, setCurrentFilter] = useState();

	const [filteredProperties, setFilteredProperties] = useState();
	const [accessibleEnvList, setAccessibleEnvList] = useState();
	const [isEditable, setIsEditable] = useState();
	const [userCanEditEnv, setUserCanEditEnv] = useState();
	const [userCanEditAllEnv, setUserCanEditAllEnv] = useState();
	
	const [applicationVersions, setApplicationVersions] = useState();
	const [applicationDetails, setApplicationDetails] = useState();
	
	const [newProductOwner, setNewProductOwner] = useState();
	
	
	
	
	

	/* HOOKS */
	
	useEffect(() => {
		AppContext.app!=undefined?
			setEnvList(AppContext.app.env)
			:null;
	}, [AppContext.app]);
	
	useEffect(() => {
			console.log("keycloak and/or envList change", keycloak, envList);
		if (envList != undefined && keycloak != undefined) {
			let localFilteredEnvList = [];
			envList.map((env) => {
				if (Keycloak.securityCheck(appId, env, 'r')) {
					localFilteredEnvList.push(env);
					if (applicationDetails?.propertiesValue[env] == null) {
						switchEnvValue(env, false);
					}
				} else {
					switchEnvValue(env, false);
				}
				if (applicationDetails?.propertiesValue[env] == null) {
					switchEnvValue(env, false);
				}
			});

			setAccessibleEnvList(localFilteredEnvList);
			setCurrentVersion(version);
		}
	}, [keycloak, envList]);
	
	useEffect(() => {
		console.log("currentVersion change", currentVersion);
		if (currentVersion != undefined) {
			setLocalStorageEnvKey("appDetails_env");
			setLocalStorageFilterKey("appDetails_filter_" + appId);
			setIsEditable([]);
			ApiDefinition.getApplicationDetails(appId, version, (data) => {
				setApplicationDetails(data);
				setApplicationVersions(data.existingVersions);
			});
		}
	}, [currentVersion]);
	
	useEffect(() => {
		console.log("applicationDetails change : ", applicationDetails);
		if (applicationDetails !== undefined) {
		}
	}, [applicationDetails]);
	
	useEffect(() => {
		console.log("localStorageEnvKey change : ", localStorageEnvKey);
		if (localStorageEnvKey != undefined) {
			localStorage[localStorageEnvKey]===undefined?localStorage.setItem(localStorageEnvKey, "{}"):null;
			setCurrentEnvVisibility({... JSON.parse(localStorage[localStorageEnvKey])});
		}
	}, [localStorageEnvKey]);
	
	useEffect(() => {
		console.log("localStorageFilterKey change : ", localStorageFilterKey);
		if (localStorageFilterKey != undefined) {
			localStorage[localStorageFilterKey]===undefined?localStorage.setItem(localStorageFilterKey, ""):null;
			setCurrentFilter(localStorage[localStorageFilterKey]);
		}
	}, [localStorageFilterKey]);
	
	useEffect(() => {
		console.log("currentEnvVisibility change : ", JSON.stringify(currentEnvVisibility));
		if (currentEnvVisibility != undefined) {
			localStorage.setItem(localStorageEnvKey, JSON.stringify(currentEnvVisibility));
			let localUseruserCanEditEnv = {};
			let localUserCanEditAllEnv = true;
			envList.map((env) => {
				localUseruserCanEditEnv[env] = Keycloak.securityCheck(appId, env, 'w');
				if (!localUseruserCanEditEnv[env]) localUserCanEditAllEnv = false;
			});
			setUserCanEditEnv(localUseruserCanEditEnv);
			setUserCanEditAllEnv(localUserCanEditAllEnv);
			console.log("userCanEditEnv change : ", JSON.stringify(localUseruserCanEditEnv));
		}
	}, [JSON.stringify(currentEnvVisibility)]);
	
	useEffect(() => {
		console.log("currentFilter change : ", currentFilter);
		if (currentFilter != undefined) {
			localStorage.setItem(localStorageFilterKey, currentFilter);
			console.log("Filtering... (" + applicationDetails?.properties?.length + " applications)");
			setFilteredProperties(applicationDetails?.properties?.filter(a => { 
						let result = a.propertyKey.toLowerCase().includes(currentFilter.toLowerCase())
										|| a.filename.toLowerCase().includes(currentFilter.toLowerCase());
						if (!result) {
							envList.map((env) => {
								if (applicationDetails?.propertiesValue?.[env]?.[a.filename]?.[a.propertyKey]?.newValue?.toLowerCase().includes(currentFilter.toLowerCase())) {
									result = true;
								};
							})
						}
						return result;
					}
				))
			console.log("Filtering... (" + applicationDetails?.properties?.length + " applications => " + filteredProperties?.length + ")");
		}
		setNewProductOwner(applicationDetails?.productOwner);
	}, [currentFilter, applicationDetails]);
	
	
	
	
	
	
	
	/* HANDLERS */

	function changeVersion(e) {
		console.log("Redirect : " + "/app/" + appId + "/version/" + e.target.value);
		navigate("/app/" + appId + "/version/" + e.target.value);
		setCurrentVersion(e.target.value);
	}

	function changeCopyVersion(e) {
		setCopyVersion(e.target.value);
	}
		
	function toggleEnvShow(e) {
		accessibleEnvList.map((env) => {
			if (e.target.dataset.envid == env) {
				switchEnvValue(env);
			}
		})
	}
	
	function saveProductOwnerCallback(productOwner) {
		productOwner==null?productOwner=document.getElementById("inputProductOwner").value:null;
		console.log("save Product Owner callback", productOwner);
		
		if (Keycloak.securityAdminCheck()) {
			ApiDefinition.updateApplication(appId, productOwner, () => {
				console.log("success update product callback");
				setApplicationDetails((current) => {
					current.productOwner = productOwner;
					return current;
				});
				unsetEditable("productOwner");
			});
	
			setNewProductOwner(productOwner);
		}
	}

	function switchIsProtectedCallback(envId, propertyKey, currentIsProtected) {
		console.log("switchIsProtectedCallback (" + envId + ", ", propertyKey, ", currentStatus: " + currentIsProtected + ")");
		
		if (Keycloak.securityAdminCheck()) {
			let newIsProtected = !currentIsProtected;
			ApiDefinition.switchPropertyIsProtected(appId, envId, currentVersion, propertyKey.filename, propertyKey.propertyKey, newIsProtected, () => {
				console.log("success switchIsProtectedCallback callback (new status: " + newIsProtected + ")");
				applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey].isProtected = newIsProtected;
				setApplicationDetails({... applicationDetails});
				unsetEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
			});
		}
	}

	function switchValidateCallback(envId, propertyKey, currentStatus) {
		console.log("switch validate callback (" + envId + ", ", propertyKey, ", currentStatus: " + currentStatus + ")");
		
		if (Keycloak.securityCheck(appId, envId, 'w')) {
			let newStatus = currentStatus===undefined||currentStatus=='VALID'?'TO_VALIDATE':'VALID';
			ApiDefinition.switchPropertyStatus(appId, envId, currentVersion, propertyKey.filename, propertyKey.propertyKey, newStatus, () => {
				console.log("success switchValidateCallback callback (new status: " + newStatus + ")");
				applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey].status = newStatus;
				setApplicationDetails({... applicationDetails});
				unsetEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
			});
		}
	}

	function switchOperationTypeCallback(envId, propertyKey, currentOperationType) {
		console.log("switch operationType callback (" + envId + ", ", propertyKey, ", currentStatus: " + currentOperationType + ")");
		
		if (Keycloak.securityCheck(appId, envId, 'w')) {
			let newOperationType = currentOperationType=='DEL'?'ADD':'DEL';
			ApiDefinition.switchPropertyOperationType(appId, envId, currentVersion, propertyKey.filename, propertyKey.propertyKey, newOperationType, () => {
				console.log("success operationType callback (new newOperationType: " + newOperationType + ")");
				if (applicationDetails.propertiesValue === undefined) applicationDetails.propertiesValue = {};
				if (applicationDetails.propertiesValue[envId] === undefined) applicationDetails.propertiesValue[envId] = {};
				if (applicationDetails.propertiesValue[envId][propertyKey.filename] === undefined) applicationDetails.propertiesValue[envId][propertyKey.filename] = {}; 
				if (applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey] === undefined) applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey] = {}; 
				applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey].operationType = newOperationType;
				setApplicationDetails({... applicationDetails});
				unsetEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
			});
		}
	}
	
	function savePropertyCallback(envId, propertyKey, newValue) {
		console.log("save callback (" + envId + ", ", propertyKey, ", " + newValue + ")");
		
		if (Keycloak.securityCheck(appId, envId, 'w')) {
			ApiDefinition.updateProperty(appId, envId, currentVersion, propertyKey.filename, propertyKey.propertyKey, newValue, () => {
				console.log("success savePropertyCallback callback (new newValue: " + newValue + ")");
				if (applicationDetails.propertiesValue === undefined) applicationDetails.propertiesValue = {};
				if (applicationDetails.propertiesValue[envId] === undefined) applicationDetails.propertiesValue[envId] = {};
				if (applicationDetails.propertiesValue[envId][propertyKey.filename] === undefined) applicationDetails.propertiesValue[envId][propertyKey.filename] = {}; 
				if (applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey] === undefined) applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey] = {}; 
				applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey].newValue = newValue;
				applicationDetails.propertiesValue[envId][propertyKey.filename][propertyKey.propertyKey].status = 'VALID';
				setApplicationDetails({... applicationDetails});
				unsetEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
			});
		}
	}
	
	function removePropertyCallback(propertyKey) {
		console.log("remove property callback (",  propertyKey, ")");
		ApiDefinition.removePropertyPermanent(appId, currentVersion, propertyKey.filename, propertyKey.propertyKey, () => {
			console.log("success removePropertyCallback callback");
			applicationDetails.properties = applicationDetails.properties.filter((el) => { return el.filename != propertyKey.filename || el.propertyKey != propertyKey.propertyKey; });
			setApplicationDetails({... applicationDetails});
			unsetEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
		});
	}
	
	function deleteAllPropertyCallback(filename, propertyKey) {
		console.log("deleteAllPropertyCallback", filename, propertyKey);
		ApiDefinition.removePropertyAllEnv(appId, currentVersion, filename, propertyKey, () => {
			console.log("success deleteAllPropertyCallback callback)");
			envList.map((env) => {
				if (applicationDetails.propertiesValue?.[env]?.[filename]?.[propertyKey] !== undefined) {
					applicationDetails.propertiesValue[env][filename][propertyKey].operationType = "DEL";
				}
			});
		});
	}
	
	function addAllPropertyCallback(filename, propertyKey) {
		console.log("addAllPropertyCallback", filename, propertyKey);
		ApiDefinition.addPropertyAllEnv(appId, currentVersion, filename, propertyKey, () => {
			console.log("success addAllPropertyCallback callback)");
			envList.map((env) => {
				if (applicationDetails.propertiesValue?.[env]?.[filename]?.[propertyKey] !== undefined) {
					applicationDetails.propertiesValue[env][filename][propertyKey].operationType = "ADD";
				}
			});
		});
	}
	
	function copyAllPropertiesCallback() {
		if (copyVersion === undefined || copyVersion == currentVersion) return;
		console.log("copyAllPropertiesCallback", appId, copyVersion, currentVersion);
		ApiDefinition.replaceAllPropertiesByVersion(appId, copyVersion, currentVersion, () => {
			console.log("success replaceAllPropertiesByVersion callback)");
			ApiDefinition.getApplicationDetails(appId, version, (data) => {
				setApplicationDetails(data);
				setApplicationVersions(data.existingVersions);
			});
		});
	}
	
	function updateFilter(e) {
		setCurrentFilter(e.currentTarget.value);
	}
	
	function editModeProductOwnerCallback() {
		console.log("edit mode Product Owner callback");
		setEditable("productOwner");
	}
	
	function cancelProductOwnerCallback() {
		console.log("edit mode Product Owner callback");
		unsetEditable("productOwner");
	}
	
	function editModePropertyCallback(envId, propertyKey) {
		console.log("edit mode callback (" + envId + ", ", propertyKey, ")");
		setEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
	}
	
	function cancelPropertyCallback(envId, propertyKey) {
		console.log("cancel callback (" + envId + ", ", propertyKey, ")");
		unsetEditable(envId + "_" + propertyKey.filename + "_" + propertyKey.propertyKey);
	}
	
	
	
	
	/* UTILS */
	
	function switchEnvValue(env, force = null) {
		if (currentEnvVisibility === undefined) return;
		let localEnvVisibility = {...currentEnvVisibility};
		force==null?
			localEnvVisibility[env]===undefined?
				localEnvVisibility[env]=true
				:localEnvVisibility[env]=!localEnvVisibility[env]
			:localEnvVisibility[env]=force;
		setCurrentEnvVisibility(localEnvVisibility);
	}
	
	function setEditable(key) {
		let localIsEditable = {... isEditable};
		localIsEditable[key] = true;
		setIsEditable(localIsEditable);
	}
	
	function unsetEditable(key) {
		let localIsEditable = {... isEditable};
		localIsEditable[key] = false;
		setIsEditable(localIsEditable);
	}
	



	return (
		<React.Fragment>
			<div className="app-details">
				<div className="app-details-header">
					<div className="title">
						<div>{applicationDetails?.appLabel}</div>
						<SelectVersion key={currentVersion} version={currentVersion} versions={applicationVersions} changeCallback={changeVersion} />
					</div>
					<span className="spacer" />
					<div className="env-selector">
						{
							accessibleEnvList?.map((env) => {
									return <CheckboxEnv key={env + "_" + currentVersion} env={env} status={currentEnvVisibility!==undefined&&currentEnvVisibility[env]!==undefined&&currentEnvVisibility[env]} properties={applicationDetails?.propertiesValue[env]} valueHandler={toggleEnvShow}/>
								})
						}
					</div>
					<div className="product-owner">
						{
							Keycloak.securityAdminCheck()?(
								isEditable!==undefined&&isEditable["productOwner"]!==undefined&&isEditable["productOwner"]?(
									<React.Fragment>
										<input autoFocus id="inputProductOwner" className="env-value" type="text" defaultValue={newProductOwner}
										 	onKeyDown={(e)=>{							
												console.log(e.key);
												if(e.key == "Enter") saveProductOwnerCallback()					
												if(e.key == "Escape") cancelProductOwnerCallback()
											}} />
										<div className="spacer" />
										<FontAwesomeIcon className="env-action" icon={faCheck}
												onClick={()=>{saveProductOwnerCallback()}} />
										<FontAwesomeIcon className="env-action" icon={faXmark}
												onClick={()=>{cancelProductOwnerCallback()}} />
									</React.Fragment>
								):(
									<React.Fragment>
										<div className="env-value">{newProductOwner}</div>
										<div className="spacer" />
										<FontAwesomeIcon className="env-action" icon={faPencil}
												onClick={(e)=>{editModeProductOwnerCallback()}} />
									</React.Fragment>
								)
							):<div className="env-value">{newProductOwner}</div>
						}
					</div>
				</div>
				<input id="appDetails_searchInput" onChange={updateFilter} className="search-input" type="text" placeholder={t('appdetails.search.placeholder')} defaultValue={currentFilter}></input>
				<table className="body">
					<thead><PropertyLineTitle envList={accessibleEnvList} envValues={currentEnvVisibility} /></thead>
					<tbody>{
						applicationDetails?.properties?.length > 0? 
						    (filteredProperties === undefined || filteredProperties?.length <= 0? 
								<td className="no-data">{t('appdetails.noconfiguration')}</td>
							 	: filteredProperties?.map((p) => {
									return <PropertyLine
										isAdmin={Keycloak.securityAdminCheck()}
										isEditable={isEditable}
										userCanEditEnv={userCanEditEnv}
										userCanEditAllEnv={userCanEditAllEnv}
										key={p.filename + "_" + p.propertyKey + "_" + currentVersion}
										envList={accessibleEnvList}
										envValues={currentEnvVisibility}
										propertyKey={p}
										propertiesValue={applicationDetails.propertiesValue}
										editModeCallback={editModePropertyCallback}
										switchOperationTypeCallback={switchOperationTypeCallback}
										switchIsProtectedCallback={switchIsProtectedCallback}
										switchValidateCallback={switchValidateCallback}
										savePropertyCallback={savePropertyCallback}
										cancelCallback={cancelPropertyCallback}
										addAllCallback={addAllPropertyCallback}
										deleteAllCallback={deleteAllPropertyCallback}
										removePropertyCallback={removePropertyCallback} />
								}))
								: <td className="no-data">
										Selectionner la version modele :&nbsp;
										<SelectVersion key={copyVersion} version={copyVersion} versions={applicationVersions} changeCallback={changeCopyVersion} />
										<button onClick={()=>{copyAllPropertiesCallback()}}>Copy Now !</button>
									</td>
					}</tbody>
				</table>
			</div>
		</React.Fragment>
	);
	
}

function SelectVersion(props) {
	return (
		<select value={props.version} onChange={props.changeCallback}>
			{
				props.versions?.sort().map((v) => {
					return <option key={"option_" + v} value={v}>{v}</option>
				})
			}
		</select>
	)
}

function CheckboxEnv(props) {
	return (
		<div className="checkbox-group">
			<input type="checkbox" id={"checkboxEnv_" + props.env}
            	data-envid={props.env}
                onChange={props.valueHandler}
                checked={props.status} />
			<label htmlFor={"checkboxEnv_" + props.env}>{props.env}</label>
		</div>
	)
}

function PropertyLineTitle(props) {
	
  	const { t } = useTranslation();

	return (
		<tr key={"LineTitle"} className="property-line-title">
			<th className="property-key">{t('appdetails.table.title.file')}</th>
			<th className="property-key">{t('appdetails.table.title.key')}</th>
			{
				props.envValues!==undefined?
					props.envList?.map((env) => {
						return <PropertyElementTitle key={env + "_title"} envId={env} envVisibility={props.envValues[env]} />
					}):null
			}
		</tr>
	)
}

function PropertyElementTitle(props) {
	return (
			props.envVisibility?(
				<th className="property-key">{props.envId.toUpperCase()}</th>
			)
			:null
	)
}

function PropertyLine(props) {
	return (
		<tr className="property-line">
				<td className="property-key"><span>{props.propertyKey.filename}</span></td>
				<td className="property-key">
					<div className="property-line-cell-content">
						<span>{props.propertyKey.propertyKey}</span>
						{
							(props.userCanEditAllEnv)?<span className="env-action"
									onClick={()=>{props.addAllCallback(props.propertyKey.filename, props.propertyKey.propertyKey)}} >
								<FontAwesomeIcon icon={faPlus} />
							</span>:null
						}
						{
							(props.userCanEditAllEnv)?<span className="env-action"
									onClick={()=>{props.deleteAllCallback(props.propertyKey.filename, props.propertyKey.propertyKey)}} >
								<FontAwesomeIcon icon={faMinus} />
							</span>:null
						}
						{
							(props.userCanEditAllEnv)?<span className="env-action"
									onClick={()=>{props.removePropertyCallback(props.propertyKey)}} >
								<FontAwesomeIcon className="error" icon={faTrash} />
							</span>:null
						}
					</div>
				</td>
			{
				props.envValues!==undefined?
					props.envList.map((env) => {
						return props.isEditable===undefined||props.isEditable==null||!props.isEditable[env + "_" + props.propertyKey.filename + "_" + props.propertyKey.propertyKey]?
						<PropertyElementView key={env + "_" + props.propertyKey.filename + "_" + props.propertyKey.propertyKey} userCanEdit={props.userCanEditEnv[env]} envId={env} envVisibility={props.envValues[env]} propertiesValue={props.propertiesValue} propertyKey={props.propertyKey} currentStatus={props.currentStatus}
									isAdmin={props.isAdmin}
									editModeCallback={props.editModeCallback}
									switchOperationTypeCallback={props.switchOperationTypeCallback}
									switchIsProtectedCallback={props.switchIsProtectedCallback}
									switchValidateCallback={props.switchValidateCallback} />	
						:<PropertyElementEdit key={env + "_" + props.propertyKey.filename + "_" + props.propertyKey.propertyKey} envId={env} envVisibility={props.envValues[env]} propertiesValue={props.propertiesValue} propertyKey={props.propertyKey}
									savePropertyCallback={props.savePropertyCallback}
									cancelCallback={props.cancelCallback} />
					}):null
			}
		</tr>
	)
}

function PropertyElementView(props) {
	
  	const { t } = useTranslation();
	
	const localProperty = props.propertiesValue?.[props.envId]?.[props.propertyKey.filename]?.[props.propertyKey.propertyKey];
	
	return (
			props.envVisibility?(props.isAdmin || !localProperty?.isProtected?(
				<td className={"status-" + (
											(localProperty?.status=="VALID")?
												"valid":"tovalidate"
										) + " status-" + (
											(localProperty?.operationType=="DEL")?
												"del":"add"
										)}><div className={"property-line-cell-content"} >{
					props.propertiesValue!=null&&props.propertiesValue[props.envId]!=null?(
							<span className="env-value"
									onDoubleClick={props.userCanEdit?()=>{props.editModeCallback(props.envId,props.propertyKey)}:null} >
								{(localProperty?.isProtected&&parseInt(localStorage.streamer_mod)==1)?"#####":localProperty?.newValue}
							</span>
						):(
							<span className="env-value"
									onDoubleClick={props.userCanEdit?()=>{props.editModeCallback(props.envId,props.propertyKey)}:null} >
								<i>{t('appdetails.novalue')}</i>
							</span>
						)}{
					props.userCanEdit?(
							<React.Fragment>
								{
									props.isAdmin?<span className="env-action" >
										<FontAwesomeIcon className={"status-" + (
												(props.propertiesValue[props.envId]!=null && localProperty?.isProtected)?
													"lock":"unlock"
											)} icon={faLock} onClick={(_e)=>{props.switchIsProtectedCallback(props.envId, props.propertyKey, localProperty?.isProtected)}} />
									</span>:null
								}
								<span className="env-action" >
									<FontAwesomeIcon className={"status-" + (
											(props.propertiesValue[props.envId]!=null && localProperty?.status=="VALID")?
												"valid":"tovalidate"
										)} icon={faWarning} onClick={(_e)=>{props.switchValidateCallback(props.envId, props.propertyKey, localProperty?.status)}} />
								</span>
								<span className="env-action" >
									<FontAwesomeIcon icon={faPencil} onClick={()=>{props.editModeCallback(props.envId, props.propertyKey)}} />
								</span>
								<span className="env-action" onClick={()=>{props.switchOperationTypeCallback(props.envId, props.propertyKey, localProperty?.operationType)}} >
									{
										(localProperty?.operationType!="DEL")?
											<FontAwesomeIcon icon={faMinus} />
											:<FontAwesomeIcon icon={faPlus} />
									}
								</span>
							</React.Fragment>
						):null
					}
				</div></td>
			):<td className={"status-locked"}><div className={"property-line-cell-content"}>
				<span className="env-action" ><FontAwesomeIcon icon={faLock} /></span>
				<span className="spacer" />
			</div></td>):null
	)
}

function PropertyElementEdit(props) {
	
	const localId = "input_" + props.envId + "_" + props.propertyKey.filename + "_" + props.propertyKey.propertyKey;
	const localDefaultValue = props.propertiesValue?.[props.envId]?.[props.propertyKey.filename]?.[props.propertyKey.propertyKey]?.newValue;
	const localProperty = props.propertiesValue?.[props.envId]?.[props.propertyKey.filename]?.[props.propertyKey.propertyKey];
	
	return (
			props.envVisibility?(
				<td className={"status-" + (
											(localProperty?.status=="VALID")?
												"valid":"tovalidate"
										) + " status-" + (
											(localProperty?.operationType=="DEL")?
												"del":"add"
										)}><div className={"property-line-cell-content"} >{
						<input autoFocus id={localId} className="env-value" type="text" defaultValue={localDefaultValue}
						 	onKeyDown={(e)=>{		
								if(e.key == "Enter")props.savePropertyCallback(props.envId, props.propertyKey, document.getElementById(localId).value)					
								if(e.key == "Escape")props.cancelCallback(props.envId, props.propertyKey)
							}} />
					}
					<FontAwesomeIcon className="env-action" icon={faCheck}
							onClick={()=>{props.savePropertyCallback(props.envId, props.propertyKey, document.getElementById(localId).value)}} />
					<FontAwesomeIcon className="env-action" icon={faXmark}
							onClick={()=>{props.cancelCallback(props.envId, props.propertyKey)}} />
				</div></td>
			):null
	)
}

