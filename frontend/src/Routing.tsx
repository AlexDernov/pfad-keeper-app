import L from "leaflet";
import 'leaflet-routing-machine';
import {useMap} from "react-leaflet";
import './index.css'
import "leaflet/dist/leaflet.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import "leaflet-routing-machine/dist/leaflet-routing-machine.css";
import React, {useEffect, useState} from "react";
import {MyCoords} from "./types/MyCoords.tsx";

type Props = {
    setter: React.Dispatch<React.SetStateAction<L.Routing.Control | undefined>> | undefined;
    coords: MyCoords[],
    planOn: boolean
}
export default function Routing(props: Props) {
    const [control, setControl] = useState<L.Routing.Control>();
    const iconStart = L.divIcon({
        html: `<svg width="40" height="40" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M6.66675 24.9999C6.66675 24.9999 8.33341 23.3333 13.3334 23.3333C18.3334 23.3333 21.6667 26.6666 26.6667 26.6666C31.6667 26.6666 33.3334 24.9999 33.3334 24.9999V4.99992C33.3334 4.99992 31.6667 6.66659 26.6667 6.66659C21.6667 6.66659 18.3334 3.33325 13.3334 3.33325C8.33341 3.33325 6.66675 4.99992 6.66675 4.99992V24.9999Z" fill="url(#paint0_linear_115_19)" stroke="#3D874D" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    <path d="M6.66675 36.6667V25" stroke="#3D874D" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    <defs>
    <linearGradient id="paint0_linear_115_19" x1="7" y1="4.5" x2="34.5" y2="28.5" gradientUnits="userSpaceOnUse">
    <stop offset="0.00286713" stop-color="#EAFB2C"/>
    <stop offset="0.49767" stop-color="#14C2F9"/>
    <stop offset="0.987264" stop-color="#02B109"/>
    </linearGradient>
    </defs>
    </svg>`,
        className: "",
        iconSize: [25, 25],
        iconAnchor: [0, 25],
    });

    useEffect(() => {
        control?.setWaypoints(props.coords.map((coord) =>
            L.latLng(parseFloat(coord.latitude), parseFloat(coord.longitude))))
    }, [props.coords]);

    const map = useMap();
    useEffect(() => {

        const control = L.Routing.control({
            waypoints: props.coords.map((coord) =>
                L.latLng(parseFloat(coord.latitude), parseFloat(coord.longitude)))
            ,
            createMarker:function(i, wp, nWps){
                if (i === 0 || i === nWps - 1) {
                    return L.marker(wp.latLng, {icon: iconStart, draggable:true });
                }else{
                    return L.marker(wp.latLng, {icon: iconStart, draggable:true });
                }},
            showAlternatives: false,
            lineOptions: {
                styles: [
                    {color: "black", opacity: 0.15, weight: 9},
                    {color: "white", opacity: 0.8, weight: 6},
                    {color: "blue", opacity: 0.5, weight: 5},
                ],
                extendToWaypoints: props.planOn,
                missingRouteTolerance: 1,
            },
            addWaypoints: props.planOn,
            show: props.planOn,
            totalDistanceRoundingSensitivity: 1,
            //@ts-expect-error Library
            geocoder: props.planOn ? L.Control.Geocoder.nominatim() : null,
            routeWhileDragging: props.planOn,
        }).addTo(map);
        if (props.setter) {
            props.setter(control)
        } else {
            setControl(control);
        }
        return () => {
            map.removeControl(control);
        }
    }, [map]);


    return null;
}