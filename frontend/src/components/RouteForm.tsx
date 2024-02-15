import '../index.css'
//import map from "../images/map.png"
import styled from "styled-components";
import {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import L, {LatLngExpression} from "leaflet";
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
    const [control, setControl] = useState<L.Routing.Control>()

    const position: LatLngExpression | undefined = [51.09, 10.27];
    const navigate = useNavigate()

    function handleSubmit(event: ChangeEvent<HTMLFormElement>) {
        event.preventDefault();
        //@ts-expect-error Library
        const waypoints: { latLng: L.LatLng }[] = control?.getWaypoints();
        if (waypoints) {
            const extractedCoords = waypoints.map(coord => ({
                latitude: coord.latLng.lat.toString(),
                longitude: coord.latLng.lng.toString()
            }));

            props.onSubmit({name, dateTime, coords: extractedCoords});
            navigate("/routes")
            console.log(`RouteForm"${name}`);
        } else
            alert("Eine Route soll ausgewählt sein!")
    }


    return (
        <StyledDiv>

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
                <StyledRouting setter={setControl} coords={props.coords}/>

            </StyledMapContainer>

            <StyledForm onSubmit={handleSubmit}>
                <StyledLegend>{props.isEdit ? "Edit die Route" : "Neue Route"}</StyledLegend>
                <StyledP>{props.isEdit ? null : "Wählen Sie bitte die Start- und Endpunkte Ihre Route aus. Sie können auch die zwischen Stops addieren, verändern und löschen."}</StyledP>

                <StyledSection>
                    <StyledLabel htmlFor={"name"}>Wo:
                        <StyledInput type={"text"} id={"name"} onChange={(e) => setName(e.target.value)}
                                     defaultValue={props.name}
                                     pattern="[0-9A-Za-zß-üА-Яа-яЁё?\s]+"/></StyledLabel>
                    <StyledLabel htmlFor={"dateTime"}>Wann:
                        <StyledInput2 type={"datetime-local"} name="date" id={"dateTime"}
                                      onChange={(e) => setDateTime(new Date(e.target.value))} defaultValue={props.date}
                                      required
                                      pattern="\d{4}-\d{2}-\d{2}" min="2023-01-01"
                                      max="2080-01-01"/>
                        <span className="validity"/></StyledLabel>
                </StyledSection>
                <StyledButton type={"submit"}>Route speichern</StyledButton>
            </StyledForm>
        </StyledDiv>
    )
}

const StyledDiv =styled.div`
display: flex;
flex-direction: column;
align-items: center`;

const StyledP = styled.p`
    margin-top: 0;
    font-size: 1.7vw;
    width: 100%;
    text-align: center;
`;
const StyledLabel = styled.label`
    width: 100%;
    margin: 0.5vw;
    font-size: 2.5vw`;

const StyledLegend = styled.legend`
    font-size: 3vw;
    margin: 2vw;
`;
const StyledInput = styled.input`
    width: 100%;
    font-size: 2vw;
    padding: 1vw;
    border-radius: 0.8vw;
    border: rgba(162, 160, 160, 0.92) solid;
`;
const StyledInput2 = styled.input`
    width: 83%;
    font-size: 2vw;
    padding: 1vw;
    border-radius: 0.8vw;
    border: rgba(162, 160, 160, 0.92) solid;
`;
const StyledForm = styled.form`
    display: flex;
    width: 35%;
    flex-direction: column;
    align-items: center;
    justify-content: space-around`;

const StyledSection = styled.section`
    display: flex;
    flex-direction: column;
    align-items: start;
    width: 100%;
`;

const StyledMapContainer = styled(MapContainer)`
    position: relative;
    margin: 0;
    width: 100vw !important;
    height: 60vh !important;
`;
const StyledButton = styled.button`
    color: #ffffff;
    background-color: #1c859c;
    border-radius: 0.975rem;
    padding: 1vw 1.5vw;
    font-size: 1.7vw;
    font-weight: 500;
    margin: 1vw 1vw;
    cursor: pointer;

    &:hover {
        padding: 0.5vw 1vw;
        font-size: 2vw;
        margin: 0.55vw 0.5vw;
    }
`;
const StyledRouting = styled(Routing)`
    width: 30vw !important;
    height: 90% !important;
`;