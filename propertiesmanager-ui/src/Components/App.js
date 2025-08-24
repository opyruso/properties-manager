import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";

import './kernel/Standard.css';
import './kustom/App.css';

import { subscribe } from './AppStaticData';
import AppContext from './AppContext';
import Header from './kernel/header/Header';
import Footer from './kernel/footer/Footer';
import AppRouter from './kustom/AppRouter';
import { Rings } from 'react-loader-spinner';

export default function App() {

  	const [loading, setLoading] = useState(1);
  	
  	const navigate = useNavigate();

	useEffect(() => {
		console.log("Init events");
		
		// eslint-disable-next-line no-unused-expressions
		AppContext.app===undefined?initConfig():null;
		
		if (document.readyState === "complete") {
			subscribe("startLoadingEvent", () => {
				console.log("startLoadingEvent");
				setLoading(loading+1);
			});
			subscribe("endLoadingEvent", () => {
				console.log("endLoadingEvent");
				setLoading(loading>0?loading-1:0);
			});
			
		}
		
	}, [loading]);
	
	function initConfig() {
		console.log("Init config...");
		fetch('/config/config.json', {
			method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
			}
		})
		.then(res => res.json())
		.then((data) => {
			console.log("Response config success : ", data);
			AppContext.app = data;
			setLoading(loading>0?loading-1:0);
		})
		.catch((e) => {
			console.error("Response config error : ", e);
			navigate('/error');
			setLoading(loading>0?loading-1:0);
		});
	}

	return (
		<React.Fragment>
			<Rings visible={loading > 0}
				color="red"
				radius={300}
				width={300}
				height={300}
				wrapperClass="loading-overlay" />
			<div className="main-header">
				<Header />
			</div>
			<div className="main-body">
				<AppRouter  />
			</div>
			<div className="main-footer">
				<Footer />
			</div>
		</React.Fragment>
	);
}
