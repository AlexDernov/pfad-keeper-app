import './App.css'
import useSWR from "swr";
import {Routes, Route} from "react-router-dom";
import RoutesList from "./components/routes-list.tsx";
import NavBar from "./components/NavBar.tsx";
import styled from "styled-components";
import Home from "./components/home.tsx";
import NoPage from "./components/NoPage.tsx";
import NewRoute from "./components/NewRoute.tsx";
import {fetcher} from "./components/fetcher.tsx";
import RouteDetails from "./components/RouteDetails.tsx";
import {MyRouteDto} from "./types/MyRouteDto.tsx";
import {useEffect, useState} from "react";
import {MyImages} from "./types/MyImages.tsx";
import axios from "axios";
import ProfilPage from "./components/profil-page.tsx";
import {MyUsersDto} from "./types/MyUsersDto.tsx";
import ProtectedRoutes from "./ProtectedRoutes.tsx";

function App() {
    const [userOnLogin, setUserOnLogin] = useState<MyUsersDto | undefined | null>(undefined);
    const [images, setImages] = useState<MyImages[]>([])
console.log(userOnLogin);
    useEffect(() => {
        axios.get("/api/images").then(response =>
            setImages(response.data))
    }, [])

    const getCurrentUser = () => {
        axios.get<MyUsersDto>("/api/users/me").then((response) => {
           if(response.data){
               setUserOnLogin(response.data);
           }

        });
    };
    useEffect(() => {
        getCurrentUser();

    }, []);


    const logout = () => {
        axios.post("/api/users/logout").then(() => getCurrentUser());
    };
    const {data, error, mutate} = useSWR("/api/routes", fetcher)
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;

    async function handelMutate() {
        await mutate();
    }


    const deleteImage = (id: string) => {
        axios.delete(`/api/images/${id}`)
            .then(() => {
                setImages([...images.filter(image => id !== image.id)]);
            })
    }

    async function handleSubmit(route: MyRouteDto) {
        const response = await fetch("/api/routes", {
            method: "POST",
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
            alert(
                "Your route has been successfully saved."
            )
        }
    }

    return (
        <><NavBar logInUser={userOnLogin} logout={logout}/>
            <StyledDiv>
                <Routes>
                    <Route element={<ProtectedRoutes user={userOnLogin}/>}>
                        <Route path={"/routes"}
                               element={<RoutesList routesData={data} routImages={images} logInUser={userOnLogin}/>}/>
                        <Route path={"/user"}
                               element={<ProfilPage userName={userOnLogin?.name} userEmail={userOnLogin?.email}/>}/>
                        <Route path={"/routes/:id"}
                               element={<RouteDetails mutateF={handelMutate} dataImages={images} logInUser={userOnLogin!}
                                                      onSubmit={handleSubmit} handleImgDelete={deleteImage}/>}/>
                        <Route path={"/routes/add"}
                               element={<NewRoute logInUser={userOnLogin!} onSubmit={handleSubmit}/>}/>
                    </Route>
                    <Route index element={<Home routeData={data}/>}/>
                    <Route path={"/*"} element={<NoPage/>}/>
                </Routes>
            </StyledDiv>
        </>
    )
}

const StyledDiv = styled.div`
    margin-top: 3.5rem;
`;

export default App
