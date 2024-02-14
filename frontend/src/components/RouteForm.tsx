import '../index.css'
//import map from "../images/map.png"
import styled from "styled-components";
import React, {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {Control, Coords, LatLngExpression} from "leaflet";
import StyledMap from "./MapContainer.tsx";
import "leaflet/dist/leaflet.css";
import {MapContainer, TileLayer} from "react-leaflet";
import Routing from "../Routing.tsx";


type PropsForm = {
    name: string;
    date: string;
    isEdit: boolean
    onSubmit: (route: MyRouteDto) => void;
}
export default function RouteForm(props: PropsForm) {
    const [name, setName] = useState<string>("");
    const [dateTime, setDateTime] = useState<Date>(new Date());
    const [control, setControl] = useState<Control>()
    const [coords, setCoords] =useState<Coords[]>([])
    const position: LatLngExpression | undefined = [51.505, -0.09];
    const navigate = useNavigate()

    function handleSubmit(event: ChangeEvent<HTMLFormElement>) {
        event.preventDefault();
        props.onSubmit({name, dateTime, coords});
        navigate("/routes")

    }
    function getCoords() {
        const waypoints = control?.getWaypoints();
        if (waypoints) {
            const extractedCoords = waypoints.map(coord => ({
                latitude: coord.latLng.lat,
                longitude: coord.latLng.lng
            }));
            setCoords(extractedCoords);
            return extractedCoords
        } return []
    }

    return (
        <>
            <section>
                <StyledMapContainer center={position} zoom={13} contextmenu={true}
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
                    <Routing setter={setControl}/>
                </StyledMapContainer>
                <button type="button" onClick={() => {
                    console.log(getCoords())
                }}>Coords speichern
                </button>
            </section>
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
                <button type={"submit"}>Speichern</button>
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

const StyledImg = styled.img`
    width: 70vw`;

const StyledMapContainer = styled(MapContainer)`
    position: relative;
    width: 100vw !important;
    height: 60vh !important;
`;
