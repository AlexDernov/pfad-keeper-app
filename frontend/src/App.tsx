import './App.css'
import axios from "axios";
import useSWR from "swr";
import {Routes, Route} from "react-router-dom";
import RoutesList from "./components/routes-list.tsx";
import NavBar from "./components/NavBar.tsx";
import styled from "styled-components";
import Home from "./components/home.tsx";
import NoPage from "./components/NoPage.tsx";

const fetcher = async (url: string) => {
    const response = await axios.get(url);
    return response.data
}

function App() {

    const {data, error} = useSWR("/api/routes", fetcher)
    console.log(data)
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;
    return (
        <><NavBar/>
            <StyledDiv>
                <Routes>
                    <Route index element={<Home/>}/>
                    <Route path={"/routes"} element={<RoutesList routesData={data}/>}/>
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
