import L, {LatLngExpression} from "leaflet";
import Routing from "../Routing.tsx";
import styled from "styled-components";
import "leaflet/dist/leaflet.css";
import {MyRoute} from "../types/MyRoute.tsx";
import {NavLink} from "react-router-dom";
import {MapContainer, Marker, Popup, TileLayer} from "react-leaflet";
import React from "react";

type Props = {
    routesData?: MyRoute[],
    oneRouteData?: MyRoute,
    setter?:  React.Dispatch<React.SetStateAction<L.Routing.Control | undefined>>,
    planOn:boolean,
    isHome:boolean
}
export default function InteractiveMap(props: Readonly<Props>) {
    const position: LatLngExpression | undefined = [51.09, 10.27];

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
    return (
        <StyledMapContainer center={position} zoom={5}  contextmenu={true}
                      contextmenuItems={[{
                          text: "Start from here",
                      }, {
                          text: `Go to here`,
                      }]}>
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright"> OpenStreetMap
          </a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />{props.isHome && props.routesData ?
            props.routesData.map((route) =>
                <Marker
                    key={route.id}
                    position={[parseFloat(route.coords[0].latitude), parseFloat(route.coords[0].longitude)]}
                    icon={iconStart}
                    contextmenu={false}
                    contextmenuItems={[]}
                > <Link to={`/routes/${route?.id}`}>
                    <Popup>
                        <PopHead>
                            <strong>{route?.name}</strong>
                        </PopHead>
                        <PopLink>Click für mehr Infos</PopLink>
                    </Popup>
                </Link>
                </Marker>
            ):
                <StyledRouting setter={props.setter} coords={props.oneRouteData?.coords ?? []} planOn={props.planOn}/>}
        </StyledMapContainer>)
}

const StyledMapContainer = styled(MapContainer)`
    height: 50vh;
    color: black;
    position: relative;
    width: 100vw;
    @media only screen and (max-width: 600px) {
        height: 40vh;
    }
`;
const StyledRouting = styled(Routing)`
    width: 30vw !important;
    height: 90% !important;
`;
const PopHead = styled.p`
    padding: 0;
    margin: 0;
    font-size: 0.875em;
    text-align: center;
`;
const Link = styled(NavLink)`
    text-decoration: none;
    &:hover {
        font-size: 1.2em;
    }
`;
const PopLink = styled.p`
    color: purple;
    cursor: pointer;
    padding: 0;
    margin: 0;
    letter-spacing: 1.5px;
    font-size: 0.775em;
    text-align: center;
`;
