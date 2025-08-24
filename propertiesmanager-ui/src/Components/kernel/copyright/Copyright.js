import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCopyright } from '@fortawesome/free-solid-svg-icons'

export default function Copyright() {
	return (
		<React.Fragment>
			<div className="copyright">
				<FontAwesomeIcon icon={faCopyright} />&nbsp;oPyRuSo 2022
			</div>
		</React.Fragment>
	);
}
