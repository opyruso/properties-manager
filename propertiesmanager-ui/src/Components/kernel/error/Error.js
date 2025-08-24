import React from 'react';

import { useTranslation } from 'react-i18next';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faWarning } from '@fortawesome/free-solid-svg-icons'

import ProfilMenu from '../header/parts/ProfilMenu';

export default function UnkownError(props) {
	
  	const { t } = useTranslation();

	return (
		<React.Fragment>
			<div className="error-page">
				<FontAwesomeIcon style={{'fontSize':'10em'}} icon={faWarning} />
				{t('error.unknown')}&nbsp;({props.errNum})
			</div>
		</React.Fragment>
	);
}

export function NotFoundError(props) {
	
  	const { t } = useTranslation();

	return (
		<React.Fragment>
			<div className="error-page">
				<FontAwesomeIcon style={{'fontSize':'10em'}} icon={faWarning} />
				{t('error.notfound')}&nbsp;({props.errNum})
			</div>
		</React.Fragment>
	);
}

export function ForbiddenError() {
	return (
		<React.Fragment>
			<div className="error-page" style={{'fontSize':'10em'}}>
				<ProfilMenu />
			</div>
		</React.Fragment>
	);
}
