import '../index.css'
import styled from "styled-components";
import {ChangeEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import {MyUsersDto} from "../types/MyUsersDto.tsx";
import {MyUser} from "../types/MyUsers.tsx";
import InteractiveMap from "./interactiveMap.tsx";
import {MyRoute} from "../types/MyRoute.tsx";


type PropsForm = {
    name: string;
    date: string;
    logInUser: MyUsersDto;
    routeId?: string;
    usersOfRoute: MyUsersDto[] | [];
    isEdit: boolean;
    allUsers: MyUser[];
    onSubmit: (route: MyRouteDto) => void;
    onDeleteMembers: (userToDelete: MyUsersDto) => void;
    routeData?:MyRoute;
}
export default function RouteForm(props: Readonly<PropsForm>) {
    const [name, setName] = useState<string>(props.name);
    const [dateTime, setDateTime] = useState<Date>(new Date(props.date));
    const [control, setControl] = useState<L.Routing.Control>()
    const [searchTerm, setSearchTerm] = useState('');
    const [usersOfRoute, setUsersOfRoute] = useState<MyUsersDto[]>([]);
    const [searchResult, setSearchResult] = useState<MyUsersDto>();
    const navigate = useNavigate()
    const [usersNotInRoute, setUsersNotInRoute] = useState<MyUsersDto[]>([])

    if (props.logInUser === null || undefined) return <div>Loading...</div>

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
                !props.isEdit ?
                    props.onSubmit({
                        name,
                        dateTime,
                        coords: extractedCoords,
                        members: [...usersOfRoute, {email: props.logInUser.email, name: props.logInUser.name}]
                    }) :
                    props.onSubmit({name, dateTime, coords: extractedCoords, members: [...(props.usersOfRoute || []), ...usersOfRoute]})
            }
            {
                props.isEdit ? navigate(`/routes/${props.routeId}`) : navigate("/routes")
            }
        } else
            alert("Eine Route soll ausgewählt sein!")
    }

    const handleSearch = (event: ChangeEvent<HTMLInputElement>) => {
        const search = event.target.value.toLowerCase();
        setSearchTerm(search);
        setUsersNotInRoute(props.allUsers.filter(user => !usersOfRoute.some(
            member => user.email === member.email)).filter(user => !props.usersOfRoute.some(
            member => user.email === member?.email)))

        const result = usersNotInRoute.find(item =>
            item?.email?.toLowerCase().includes(searchTerm) ||
            item?.name?.toLowerCase().includes(searchTerm)
        );
        setSearchResult(result);
    };

    return (
        <StyledDiv>
<InteractiveMap oneRouteData={props.routeData} setter={setControl} planOn={true} isHome={false}/>
            <StyledForm onSubmit={handleSubmit}>
                <StyledLegend>{props.isEdit ? "Edit die Route" : "Neue Route"}</StyledLegend>
                <StyledP>{props.isEdit ? null : "Wählen Sie bitte die Start- und Endpunkte Ihre Route aus. Sie können auch die zwischen Stops addieren, verändern und löschen."}</StyledP>

                <StyledSection>
                    <StyledLabel htmlFor={"name"}>Wo:
                        <StyledInput type={"text"} id={"name"} onChange={(e) => setName(e.target.value)}
                                     defaultValue={props.name}
                        /></StyledLabel>
                    <StyledLabel htmlFor={"dateTime"}>Wann:
                        <InputDiv> <StyledInput2 type={"datetime-local"} name="date" id={"dateTime"}
                                                 onChange={(e) => setDateTime(new Date(e.target.value))}
                                                 defaultValue={props.date}
                                                 required
                                                 pattern="\d{4}-\d{2}-\d{2}" min="2023-01-01"
                                                 max="2080-01-01"/>
                            <span className="validity"/></InputDiv></StyledLabel>
                    <StyledMemberDiv> <StyledLabel htmlFor={"members"}>Teilnehmer:
                        <InputDiv>
                            <StyledInput2 list="searchSuggestions" type="text"
                                          placeholder="Search by Google-email or name"
                                          value={searchTerm}
                                          onChange={handleSearch}
                            />
                            {usersNotInRoute && <StyledDatalist id="searchSuggestions">
                                {usersNotInRoute.map(result => (
                                    <StyledOption key={result.email} value={result.name}/>
                                ))}
                            </StyledDatalist>}
                            <StyledAddButton type="button" title="ADD"
                                             onClick={() => searchResult ? setUsersOfRoute([...usersOfRoute, {
                                                 email: searchResult.email,
                                                 name: searchResult?.name
                                             }]) : null}>✚
                            </StyledAddButton></InputDiv></StyledLabel>

                    </StyledMemberDiv>
                    {usersOfRoute &&
                    <StyledUl>
                        {usersOfRoute.map(user => (<StyledLiDiv>
                            <StyledLi key={user.email}>{user.name ? user.name : user.email}</StyledLi>
                            <StyledDeleteButton type="button" title="DELETE"
                                                onClick={() => setUsersOfRoute([...usersOfRoute.filter(userToStayInList => userToStayInList.email !== user.email)])}> ✘
                            </StyledDeleteButton>
                        </StyledLiDiv>))}
                    </StyledUl>}
                    {props.usersOfRoute &&
                    <StyledUl>
                        {props.usersOfRoute.map(userSaved => (<StyledLiDiv>
                                <StyledLi
                                    key={userSaved.email}>{userSaved.name ? userSaved.name : userSaved.email}</StyledLi>
                                <StyledDeleteButton type="button" title="DELETE"
                                                    onClick={() => props.onDeleteMembers(userSaved)}> ✘
                                </StyledDeleteButton></StyledLiDiv>
                        ))}
                    </StyledUl>}
                </StyledSection>

                <StyledButton type={"submit"}>Route speichern</StyledButton>
            </StyledForm>
        </StyledDiv>
    )
}
const StyledDatalist = styled.datalist`
    position: absolute;
    max-height: 20em;
    border: 0 none;
    overflow-x: hidden;
    overflow-y: auto;
    font-size: 0.8em;
    padding: 0.3em 1em;
    background-color: #ccc;
    cursor: pointer;

    &:hover &:focus {
        color: #fff;
        background-color: #036;
        outline: 0 none;
    }
`;
const StyledOption = styled.option`
    font-size: 0.8em;
    padding: 0.3em 1em;
    background-color: #ccc;
    cursor: pointer;

    &:hover &:focus {
        color: #fff;
        background-color: #036;
        outline: 0 none;
    }
`;
const InputDiv = styled.div`
    width: 100%`;
const StyledMemberDiv = styled.div`
    width: 100%;
    display: flex`;

const StyledUl = styled.ul`
    margin: 0 0 0 -0.5vw;
    width: 40vw;
`;
const StyledLiDiv = styled.div`
    width: 100%;
    display: flex;
`;
const StyledLi = styled.li`
    list-style-type: disc;
    list-style-position: inherit;
    width: 60%;
    font-size: 2vw;
    padding: 1vw;

    &::marker {
        color: #1c859c;
    }
`;

const StyledDeleteButton = styled.button`
    color: #1c859c;
    border: transparent none;
    cursor: pointer;
    font-size: 2.5vw;
    background-color: transparent`;

const StyledAddButton = styled.button`
    color: #1c859c;
    margin: 0.5vw;
    border: transparent none;
    cursor: pointer;
    font-size: 2.5vw;
    background-color: transparent`;

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
    width: 75%;
    font-size: 2vw;
    padding: 1vw;
    border-radius: 0.8vw;
    border: rgba(162, 160, 160, 0.92) solid;
`;
const StyledForm = styled.form`
    display: flex;
    width: 75%;
    flex-direction: column;
    align-items: center;
    justify-content: space-around`;

const StyledSection = styled.section`
    display: flex;
    flex-direction: column;
    align-items: start;
    width: 100%;
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

