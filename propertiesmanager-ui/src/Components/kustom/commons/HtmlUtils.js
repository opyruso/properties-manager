import React from 'react';

import { underlineJsonPath, underlineProperties, underlineSearchAndReplace, underlineXpath } from './Functions';

let keyId = 0;

export function TextInputUtils(props) {
	return <div className={"group-element"}>
				<span className="group-element-label">{props.label}</span>
				<input className="group-element-form" id={props.id} value={props.value} defaultValue={props.defaultValue} type="text" onChange={(e)=>{props.onChange!==undefined?props.onChange(e.target.value):null}} />
	</div>;
}

export function DropDownUtils(props) {
	return <div className={"group-element"}>
				<span className="group-element-label">{props.label}</span>
				<select className="group-element-form" disabled={props.list === undefined} value={props.selectedValue} onChange={(e)=>{props.onChange!==undefined?props.onChange(e.target.value):null}}>
					{props.selectedValue===undefined?<option key={props.label + "_option_undefined"} value={undefined}>-</option>:null}
					{
						props.list?.map((v) => {
							return <option key={props.label + "_option_" + keyId++} value={v?.[props.idParam]===undefined?v:v[props.idParam]}>{v?.[props.textParam]===undefined?v:v[props.textParam]}</option>
						})
					}
				</select>
	</div>;
}

export function FileInputUtils(props) {
	return <div className={"group-element"}>
				<span className="group-element-label">{props.label}</span>
				<input className="group-element-form" type="file" onChange={(e)=>{props.onChange!==undefined?props.onChange(e.target.files[0]):null}} />
	</div>;
}

export function ButtonUtils(props) {
	return <div className={"group-element"}>
				<button className="group-element-form" disabled={props.inactive} type="button" onClick={(e)=>{props.onClick!==undefined?props.onClick(e):null}}>{props.label}</button>
	</div>;
}

export function RichContentUtils(props) {
	return <div className={"group-element"}>
				<span className="group-element-label">{props.label}</span>
				<div className="group-element-form rich-content">
					{
						props.underline===undefined?props.content
						:underline(props.content, props.underline, props.contentType)
					}
				</div>
	</div>;
}


function underline(content, text, contentType) {
	switch(contentType?.toLowerCase()) {
		case 'xml': return underlineXpath(content, text);
		case 'properties': return underlineProperties(content, text);
		case 'json': return underlineJsonPath(content, text);
		default: console.log('more...');
	}
	if (content.startsWith('<')) return underlineXpath(content, text);
		console.log('more... 2');
	if (content.startsWith('{') || text.startsWith('[')) return underlineJsonPath(content, text);
		console.log('more... 3');
	if (content.match(/\n[^\n]+=[^\n]+\n/g) !== null) return underlineProperties(content, text);
		console.log('default');
	return underlineSearchAndReplace(content, text);
}
