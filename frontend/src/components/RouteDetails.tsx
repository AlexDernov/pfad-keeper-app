import {fetcher} from "./fetcher.tsx";
import useSWR, {KeyedMutator} from "swr";
import {useNavigate, useParams} from "react-router-dom";
import "leaflet/dist/leaflet.css";
import RouteForm from "./RouteForm.tsx";
import React, {useEffect, useState} from "react";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {MapContainer, TileLayer, useMap} from "react-leaflet";
import L, {Control, Coords, LatLngExpression} from "leaflet";
import Routing from "../Routing.tsx";
import styled from "styled-components";
import RouteLine from "./RouteLine.tsx";


type Props = {
    mutateF: KeyedMutator<any>,
    onSubmit: (route: MyRouteDto) => void,
}
export default function RouteDetails(props: Props) {

    const position: LatLngExpression | undefined = [51.09, 10.27];
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate()
    const {id} = useParams();
    const {data, error, mutate} = useSWR(`/api/routes/${id}`, fetcher)

    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;



    async function handleEditRoute(route: MyRouteDto) {
console.log(`Route Details"${route.name}`);
        const response = await fetch(`/api/routes/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: route.name, dateTime: route.dateTime, coords:route.coords}),
        });
        if (response.ok) {
            await mutate();
            await props.mutateF();
            setIsEditMode(false);
        }
    }

    async function handleDeleteRoute() {
        const response = await fetch(`/api/routes/${id}`, {
            method: "DELETE",
        });
        if (!response.ok) {
            return <h1>Something gone wrong!</h1>;
        }

        if (response.ok) {
            await props.mutateF();
            navigate("/routes")
        }
    }

    return (
        <>
        {isEditMode?
            <RouteForm name={data.name} date={data.dateTime} isEdit={isEditMode} onSubmit={handleEditRoute} coords={data.coords}/>
        :(
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
                <RouteLine coords={data.coords}/>
            </StyledMapContainer>

            <h2>Ort: {data.name}</h2>
            <p>Datum: {new Date(data.dateTime).toLocaleDateString()}</p></>)}

            {!isEditMode ? (
                <StyledButton
                    type="button"
                    onClick={() => {
                        setIsEditMode(!isEditMode);
                    }}
                >
                    Edit
                </StyledButton>
            ) : null}
            {!isEditMode ? (
                <StyledButton type="button" onClick={handleDeleteRoute}>
                    Delete
                </StyledButton>
            ) : (
                <StyledButton
                    type="button"
                    onClick={() => {
                        setIsEditMode(!isEditMode);
                    }}
                >
                    Cancel
                </StyledButton>
            )}

        </>
    )
}
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