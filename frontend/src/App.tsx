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



function App() {

    const {data, error, mutate} = useSWR("/api/routes", fetcher)
    console.log(data)
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;

    // eslint-disable-next-line react-hooks/rules-of-hooks


    async function handleSubmit(route: MyRouteDto) {
        const response = await fetch("/api/routes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: route.name, dateTime: route.dateTime}),
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
                    <Route index element={<Home/>}/>
                    <Route path={"/routes"} element={<RoutesList routesData={data}/>}/>
                    <Route path={"/routes/:id"} element={<RouteDetails mutateF={mutate} onSubmit={handleSubmit}/>}/>
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
