import {fetcher} from "./fetcher.tsx";
import useSWR from "swr";
import {useNavigate, useParams} from "react-router-dom";
import "leaflet/dist/leaflet.css";
import RouteForm from "./RouteForm.tsx";
import {ChangeEvent, useEffect, useState} from "react";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import {MapContainer, TileLayer} from "react-leaflet";
import {LatLngExpression} from "leaflet";
import styled from "styled-components";
import Routing from "../Routing.tsx";
import axios from "axios";
import Carousel from "./Carousel.tsx";
import {MyImages} from "../types/MyImages.tsx";
import ImagesList from "./images-list.tsx";
import {MyUser} from "../types/MyUsers.tsx";

type Props = {
    mutateF: () => void,
    onSubmit: (route: MyRouteDto) => void,
    dataImages: MyImages[],
    handleImgDelete: (id: string) => void
    hostUser: MyUser;
}
export default function RouteDetails(props: Readonly<Props>) {
    const [file, setFile] = useState<File | null>(null);
    const position: LatLngExpression | undefined = [51.09, 10.27];
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate()
    const [imgSaved, setImgSaved] = useState(false);
    const [usersAll, setUsersAll] = useState<MyUser[]>([])

    useEffect(() => {
        axios.get("/api/users").then(response =>
            setUsersAll(response.data))
    }, [])


    const {id } = useParams<string>();
    const {data, error, mutate} = useSWR(`/api/routes/${id}`, fetcher)
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;
    if (!id) return <div>Loading</div>;
    if(!usersAll) return <div>Loading</div>
    // eslint-disable-next-line react-hooks/rules-of-hooks

    function uploadFile(file: File) {
        const formData = new FormData();
        formData.append("file", file)
        formData.append("data", new Blob([JSON.stringify({"routeId": id})], {type: "application/json"}))
        return axios.post("/api/images", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            }
        })
    }

    async function handleEditRoute(route: MyRouteDto) {

        const response = await fetch(`/api/routes/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: route.name, dateTime: route.dateTime, coords: route.coords, userIds: route.members}),
        });
        if (response.ok) {
            await mutate();
            props.mutateF();
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
            props.mutateF();
            navigate("/routes")
        }
    }

    function handleChangeFile(event: ChangeEvent<HTMLInputElement>) {
        if (!event.target.files) {
            return;
        } else {
            setFile(event.target.files[0])
        }
    }

    function handleSaveImg() {
        if (!file) {
            return;
        }
        uploadFile(file)
            .then(response => {
                // Handle response if needed
                console.log(response);
                setImgSaved(true);
            })
            .catch(error => {
                // Handle error if needed
                console.error(error);
            });
        setImgSaved(false);
    }
    function filterUsersByRouteId(users: MyUser[], routeId: string) {
        return users.filter((user) => user?.routeIds.includes(routeId));
    }
    const filteredUsers = filterUsersByRouteId(usersAll, id);

    return (
        <StyledDetails>
            {isEditMode ?
                <RouteForm name={data.name} usersOfRoute={filteredUsers} routeId={id} allUsers={usersAll} date={data.dateTime} hostUser={props.hostUser} isEdit={isEditMode} onSubmit={handleEditRoute}
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
                            <Routing setter={undefined} coords={data.coords} planOn={false}/>
                        </StyledMapContainer>

                        <StyledH2>{data.name}</StyledH2>
                        <StyledP>Datum: <i>{new Date(data.dateTime).toLocaleDateString()}</i></StyledP></>)}
            {isEditMode ? null : <Carousel dataImages={props.dataImages} routeId={id}/>}
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
                {isEditMode?<>
                <ImagesList imgData={props.dataImages} routeID={id} onDelete={props.handleImgDelete}/>
                <div>
                    <input type="file" onChange={handleChangeFile}/>
                    {file && !imgSaved ?
                        <img src={URL.createObjectURL(file)} alt={"Bild"} width="auto" height="300vw"/> : null}
                    {file && !imgSaved ?
                        <StyledButton type="button" onClick={handleSaveImg}>Save Img</StyledButton> : null}
                </div></>: null}
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
            display: flex;
            flex-direction: column;
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