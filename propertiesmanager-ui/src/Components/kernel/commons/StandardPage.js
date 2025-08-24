import React, { useEffect, useState } from 'react';

import { useTranslation } from 'react-i18next';

export function LineStandard(props) {
	
  	const { t } = useTranslation();
  	
  	const [content, setContent] = useState(props.content);
  	
  	useEffect(() => {
		setContent(generateHtml(t(props.content + '.content', {interpolation: {escapeValue: false}}), props.vars));
	}, [t, props.content, props.vars]);
  	
  	function generateHtml(c, v) {
	  	if (v!==undefined) {
			Object.keys(v).map((k) => {
				let r = new RegExp("\\{\\{"+k+"\\}\\}", "g");
				c = c.replace(r, v[k]);
				return true;
			});
		}
		return c;
	}

	return <React.Fragment>
			<div className={props.invert?'line line-invert':'line'}>
				<div className="spacer" />
				<div className="illustration"><img src={ t(props.content + '.illustration') } alt={ t(props.content + '.title') } /></div>
				<div className="content">
					<span className="title">{ t(props.content + '.title') }</span>
					<span className="text"dangerouslySetInnerHTML={{__html: content }} />
				</div>
				<div className="spacer" />
			</div>
		</React.Fragment>
}