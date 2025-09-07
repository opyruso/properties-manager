import React, { useState } from 'react';
import './Search.css';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import ApiDefinition from '../../api/ApiDefinition';
import { underlineSearchAndReplace } from '../../commons/Functions';

export default function Search() {
        const { t } = useTranslation();
        const navigate = useNavigate();
        const [value, setValue] = useState('');
        const [results, setResults] = useState();

        function doSearch() {
                if (value.trim() === '') return;
                ApiDefinition.searchValues(value.trim(), (data) => { setResults(data); });
        }

        function handleKey(e) {
                if (e.key === 'Enter') doSearch();
        }

        function goTo(result) {
                localStorage.setItem('appDetails_env', result.envId);
                localStorage.setItem('appDetails_filter_' + result.appId, value);
                navigate('/app/' + result.appId + '/version/' + result.numVersion);
        }

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
                                                        : results.map((r, i) =>
                                                                <tr key={i} className="search-line" onClick={() => goTo(r)}>
                                                                        <td className="app-label">{r.appLabel}</td>
                                                                        <td className="product-owner">{r.productOwner}</td>
                                                                        <td className="version">{r.numVersion}</td>
                                                                        <td className="env">{r.envId}</td>
                                                                        <td className="deploy-date">{r.deployDate ? new Date(r.deployDate).toLocaleString() : '-'}</td>
                                                                        <td className="key">{r.propertyKey}</td>
                                                                        <td className="value">{underlineSearchAndReplace(r.value || '', value)}</td>
                                                                </tr>
                                                        )
                                        }
                                </tbody>
                        </table>
                </div>
        );
}

