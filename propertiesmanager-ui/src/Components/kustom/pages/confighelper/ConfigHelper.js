import './ConfigHelper.css';

import React, { useState, useEffect, useContext } from 'react';

import { useKeycloakInstance } from '../../../Keycloak';
import Keycloak from '../../../Keycloak';

import AppContext from '../../../AppContext';

import ApiDefinition from '../../../kustom/api/ApiDefinition';
import { ButtonUtils, DropDownUtils, FileInputUtils, RichContentUtils, TextInputUtils } from '../../../kustom/commons/HtmlUtils';
import { useTranslation } from 'react-i18next';
import { subscribe, unsubscribe } from '../../../AppStaticData';

export default function ConfigHelper() {
	
  	const { t } = useTranslation();

const { keycloak } = useKeycloakInstance();
        const { app } = useContext(AppContext);

	const [envList, setEnvList] = useState();
	
	const [applications, setApplications] = useState();
	const [versions, setVersions] = useState();
	const [files, setFiles] = useState();
	const [applicationDetails, setApplicationDetails] = useState();

	const [selectedEnvironment, setSelectedEnvironment] = useState();
	const [selectedApplication, setSelectedApplication] = useState();
	const [selectedVersion, setSelectedVersion] = useState();
	const [selectedFilename, setSelectedFilename] = useState();
	
	const [newPropertyKey, setNewPropertyKey] = useState();
	const [newValue, setNewValue] = useState();

        const [textInput, setTextInput] = useState();
        const [logsOutput, setLogsOutput] = useState();
        const [selectedTab, setSelectedTab] = useState('source');
	
	



	

	/* HOOKS */
	
        useEffect(() => {
                app!=undefined?
                        setEnvList(app.env)
                        :null;
        }, [app]);
	
useEffect(() => {
if (keycloak?.authenticated && envList != undefined) {
                        setApplications(undefined);
			setVersions(undefined);
			setFiles(undefined);
			setTextInput(undefined);
			setLogsOutput(undefined);
			
			setSelectedEnvironment(undefined);
			setSelectedApplication(undefined);
			setSelectedVersion(undefined);
			setSelectedFilename(undefined);
			
			refreshApplications();
		}
}, [envList, keycloak?.authenticated]);

useEffect(() => {
                const listener = () => {
                        refreshApplications();
                };
                subscribe('archivesChangeEvent', listener);
                return () => unsubscribe('archivesChangeEvent', listener);
}, []);
	
	

	
	
	


	
	
	
	/* HANDLERS */
		
	function selectApplicationCallback(appId) {
		console.log("selectApplicationCallback : ", appId);
		setSelectedApplication(appId);
		refreshVersionsByAppId(appId);
		setSelectedVersion(undefined);
		setFiles(undefined);
		setSelectedFilename(undefined);
		setLogsOutput(undefined);
	}
		
	function selectVersionCallback(version) {
		console.log("selectVersionCallback : ", version);
		setSelectedVersion(version);
		refreshFilesByAppIdAndVersion(selectedApplication, version);
		refreshApplicationDetailsByAppIdAndVersion(selectedApplication, version);
		setSelectedFilename(undefined);
		setLogsOutput(undefined);
	}
		
	function selectFilenameCallback(filename) {
		console.log("selectFilenameCallback : ", filename);
		setSelectedFilename(filename);
		let content = atob(files[filename]);
		setTextInput(content);
		setLogsOutput(content);
	}
		
	function selectEnvironmentCallback(envId) {
		console.log("selectEnvironmentCallback : ", envId);
		setSelectedEnvironment(envId);
	}
		
	function selectFileContentCallback(file) {
		console.log("selectFileContentCallback : ", file);
		let fr = new FileReader();
                fr.onloadend = (_e)=>{
                        let text = fr.result;
                        try {
                                JSON.stringify(btoa(text));
                                setTextInput(text);
                                setLogsOutput(text);
                                if (Keycloak.securityAdminCheck() && selectedApplication !== undefined && selectedVersion !== undefined && selectedFilename !== undefined) {
                                        addOrUpdatePropertiesFile(selectedApplication, selectedVersion, selectedFilename, text);
                                }
                        } catch (e) {
                                console.error(e);
                                setTextInput("Error, Invalid file content !");
                                setLogsOutput("Error, Invalid file content !");
                        }
                };
                fr.readAsText(file);
        }
		
        function startTestCallback() {
                console.log("startTestCallback");
                setLogsOutput("...");
                setSelectedTab('result');
                startTest(selectedApplication, selectedVersion, selectedFilename, selectedEnvironment, textInput);
        }
		
	function addOrUpdatePropertiesFileCallback() {
		console.log("addOrUpdatePropertiesFileCallback");
		setLogsOutput("...");
		addOrUpdatePropertiesFile(selectedApplication, selectedVersion, selectedFilename, textInput);
	}
		
	function addNewPropertyKeyCallback() {
		console.log("addNewPropertyKeyCallback");
		addNewPropertyKey(selectedApplication, selectedVersion, selectedFilename, newPropertyKey, newValue);
	}
		
	function selectPropertyKeyCallback(key, val) {
		console.log("selectPropertyKeyCallback", key);
		setNewPropertyKey(key);
		setNewValue(val);
	}
		
	function changeNewPropertyKeyCallback(key) {
		console.log("changeNewPropertyKeyCallback", key);
		setNewPropertyKey(key);
		setNewValue(undefined);
	}
		
	function changeNewValueCallback(val) {
		console.log("changeNewValueCallback", val);
		setNewValue(val);
	}
		
	function changeNewFilenameCallback(val) {
		console.log("changeNewFilenameCallback", val);
		setSelectedFilename(val);
	}
	
	
	
	







	/* UTILS */
	function refreshApplications() {
		ApiDefinition.getApplications((data) => {
			setApplications(data);
		});
	}
	
	function refreshVersionsByAppId(appId) {
		ApiDefinition.getApplicationVersions(appId, (data) => {
			setVersions(data);
		});
	}
	
	function refreshFilesByAppIdAndVersion(appId, version) {
		ApiDefinition.getFilesByAppIdAndVersion(appId, version, (data) => {
			console.log("AAAAA", data);
			setFiles(data);
		});
	}
	
	function refreshApplicationDetailsByAppIdAndVersion(appId, version) {
		ApiDefinition.getApplicationDetails(appId, version, (data) => {
			setApplicationDetails(data);
		});
	}
	
	function startTest(appId, version, filename, envId, fileContent) {
		ApiDefinition.testFile(appId, version, filename, envId, fileContent, (result, logsOutput) => {
			console.log("startTest ending correctly : ", result, logsOutput);
			setLogsOutput(logsOutput);
		}, (_e) => {
			setLogsOutput("API Offline or you didn't have rights to perform this task !");
		});
	}
	
	function addNewPropertyKey(appId, version, filename, newPropertyKey, newValue) {
		ApiDefinition.addProperty(appId, version, filename, newPropertyKey, newValue, () => {
			console.log("addNewPropertyKey ending correctly : ", logsOutput);
			setNewPropertyKey(undefined);
			setNewValue(undefined);
			refreshApplicationDetailsByAppIdAndVersion(appId, version);
			if (selectedEnvironment !== undefined && textInput !== undefined && logsOutput !== undefined)
				startTest(selectedApplication, selectedVersion, selectedFilename, selectedEnvironment, textInput);
		}, (_e) => {
			setLogsOutput("API Offline or you didn't have rights to perform this task !");
		});
	}
	
	function addOrUpdatePropertiesFile(appId, version, filename, fileContent) {
		ApiDefinition.addOrUpdateFile(appId, version, filename, fileContent, () => {
			console.log("addOrUpdatePropertiesFileCallback ending correctly : ", logsOutput);
			setNewPropertyKey(undefined);
			setNewValue(undefined);
			if (selectedEnvironment !== undefined && textInput !== undefined && logsOutput !== undefined)
				startTest(appId, version, filename, selectedEnvironment, fileContent);
		}, (_e) => {
			setLogsOutput("API Offline or you didn't have rights to perform this task !");
		});
	}
	
	
	
	


	return (
		<div className="testing">
			<div className="testing-content">
				<div className="testing-parameters">
					<div className="testing-parameters-line">
						<DropDownUtils label={t('confighelper.params.title.application')} idParam="appId" textParam="appLabel" list={applications?.sort()} selectedValue={selectedApplication} onChange={selectApplicationCallback} />
						<DropDownUtils label={t('confighelper.params.title.versions')} list={versions?.sort()} selectedValue={selectedVersion} onChange={selectVersionCallback} />
					</div>
					<div className="testing-parameters-line">
						<DropDownUtils label={t('confighelper.params.title.files')} list={files!==undefined?Object.keys(files).sort():null} selectedValue={selectedFilename} onChange={selectFilenameCallback} />
						<DropDownUtils label={t('confighelper.params.title.environment')} list={envList} selectedValue={selectedEnvironment} onChange={selectEnvironmentCallback} />
					</div>
					<div className="testing-parameters-line">
						<ButtonUtils label={t('confighelper.params.title.testing')} inactive={selectedApplication===undefined || selectedVersion===undefined || selectedFilename===undefined || selectedEnvironment===undefined || textInput===undefined} onClick={startTestCallback} />
					</div>
					<hr />
					<div className="testing-parameters-line">
						<TextInputUtils label={t('confighelper.params.title.newfilename')} value={selectedFilename} onChange={changeNewFilenameCallback} />
						<FileInputUtils label={t('confighelper.params.title.replacefile')} onChange={selectFileContentCallback} />
					</div>
					<div className="testing-parameters-line">
						<ButtonUtils label={t('confighelper.params.title.save')} inactive={selectedApplication===undefined || selectedVersion===undefined || selectedFilename===undefined || textInput===undefined} onClick={addOrUpdatePropertiesFileCallback} />
					</div>
					<hr />
					{selectedFilename!==undefined?(
						<React.Fragment>
							<div className="testing-parameters-line">
								<TextInputUtils label={t('confighelper.params.title.newkey')} value={newPropertyKey} onChange={changeNewPropertyKeyCallback} />
								<TextInputUtils label={t('confighelper.params.title.defaultvalue')} value={newValue} onChange={changeNewValueCallback} />
							</div>
							<div className="testing-parameters-line">
								<ButtonUtils label={t('confighelper.params.title.selectedfilename') + selectedFilename}
									inactive={newPropertyKey===undefined || selectedApplication===undefined || selectedVersion===undefined || selectedFilename===undefined} onClick={addNewPropertyKeyCallback} />
							</div>
							<hr />
						</React.Fragment>
						):null}
					<div className="testing-parameters-line">
						<ExistingRules applicationDetails={applicationDetails} selectedFilename={selectedFilename} selectPropertyKeyCallback={changeNewPropertyKeyCallback} />
					</div>
				</div>
                                <div className="testing-files">
                                        <div className="testing-tabs">
                                                <button className={selectedTab==='source'?"selected":null} onClick={()=>setSelectedTab('source')}>{t('confighelper.title.testcontent')}</button>
                                                <button className={selectedTab==='result'?"selected":null} onClick={()=>setSelectedTab('result')}>{t('confighelper.title.testresult')}</button>
                                        </div>
                                        <div className="testing-tab-content">
                                                {selectedTab==='source'?(
                                                        <RichContentUtils label={t('confighelper.title.testcontent')} content={textInput} contentType={selectedFilename?.split('.').slice(-1)[0]} underline={newPropertyKey} />
                                                ):(
                                                        <LogReader label={t('confighelper.title.testresult')} logsOutput={logsOutput} selectPropertyKeyCallback={selectPropertyKeyCallback} />
                                                )}
                                        </div>
                                </div>
			</div>
		</div>
	);
}

function LogReader(props) {
	return <div className={"group-element"}>
				<span className="group-element-label">{props.label}</span>
				<div className="group-element-form log-lines">
				{
					props.logsOutput===undefined?''
						:!Array.isArray(props.logsOutput)?<pre>{props.logsOutput}</pre>
							:props.logsOutput?.map((l) => {
								return <div className={"log-line log-line-status-" + l?.status} >
											<span className={"log-line-status"}>{l?.status}</span>
											<span className={"log-line-comment"}>{
												l?.data?.propertyKey!==undefined?<div><span className={"key-found"} onClick={(_e)=>{props.selectPropertyKeyCallback(l.data.propertyKey, l.data.value)}}>{l?.comment}</span></div>
												:l?.comment
											}</span>
										</div>
							})
				}
				</div>
	</div>;
}

function ExistingRules(props) {
	
  	const { t } = useTranslation();

	return <div className={"group-element"}>
				<span className="group-element-label">{t('confighelper.existingkey.title')}</span>
				{
					props.applicationDetails?.properties?.map((p) => {
						return p.filename===props.selectedFilename?<div key={ExistingRules + '_' + p.propertyKey} className="actual-property" onClick={(_e)=>{props.selectPropertyKeyCallback(p.propertyKey)}}>{p.propertyKey}</div>:null
					})
				}
		</div>;
}


