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