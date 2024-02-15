import {fetcher} from "./fetcher.tsx";
import useSWR, {KeyedMutator} from "swr";
import {useNavigate, useParams} from "react-router-dom";
import "leaflet/dist/leaflet.css";
import RouteForm from "./RouteForm.tsx";
import {useState} from "react";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {MapContainer, TileLayer} from "react-leaflet";
import {LatLngExpression} from "leaflet";
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
        const response = await fetch(`/api/routes/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: route.name, dateTime: route.dateTime, coords: route.coords}),
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
        <StyledDetails>
            {isEditMode ?
                <RouteForm name={data.name} date={data.dateTime} isEdit={isEditMode} onSubmit={handleEditRoute}
                           coords={data.coords}/>
                : (
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

                        <StyledH2>{data.name}</StyledH2>
                        <StyledP>Datum: <i>{new Date(data.dateTime).toLocaleDateString()}</i></StyledP></>)}
            <StyledDiv>
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
            </StyledDiv>
        </StyledDetails>
    )
}
const StyledH2 = styled.h2`
    font-size: 3vw;
    margin: 4vw 0 1vw 0;
`;
const StyledP = styled.p`
    margin: 0 0 1vw 0;
    font-size: 2.5vw;
`;

const StyledDetails = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;
const StyledDiv = styled.div`
    `
;
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
    font-size: 1.7vw;
    font-weight: 500;
    margin: 0.5vw 1vw;
    cursor: pointer;

    &:hover {
        padding: 0.5vw 1vw;
        font-size: 2vw;
        margin: 0.55vw 0.5vw;
    }
`;