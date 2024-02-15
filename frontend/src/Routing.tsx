import L from "leaflet";
import 'leaflet-routing-machine';
import {useMap} from "react-leaflet";
import './index.css'
import "leaflet/dist/leaflet.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.css";
import "leaflet-control-geocoder/dist/Control.Geocoder.js";
import "leaflet-routing-machine/dist/leaflet-routing-machine.css";
import React, {useEffect} from "react";
import {MyCoords} from "./types/MyCoords.tsx";

type Props = {
    setter: React.Dispatch<React.SetStateAction<L.Routing.Control | undefined>>
    coords: MyCoords[]
}
export default function Routing(props: Props) {

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
                extendToWaypoints: true,
                missingRouteTolerance: 1,
            },
            addWaypoints: true,
            //@ts-expect-error Library
            geocoder: L.Control.Geocoder.nominatim(),
            routeWhileDragging: true,
        }).addTo(map);
        props.setter(control);
        return () => {
            map.removeControl(control);
        }
    }, [map]);


    return null;
}