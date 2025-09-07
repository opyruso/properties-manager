import './GlobalVariables.css';

import React, { useState, useEffect, useContext } from 'react';

import Keycloak, { useKeycloakInstance } from '../../../Keycloak';

import AppContext from '../../../AppContext';

import ApiDefinition from '../../../kustom/api/ApiDefinition';
import { ButtonUtils, TextInputUtils } from '../../../kustom/commons/HtmlUtils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faXmark, faTrash } from '@fortawesome/free-solid-svg-icons';
import { t } from 'i18next';

export default function GlobalVariables() {

        const { keycloak } = useKeycloakInstance();
        const { app } = useContext(AppContext);

        const [envList, setEnvList] = useState();

        const [globalVariables, setGlobalVariables] = useState();

        const [newGlobalVariable, setNewGlobalVariable] = useState('');

        const [editMode, setEditMode] = useState({});


        /* HOOKS */

        useEffect(() => {
                app != undefined ?
                        setEnvList(app.env)
                        : null;
        }, [app]);

        useEffect(() => {
                if (keycloak?.authenticated && envList != undefined) {
                        refreshGlobalVariables();
                }
        }, [envList, keycloak?.authenticated]);


        /* HANDLERS */

        function addNewGlobalVariableCallback() {
                addNewGlobalVariables(newGlobalVariable);
        }

        function deleteGlobalVariableCallback(key) {
                if (confirm(t('globalvariable.remove.confirm'))) {
                        deleteGlobalVariable(key);
                }
        }

        function switchEditMode(env, key, mode) {
                const cellId = env + '_' + key;
                setEditMode((prev) => {
                        const copy = { ...prev };
                        if (mode) {
                                copy[cellId] = true;
                        } else {
                                delete copy[cellId];
                        }
                        return copy;
                });
        }

        function updateGlobalVariableCallback(key, env, value) {
                updateGlobalVariableValue(key, env, value);
        }


        /* UTILS */
        function refreshGlobalVariables() {
                ApiDefinition.getGlobalVariables((data) => {
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
                        switchEditMode(env, key, false);
                });
        }
        function deleteGlobalVariable(key) {
                ApiDefinition.deleteGlobalVariable(key, () => {
                        refreshGlobalVariables();
                });
        }


        /* RENDER */
        return (
                <div className="global-variables">
                        {
                                Keycloak.securityAdminCheck() ? <div className="add-panel">
                                        <TextInputUtils id="newGlobalVariableKey" label={t('globalvariable.newkey')} value={newGlobalVariable} onChange={setNewGlobalVariable} />
                                        <ButtonUtils inactive={newGlobalVariable === undefined || newGlobalVariable === ''} label={t('globalvariable.addnew.btn')} onClick={() => { addNewGlobalVariableCallback(); }} />
                                </div> : null
                        }
                        <table>
                                <thead>
                                        <tr className="global-variable-line-title">
                                                <th>{t('appdetails.table.title.key')}</th>
                                                {envList?.map((env) => { return <th key={env}>{env}</th>; })}
                                                <th className="delete-col"></th>
                                        </tr>
                                </thead>
                                <tbody>
                                        {
                                                globalVariables?.keys?.map((gv) => {
                                                        return <tr key={gv.globalVariableKey} className="global-variable-line">
                                                                <td className="global-variable-key"><div className="cell-content"><span>{gv.globalVariableKey}</span></div></td>
                                                                {
                                                                        envList?.map((env) => {
                                                                                const cellId = env + '_' + gv.globalVariableKey;
                                                                                const inputId = 'input_' + cellId;
                                                                                const localValue = globalVariables.values?.[gv.globalVariableKey]?.[env]?.newValue;
                                                                                return editMode[cellId] ?
                                                                                        <td key={cellId}>
                                                                                                <div className="cell-content">
                                                                                                        <input autoFocus id={inputId} className="env-value" type="text" defaultValue={localValue}
                                                                                                                onKeyDown={(e) => {
                                                                                                                        if (e.key === 'Enter') updateGlobalVariableCallback(gv.globalVariableKey, env, document.getElementById(inputId).value);
                                                                                                                        if (e.key === 'Escape') switchEditMode(env, gv.globalVariableKey, false);
                                                                                                                }} />
                                                                                                        <FontAwesomeIcon className="env-action" icon={faCheck} onClick={() => { updateGlobalVariableCallback(gv.globalVariableKey, env, document.getElementById(inputId).value); }} />
                                                                                                        <FontAwesomeIcon className="env-action" icon={faXmark} onClick={() => { switchEditMode(env, gv.globalVariableKey, false); }} />
                                                                                                </div>
                                                                                        </td>
                                                                                        :
                                                                                        <td key={cellId}>
                                                                                                <div className="cell-content">
                                                                                                        <span className="env-value" onDoubleClick={() => { switchEditMode(env, gv.globalVariableKey, true); }}>
                                                                                                                {localValue !== undefined && localValue !== null ? localValue : <i>{t('appdetails.novalue')}</i>}
                                                                                                        </span>
                                                                                                </div>
                                                                                        </td>;
                                                                        })
                                                                }
                                                                <td className="delete-col">
                                                                        {Keycloak.securityAdminCheck() ? <div className="cell-content">
                                                                                <span className="env-action" onClick={() => { deleteGlobalVariableCallback(gv.globalVariableKey); }}><FontAwesomeIcon className="error" icon={faTrash} /></span>
                                                                        </div> : null}
                                                                </td>
                                                        </tr>;
                                                })
                                        }
                                </tbody>
                        </table>
                </div>
        );
}


