import {fetcher} from "./fetcher.tsx";
import useSWR from "swr";
import {useNavigate, useParams} from "react-router-dom";
import "leaflet/dist/leaflet.css";
import RouteForm from "./route-form.tsx";
import {ChangeEvent, useEffect, useState} from "react";
import {MyRouteDto} from "../types/MyRouteDto.tsx";
import styled from "styled-components";
import axios from "axios";
import Carousel from "./carousel.tsx";
import {MyImages} from "../types/MyImages.tsx";
import ImagesList from "./images-list.tsx";
import {MyUser} from "../types/MyUsers.tsx";
import {MyUsersDto} from "../types/MyUsersDto.tsx";
import InteractiveMap from "./interactiveMap.tsx";
import {MyRoute} from "../types/MyRoute.tsx";
import Loading from "./loading.tsx";

type Props = {
    mutateF: () => void,
    onSubmit: (route: MyRouteDto) => void,
    dataImages: MyImages[],
    handleImgDelete: (id: string) => void
    logInUser: MyUsersDto
}
export default function RouteDetails(props: Readonly<Props>) {
    const [file, setFile] = useState<File | null>(null);
    const [isEditMode, setIsEditMode] = useState(false);
    const navigate = useNavigate()
    const [imgSaved, setImgSaved] = useState(false);
    const [usersAll, setUsersAll] = useState<MyUser[]>([])


    useEffect(() => {
        axios.get("/api/users").then(response =>
            setUsersAll(response.data))
    }, [])


    const {id} = useParams<string>();
    const {data, isLoading, error, mutate} = useSWR(`/api/routes/${id}`, fetcher)
    const [membersOfRoute, setMembersOfRoute] = useState<MyUsersDto[]>(data?.members)
    const [dataOfOneRoute, setDataOfOneRoute] = useState<MyRoute>(data)
    useEffect(() => {
        setMembersOfRoute(data?.members);
        setDataOfOneRoute(data);
    }, [data]);
    if (isLoading) {
        return <Loading />;
    }
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;
    if (!id) return <div>Loading</div>;
    if (!usersAll) return <div>Loading</div>


    function deleteMemberFromTheRouteList(userToDelete: MyUsersDto) {
        axios.put(`/api/routes/membersList/${id}`, userToDelete).then(response => {
            setMembersOfRoute(response.data)
        })
    }

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
            body: JSON.stringify({
                name: route.name,
                dateTime: route.dateTime,
                coords: route.coords,
                members: route.members
            }),
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
                console.log(response);
                setImgSaved(true);
            })
            .catch(error => {
                console.error(error);
            });
        setImgSaved(false);
    }

    return (
        <StyledDetails>
            {isEditMode ?
                <RouteForm name={data.name} usersOfRoute={membersOfRoute} routeId={id} allUsers={usersAll}
                           date={data.dateTime} logInUser={props.logInUser} isEdit={isEditMode}
                           onSubmit={handleEditRoute} onDeleteMembers={deleteMemberFromTheRouteList}
                            routeData={dataOfOneRoute}/>
                : (
                    <>
                        <InteractiveMap routesData={undefined} oneRouteData={dataOfOneRoute} setter={undefined} planOn={false}
                                        isHome={false}/>
                        <StyledInfoBlock>
                            <div>
                                <StyledH2>{data.name}</StyledH2>
                                <StyledP>Teilnehmer: </StyledP>
                            </div>
                            <div><StyledP2>{new Date(data.dateTime).toLocaleDateString()}</StyledP2>
                                {data ? <StyledUl>

                                    {data?.members.map((userSaved: MyUsersDto) => (<StyledLiDiv>
                                            <StyledLi
                                                key={userSaved.email}>{userSaved.name ? userSaved.name : userSaved.email}</StyledLi>
                                        </StyledLiDiv>
                                    ))}
                                </StyledUl> : null}</div>
                        </StyledInfoBlock>
                    </>)
            }

            {isEditMode ? null : <Carousel dataImages={props.dataImages} routeId={id}/>}
            <StyledDiv>
               {!isEditMode && (
                    <StyledButton
                        type="button"
                        onClick={() => {
                            setIsEditMode(!isEditMode);
                        }}
                    >
                        Edit
                    </StyledButton>
                )}
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
                {isEditMode ? <>
                    <ImageUploadDiv>
                        <ButtonImgDiv>
                            <label> Bild hochladen <StyledImageInput type="file" onChange={handleChangeFile}/></label>
                            {file && !imgSaved &&
                                (<StyledButtonSaveImage type="button" onClick={handleSaveImg}>Save
                                    Img</StyledButtonSaveImage>)}
                        </ButtonImgDiv>
                        {file && !imgSaved &&
                            ( <img src={URL.createObjectURL(file)} alt={"Bild"} width="auto" height="300vw"/>)}
                    </ImageUploadDiv>
                    <ImagesList imgData={props.dataImages} routeID={id} onDelete={props.handleImgDelete}/>

                </> : null}
            </StyledDiv>


        </StyledDetails>
    )
}

const ButtonImgDiv = styled.div`
    display: flex;
    flex-direction: column;
`;
const StyledButtonSaveImage = styled.button`
    color: #ffffff;
    background-color: #1c859c;
    border-radius: 0.975rem;
    padding: 1vw 1.5vw;
    font-size: 1.7vw;
    max-height: 70px;
    font-weight: 500;
    cursor: pointer;
    height: auto;

    &:hover {
        padding: 0.5vw 1vw;
        font-size: 2vw;
        margin: 0.55vw 0.5vw;
    }
`;
const ImageUploadDiv = styled.div`
    margin: 20px 0;
    display: flex;
    justify-content: center;
    gap: 20px;
`;
const StyledImageInput = styled.input`

    &::file-selector-button {
        color: #ffffff;
        background-color: #1c859c;
        border-radius: 0.975rem;
        padding: 1vw 1.5vw;
        font-size: 1.7vw;
        font-weight: 500;
        margin: 0.5vw 1vw;
        cursor: pointer;
    }
`;
const StyledInfoBlock = styled.div`
    display: flex;
    gap: 20vw;
    flex-direction: row;
    align-content: space-between;
    margin: 0 0 2vw 15vw;
`;
const StyledH2 = styled.h2`
    font-size: 3vw;
    padding: 0.5vw;
    margin: 2vw 0 1vw 0;
`;
const StyledP = styled.p`
    font-size: 3vw;
    margin: 0;
    padding: 0.5vw
`;
const StyledP2 = styled.p`
    font-size: 2.5vw;
    margin: 2.6vw 0 1vw 0;
    padding: 0.5vw
`;
const StyledLiDiv = styled.div`
    width: 100%;
    display: flex;
`;
const StyledLi = styled.li`
    list-style-type: disc;
    list-style-position: inherit;
    width: 60%;
    font-size: 2.5vw;
    padding: 1vw;

    &::marker {
        color: #1c859c;
    }
`;
const StyledDetails = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;
const StyledDiv = styled.div`
    display: flex;
    flex-direction: column`;

const StyledUl = styled.ul`
    margin: 0;
    margin-block-start: 0;
    margin-block-end: 0;
    padding-inline-start: 0;
    width: 40vw;
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