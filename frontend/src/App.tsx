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



function App() {
    const [images, setImages] = useState<MyImages[]>([])
    const {data, error, mutate} = useSWR("/api/routes", fetcher)
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;

    async function handelMutate() {
        await mutate();
    }

    useEffect(() => {
        axios.get("/api/images").then(response =>
            setImages(response.data))
    }, [images])

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
            body: JSON.stringify({name: route.name, dateTime: route.dateTime, coords: route.coords}),
        });
        if (response.ok) {
            alert(
                "Your route has been successfully saved."
            )
        } else {
            console.log("Not ok");
        }
    }

    return (
        <><NavBar/>
            <StyledDiv>
                <Routes>
                    <Route index element={<Home routeData={data}/>}/>
                    <Route path={"/routes"} element={<RoutesList routesData={data}/>}/>
                    <Route path={"/routes/:id"} element={<RouteDetails mutateF={handelMutate} dataImages={images}
                                                                       onSubmit={handleSubmit} handleImgDelete={deleteImage}/>}/>
                    <Route path={"/routes/add"} element={<NewRoute onSubmit={handleSubmit}/>}/>
                    <Route path={"/*"} element={<NoPage/>}/>
                </Routes>
            </StyledDiv>
        </>
    )
}

const StyledDiv = styled.div`
    margin-top: 15vw;
`;

export default App
