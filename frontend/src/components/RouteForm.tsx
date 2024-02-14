import '../index.css'
//import map from "../images/map.png"
import styled from "styled-components";
import  {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {Control, LatLngExpression} from "leaflet";
import "leaflet/dist/leaflet.css";
import {MapContainer, TileLayer} from "react-leaflet";
import Routing from "../Routing.tsx";

import {MyCoords} from "../types/MyCoords.tsx";


type PropsForm = {
    name: string;
    date: string;
    coords: MyCoords[];
    isEdit: boolean;
    onSubmit: (route: MyRouteDto) => void;
}
export default function RouteForm(props: PropsForm) {
    const [name, setName] = useState<string>(props.name);
    const [dateTime, setDateTime] = useState<Date>(new Date(props.date));
    const [control, setControl] = useState<Control>()

    const position: LatLngExpression | undefined = [51.09, 10.27];
    const navigate = useNavigate()

    function handleSubmit(event: ChangeEvent<HTMLFormElement>) {
        event.preventDefault();
        //@ts-expect-error Library
        const waypoints:{latLng:L.LatLng}[] = control?.getWaypoints();
        if (waypoints) {
            const extractedCoords = waypoints.map(coord => ({
                latitude: coord.latLng.lat.toString(),
                longitude: coord.latLng.lng.toString()
            }));

            props.onSubmit({name, dateTime, coords: extractedCoords});
            navigate("/routes")
            console.log(`RouteForm"${name}`);
        } else{
            alert("Eine Route soll ausgewählt sein!")
        }
    }


    return (
        <>

                <StyledMapContainer center={position} zoom={5} contextmenu={true}
                                    contextmenuItems={[{
                                        text: "Start from here",
                                        //callback: startHere
                                    }, {
                                        text: `Go to here`,
                                        // callback: goHere
                                    }]}>
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright"> OpenStreetMap
          </a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <Routing setter={setControl} coords={props.coords}/>

                </StyledMapContainer>
                <StyledButton type="button" onClick={() => {
                }}>Coords speichern
                </StyledButton>

            <StyledForm onSubmit={handleSubmit}>
                <legend>{props.isEdit ? "Edit die Route" : "Neue Route"}</legend>


                <StyledSection>
                    <label htmlFor={"name"}>Wo:
                        <input type={"text"} id={"name"} onChange={(e) => setName(e.target.value)}
                               defaultValue={props.name}
                               pattern="[0-9A-Za-zß-üА-Яа-яЁё?\s]+"/></label>
                    <label htmlFor={"dateTime"}>Wann:
                        <input type={"datetime-local"} name="date" id={"dateTime"}
                               onChange={(e) => setDateTime(new Date(e.target.value))} defaultValue={props.date}
                               required
                               pattern="\d{4}-\d{2}-\d{2}" min="2023-01-01"
                               max="2080-01-01"/>
                        <span className="validity"/></label>
                </StyledSection>
                <StyledButton type={"submit"}>Route speichern</StyledButton>
            </StyledForm>
        </>
    )
}

const StyledForm = styled.form`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around`;

const StyledSection = styled.section`
    display: flex;
    flex-direction: column;
    align-items: start;
`;

const StyledMapContainer = styled(MapContainer)`
    position: relative;
    margin: 0;
    width: 100vw !important;
    height: 70vh !important;
`;
const StyledButton = styled.button`
    color: #ffffff;
    background-color: #1c859c;
    border-radius: 0.975rem;
    padding: 1vw 1.5vw;
    font-size: 1vw;
    font-weight: 500;
    margin: 0.5vw 1vw;
    cursor: pointer;
    &:hover{
        padding: 0.5vw 1vw;
        font-size: 1.6vw;
        margin: 0.55vw 0.5vw;
    }
`;