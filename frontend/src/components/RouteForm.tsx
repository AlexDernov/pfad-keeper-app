import '../index.css'
import styled from "styled-components";
import {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import L, {LatLngExpression} from "leaflet";
import "leaflet/dist/leaflet.css";
import {MapContainer, TileLayer} from "react-leaflet";
import Routing from "../Routing.tsx";

import {MyCoords} from "../types/MyCoords.tsx";
import {MyUsersDto} from "../types/MyUsersDto.tsx";
import {MyUser} from "../types/MyUsers.tsx";



type PropsForm = {
    name: string;
    date: string;
    hostUser: MyUser;
    routeId: string | undefined;
    usersOfRoute: MyUsersDto[] | [];
    coords: MyCoords[];
    isEdit: boolean;
    allUsers: MyUser[];
    onSubmit: (route: MyRouteDto) => void;
}
export default function RouteForm(props: PropsForm) {
    const [name, setName] = useState<string>(props.name);
    const [dateTime, setDateTime] = useState<Date>(new Date(props.date));
    const [control, setControl] = useState<L.Routing.Control>()
    const [searchTerm, setSearchTerm] = useState('');
    const [usersOfRoute, setUsersOfRoute] = useState<MyUsersDto[]>(props.usersOfRoute || []);
    const [searchResult, setSearchResult] = useState<MyUser>();
    const position: LatLngExpression | undefined = [51.09, 10.27];
    const navigate = useNavigate()
    const [usersNotInRoute, SetUsersNotInRoute] = useState<MyUser[]>([])
console.log(usersNotInRoute);
    function handleSubmit(event: ChangeEvent<HTMLFormElement>) {
        event.preventDefault();
        //@ts-expect-error Library
        const waypoints: { latLng: L.LatLng }[] = control?.getWaypoints();
        if (waypoints) {
            const extractedCoords = waypoints.map(coord => ({
                latitude: coord.latLng.lat.toString(),
                longitude: coord.latLng.lng.toString()
            }));
            {
                !props.isEdit ? props.onSubmit({
                        name,
                        dateTime,
                        coords: extractedCoords,
                        members: [{email: props.hostUser?.email, name: props.hostUser?.name}]
                    }) :
                    props.onSubmit({name, dateTime, coords: extractedCoords, members: usersOfRoute});
            }
            {
                props.isEdit ? navigate(`/routes/${props.routeId}`) : navigate("/routes")
            }
            console.log(`RouteForm"${name}`);
        } else
            alert("Eine Route soll ausgewählt sein!")
    }

    const handleSearch = (event: ChangeEvent<HTMLInputElement>) => {
        const search = event.target.value.toLowerCase();
        setSearchTerm(search);
        //axios.get("/api/routes/" + props.routeId).then(r => setRoute(r.data))
        //const membersInRoute = route?.members
        SetUsersNotInRoute(props.allUsers.filter(user => !usersOfRoute?.some(
            member => user?.email === member?.email)))
        const result = usersNotInRoute.find(item =>
            item?.email?.toLowerCase().includes(searchTerm) ||
            item?.name?.toLowerCase().includes(searchTerm)
        );
        setSearchResult(result);
    };

    async function handleMembersAdd() {
        if (searchResult) {
            await fetch(`/api/users/${props.routeId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: searchResult?.email,
            });
            setUsersOfRoute([...usersOfRoute, searchResult]);
            props.isEdit = true;
        } else {
            alert("No user with such email or name!")
        }
    }

    async function handleMembersDelete(email:string | undefined) {
       if(email) {
           await fetch(`/api/users/${props.routeId}`, {
               method: "POST",
               headers: {
                   "Content-Type": "application/json",
               },
               body: email
           });
           const updatedUsersOfRoute = usersOfRoute.filter(
               user => user?.email !== email)
           setUsersOfRoute(updatedUsersOfRoute);
           props.isEdit = true;
       }

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
                <StyledRouting setter={setControl} coords={props.coords} planOn={true}/>

            </StyledMapContainer>

            <StyledForm onSubmit={handleSubmit}>
                <StyledLegend>{props.isEdit ? "Edit die Route" : "Neue Route"}</StyledLegend>
                <StyledP>{props.isEdit ? null : "Wählen Sie bitte die Start- und Endpunkte Ihre Route aus. Sie können auch die zwischen Stops addieren, verändern und löschen."}</StyledP>

                <StyledSection>
                    <StyledLabel htmlFor={"name"}>Wo:
                        <StyledInput type={"text"} id={"name"} onChange={(e) => setName(e.target.value)}
                                     defaultValue={props.name}
                        /></StyledLabel>
                    <StyledLabel htmlFor={"dateTime"}>Wann:
                        <StyledInput2 type={"datetime-local"} name="date" id={"dateTime"}
                                      onChange={(e) => setDateTime(new Date(e.target.value))} defaultValue={props.date}
                                      required
                                      pattern="\d{4}-\d{2}-\d{2}" min="2023-01-01"
                                      max="2080-01-01"/>
                        <span className="validity"/></StyledLabel>
                    <StyledLabel htmlFor={"members"}>Teilnehmer:
                        <StyledInput type="text" placeholder="Search by Google-email or name"
                                     value={searchTerm}
                                     onChange={handleSearch} list="searchSuggestions"
                        /></StyledLabel>
                    <button type="button" onClick={handleMembersAdd}>Add</button>
                    <datalist id="searchSuggestions">
                        {usersNotInRoute.map(result => (
                            <option key={result?.email} value={result?.name}/>
                        ))}
                    </datalist>
                    <ul>
                        {usersOfRoute?.map(user => (<>
                            <li key={user?.email}>{user?.name ? user?.name : user?.email}</li>
                            <button type="button" onClick={()=>handleMembersDelete(user?.email)}> Delete</button>
                        </>))}
                    </ul>
                </StyledSection>

                <StyledButton type={"submit"}>Route speichern</StyledButton>
            </StyledForm>
        </StyledDiv>
    )
}

const StyledDiv = styled.div`
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