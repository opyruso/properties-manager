import './GlobalVariables.css';

import React, { useState, useEffect } from 'react';

import { useKeycloak } from '@react-keycloak/web';

import Keycloak from '../../../Keycloak';

import AppContext from '../../../AppContext';

import ApiDefinition from '../../../kustom/api/ApiDefinition';
import { ButtonUtils, TextInputUtils } from '../../../kustom/commons/HtmlUtils';
import { t } from 'i18next';

export default function GlobalVariables() {

	const { keycloak, } = useKeycloak();

	const [envList, setEnvList] = useState();

	const [globalVariables, setGlobalVariables] = useState();
	const [selectedVariable, setSelectedVariable] = useState();
	
	const [newGlobalVariable, setNewGlobalVariable] = useState();
	
	



	

	/* HOOKS */
	
	useEffect(() => {
		AppContext.app!=undefined?
			setEnvList(AppContext.app.env)
			:null;
	}, [AppContext.app]);
	
	useEffect(() => {
		if (keycloak.authenticated && envList != undefined) {
			refreshGlobalVariables();
		}
	}, [envList, keycloak.authenticated]);
	
	

	
	
	


	
	
	
	/* HANDLERS */
		
	function selectVariableCallback(key) {
		console.log("selectVariable : ", key);
		setSelectedVariable(key);
	}
		
	function addNewGlobalVariableCallback(newKey) {
		console.log("addNewGlobalVariableCallback : ", newKey);
		addNewGlobalVariables(newKey);
	}
		
	let saveTimeout = undefined;
	function updateGlobalVariableCallback(key, envId, newValue) {
		console.log("updateGlobalVariableCallback : ", key, envId, newValue);
		if (saveTimeout !== undefined) {
			clearTimeout(saveTimeout);
		}
		saveTimeout = setTimeout(() => {
				updateGlobalVariableValue(key, envId, newValue);
			}, 1500);
	}
		
	function deleteGlobalVariableCallback(key) {
		console.log("deleteGlobalVariableCallback : ", key);
		if (confirm(t('globalvariable.remove.confirm'))) {
			deleteGlobalVariable(key);
		}
	}
	
	
	







	/* UTILS */
	function refreshGlobalVariables() {
		ApiDefinition.getGlobalVariables((data) => {
			console.log(data);
			setGlobalVariables(data);
		});
	}
	function addNewGlobalVariables(newKey) {
		ApiDefinition.addGlobalVariable(newKey, () => {
			refreshGlobalVariables();
			setNewGlobalVariable('');
		});
	}
	function updateGlobalVariableValue(key, env, value) {
		ApiDefinition.updateGlobalVariableValue(key, env, value, () => {
			refreshGlobalVariables();
		});
	}
	function deleteGlobalVariable(key) {
		ApiDefinition.deleteGlobalVariable(key, () => {
			refreshGlobalVariables();
			setSelectedVariable(undefined);
		});
	}

	
	
	
	


	return (
		<div className="global-variables">
			<div className="variables-list">
				{
					Keycloak.securityAdminCheck()?<React.Fragment>
						<div className="add-panel">
							<TextInputUtils id="newGlobalVariableKey" label={t('globalvariable.addnew')} value={newGlobalVariable} onChange={setNewGlobalVariable} />
							<ButtonUtils inactive={newGlobalVariable===undefined} label={t('globalvariable.addnew.btn')} onClick={() => {addNewGlobalVariableCallback(newGlobalVariable)}} />
						</div>
					</React.Fragment>:null
				}
				<ListGlobalVariables list={globalVariables?.keys} selectedVariable={selectedVariable} onClick={selectVariableCallback} />
			</div>
			<div className="edit-panel">
				{
					selectedVariable!==undefined?<React.Fragment>
						<h2>{selectedVariable}</h2>
						<GlobalVariablePanel envList={envList} variable={selectedVariable} values={globalVariables.values?.[selectedVariable]} onChange={updateGlobalVariableCallback} />
						<div className="spacer" />
						<ButtonUtils label={t('globalvariable.remove.btn')} onClick={() => {deleteGlobalVariableCallback(selectedVariable)}} />
					</React.Fragment>:null
				}
			</div>
		</div>
	);
}





function ListGlobalVariables(props) {
	return <React.Fragment>
			{
				props.list?.map((gv) => {return <div key={gv.globalVariableKey} className={(props.selectedVariable==gv.globalVariableKey)?"global-variable-item selected":"global-variable-item"} onClick={(_e) => {props.onClick(gv.globalVariableKey)}}>{gv.globalVariableKey}</div>})
			}
		</React.Fragment>;
}

function GlobalVariablePanel(props) {
	return <React.Fragment>
			{
				props.envList?.map((env) => {
					return <TextInputUtils key={props.variable + "_" + env} label={t('globalvariable.editvalue') + ' : ' + env} defaultValue={props.values?.[env]?.newValue}
								onChange={(e) => {props.onChange(props.variable, env, e)}} />;
				})
			}
		</React.Fragment>;
}


