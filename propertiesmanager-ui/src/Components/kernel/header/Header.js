import './Header.css';

import React from 'react';
import { Link } from 'react-router-dom';
import ProfilMenu from './parts/ProfilMenu';
import StreamerMenu from './parts/StreamerMenu';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faBoxesPacking } from '@fortawesome/free-solid-svg-icons';

import { useTranslation } from 'react-i18next';
import LangSwitcher from './parts/LangSwitcher';
import { useLocation } from 'react-router-dom';

import AppStaticData from '../../AppStaticData';

export default function Header() {
	
	const location = useLocation();
	
  	const { t } = useTranslation();
  	
  	let i = 0;

	return (
		<React.Fragment>
			<header className="header">
		        <div className="title" htmlFor="hamb-checkbox">
		        	<FontAwesomeIcon className="logo" icon={faBoxesPacking} />&nbsp;{t('header.title')}
				</div>
		        <input className="hamb-checkbox" type="checkbox" id="hamb-checkbox"/>
				<label className="hamb" htmlFor="hamb-checkbox"><span className="hamb-line"></span></label>
		        <nav className="nav">
		            <ul className="menu">
						{
							AppStaticData.menuLinks?.map((link) => {return link.spacer===true?
							<li key={link.path + "_" + i++} className="nohover spacer" />
							:<HeaderLink
							key={link.path + "_" + i++}
							selected={location?.pathname===link.path}
							path={link.path} labelRef={link.i18nLabelRef} />})
						}
						<li className="nohover spacer" />
						<li className="nohover profile"><StreamerMenu /></li>
						<li className="nohover profile"><ProfilMenu /></li>
						<li className="nohover lang"><LangSwitcher /></li>
					</ul>
				</nav>
			</header>
		
		</React.Fragment>
	);
}

export function HeaderLink(props) {
	
  	const { t } = useTranslation();

	return <li key={props.path} className={props.selected===true?'selected':null}>
			{props.path.startsWith('http')?
				<a target="_blank" rel="noreferrer" key={props.path + "_link"} href={props.path}>{t(props.labelRef)}</a>
				:<Link key={props.path + "_link"} onClick={() => {document.getElementById('hamb-checkbox').checked = false}} to={props.path}>{t(props.labelRef)}</Link>}</li>
}





