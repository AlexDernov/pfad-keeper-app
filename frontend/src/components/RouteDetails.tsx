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
    const {data, error} = useSWR(`/api/routes/${id}`, fetcher)

    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;



    async function handleEditRoute(route: MyRouteDto) {

        const response = await fetch(`/api/routes/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: route.name, dateTime: route.dateTime}),
        });
        if (response.ok) {
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
            <p>Datum: {new Date(data.dateTime).toLocaleDateString()}</p>
            <div>
                {isEditMode && (
                    <RouteForm name={data.name} date={data.dateTime} isEdit={isEditMode} onSubmit={handleEditRoute}/>
                )}
            </div>

            {!isEditMode ? (
                <button
                    type="button"
                    onClick={() => {
                        setIsEditMode(!isEditMode);
                    }}
                >
                    Edit
                </button>
            ) : null}
            {!isEditMode ? (
                <button type="button" onClick={handleDeleteRoute}>
                    Delete
                </button>
            ) : (
                <button
                    type="button"
                    onClick={() => {
                        setIsEditMode(!isEditMode);
                    }}
                >
                    Cancel
                </button>
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