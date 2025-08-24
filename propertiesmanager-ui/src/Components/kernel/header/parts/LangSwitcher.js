import React from 'react';
import { useTranslation } from 'react-i18next';

import AppStaticData from '../../../AppStaticData';

function LangSwitcher() {

  const { i18n } = useTranslation();

  return (
    <React.Fragment>
    	<img className="lang-country" src={AppStaticData.lngs[i18n.resolvedLanguage].iconName} alt={AppStaticData.lngs[i18n.resolvedLanguage].iconName} />
    	<select className="lang-switcher" defaultValue={i18n.resolvedLanguage} onChange={(e)=>{
					console.log("changing lang : " + e.target.value, e);
					i18n.changeLanguage(e.target.value);
				}}>
    	{Object.keys(AppStaticData.lngs).map((lng) => (
			<option key={lng} value={lng} className={i18n.resolvedLanguage === lng ? 'selected' : ''} >
              {AppStaticData.lngs[lng].nativeName}
            </option>
          ))}
          
    	</select>
    </React.Fragment>
  );
}

export default LangSwitcher;