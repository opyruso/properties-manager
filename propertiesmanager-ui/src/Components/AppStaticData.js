export default {
	menuLinks : [
			{path: '/', i18nLabelRef: 'header.title.apps'},
			{path: '/config-helper', i18nLabelRef: 'header.title.config-helper'},
			{path: '/global-variables', i18nLabelRef: 'header.title.global-variables'},
		],
	lngs : {
		  fr: { nativeName: 'Français', iconName: '/resources/lang/France-flag.png' },
		  en: { nativeName: 'English', iconName: '/resources/lang/Great-Britain-flag.png' },
		  de: { nativeName: 'Deutsch', iconName: '/resources/lang/Germany-flag.png' },
		  lu: { nativeName: 'Luxembourgeois', iconName: '/resources/lang/Luxembourg-flag.png' }
		}
}

function subscribe(eventName, listener) {
  document.addEventListener(eventName, listener);
}

function unsubscribe(eventName, listener) {
  document.removeEventListener(eventName, listener);
}

function publish(eventName, data) {
  const event = new CustomEvent(eventName, { detail: data });
  document.dispatchEvent(event);
}

export { publish, subscribe, unsubscribe};