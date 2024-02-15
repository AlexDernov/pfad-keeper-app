import {useMap} from "react-leaflet";
import {useEffect, useState} from "react";
import "../index.css";
import L from "leaflet";
import {MyCoords} from "../types/MyCoords.tsx";

type Props = {
    coords: MyCoords[]
}
export default function RouteLine(props: Props) {
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
                extendToWaypoints: false,
                missingRouteTolerance: 1,
            },
            addWaypoints: false,
            show: false,
            totalDistanceRoundingSensitivity: 1,
            routeWhileDragging: false,
        }).addTo(map);
        setControl(control);
        return () => {
            map.removeControl(control);
        }
    }, [map]);


    return null;
}