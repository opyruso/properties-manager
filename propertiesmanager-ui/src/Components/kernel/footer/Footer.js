import './Footer.css'

import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCopyright } from '@fortawesome/free-solid-svg-icons'

import AppStaticData from '../../AppStaticData';
import AppContext from '../../AppContext';

export default function Footer() {
	
	const [errorMessage, setErrorMessage] = useState('');
	
	useEffect(() => {
		setErrorMessage(AppContext.lastErrorMessage);
	}, [AppContext.lastErrorMessage]);
	
	return (
		<React.Fragment>
			<div className="version">
				{AppStaticData.version}
			</div>
			<div className="spacer" />
			<div className="error">
				{errorMessage}
			</div>
			<div className="spacer" />
			<div className="copyright">
				<Link to="/copyright">
					<FontAwesomeIcon icon={faCopyright} /> Copyright oPyRuSo 2022
				</Link>
			</div>
		</React.Fragment>
	);
}
