import Routing from "../Routing.tsx";
import {MapContainer, TileLayer} from "react-leaflet";
import {Control, LatLngExpression} from "leaflet";
import "leaflet/dist/leaflet.css";
import React from "react";
import styled from "styled-components";

type Props = {
    onGetCoords: ()=> any,
    setter:  React.Dispatch<React.SetStateAction<Control | undefined>>
}
export default function StyledMap(props: Props) {
    const position: LatLngExpression | undefined = [51.505, -0.09];
    return (
       <>
            <StyledMapContainer center={position} zoom={13} contextmenu={true}
                                contextmenuItems={[{
                                    text: "Start from here",
                                    //callback: startHere
                                }, {
                                    text: `Go to here`,
                                    // callback: goHere
                                }]}>
                {/* <LeafletControlGeocoder />**/}

                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright"> OpenStreetMap
          </a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Routing setter={props.setter}/>
            </StyledMapContainer>
            <button type="button" onClick={() => {
                console.log(props.onGetCoords())
            }}>Save
            </button>
        </>)
}
const StyledMapContainer = styled(MapContainer)`
position: unset !important;
    width: 100vw !important;
`;

