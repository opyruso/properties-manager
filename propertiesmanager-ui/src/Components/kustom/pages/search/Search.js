import React, { useEffect, useState } from 'react';
import './Search.css';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import ApiDefinition from '../../api/ApiDefinition';
import { underlineSearchAndReplace } from '../../commons/Functions';
import { subscribe, unsubscribe } from '../../../AppStaticData';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLock } from '@fortawesome/free-solid-svg-icons';

export default function Search() {
        const { t } = useTranslation();
        const navigate = useNavigate();
        const [value, setValue] = useState('');
        const [results, setResults] = useState();
        const [revealed, setRevealed] = useState({});

        function doSearch() {
                if (value.trim() === '') return;
                ApiDefinition.searchValues(value.trim(), (data) => { setResults(data); setRevealed({}); });
        }

        function handleKey(e) {
                if (e.key === 'Enter') doSearch();
        }

        function goTo(result) {
                localStorage.setItem('appDetails_env', JSON.stringify({ [result.envId]: true }));
                localStorage.removeItem('appDetails_filter_' + result.appId);
                navigate('/app/' + result.appId + '/version/' + result.numVersion);
        }

        useEffect(() => {
                const listener = () => {
                        if (value.trim() !== '') {
                                doSearch();
                        }
                };
                subscribe('archivesChangeEvent', listener);
                return () => unsubscribe('archivesChangeEvent', listener);
        }, [value]);

        return (
                <div className="search">
                        <h1>{t('search.title')}</h1>
                        <div className="search-panel">
                                <input className="search-input" type="text" value={value} placeholder={t('search.placeholder')} onChange={(e)=>setValue(e.target.value)} onKeyDown={handleKey} />
                                <button onClick={doSearch}>{t('search.button')}</button>
                        </div>
                        <table>
                                <thead>
                                        <tr className="search-line-title">
                                                <th className="app-label">{t('search.application')}</th>
                                                <th className="product-owner">{t('search.owner')}</th>
                                                <th className="version">{t('search.version')}</th>
                                                <th className="env">{t('search.environment')}</th>
                                                <th className="deploy-date">{t('search.deploydate')}</th>
                                                <th className="key">{t('search.key')}</th>
                                                <th className="value">{t('search.value')}</th>
                                        </tr>
                                </thead>
                                <tbody>
                                        {
                                                results === undefined || results.length === 0 ?
                                                        <tr className="search-line"><td className="no-data" colSpan="7">{t('search.noresult')}</td></tr>
                                                        : results.map((r, i) => {
                                                                        const isProtected = r.isProtected;
                                                                        return (
                                                                                <tr key={i} className="search-line" onClick={() => goTo(r)}>
                                                                                        <td className="app-label">{underlineSearchAndReplace(r.appLabel || '', value)}</td>
                                                                                        <td className="product-owner">{underlineSearchAndReplace(r.productOwner || '', value)}</td>
                                                                                        <td className="version">{underlineSearchAndReplace(r.numVersion || '', value)}</td>
                                                                                        <td className="env">{r.envId}</td>
                                                                                        <td className="deploy-date">{r.deployDate ? new Date(r.deployDate).toLocaleString() : '-'}</td>
                                                                                        <td className="key">{underlineSearchAndReplace(r.propertyKey || '', value)}</td>
                                                                                        <td className="value">
                                                                                                {
                                                                                                        parseInt(localStorage.streamer_mod) === 1 && isProtected ?
                                                                                                                <span>
                                                                                                                        {revealed[i] ? underlineSearchAndReplace(r.value || '', value) : '*****'}
                                                                                                                        <FontAwesomeIcon className="lock" icon={faLock} onClick={(e)=>{e.stopPropagation(); setRevealed(prev=>({...prev, [i]: !prev[i]}));}} />
                                                                                                                </span>
                                                                                                                : underlineSearchAndReplace(r.value || '', value)
                                                                                                }
                                                                                        </td>
                                                                                </tr>
                                                                        );
                                                        })
                                       }
                               </tbody>
                       </table>
               </div>
        );
}

