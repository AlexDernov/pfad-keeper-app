import {fetcher} from "./fetcher.tsx";
import useSWR, {KeyedMutator} from "swr";
import { useParams} from "react-router-dom";
import map from "../images/map.png"
import RouteForm from "./RouteForm.tsx";
import { useState} from "react";
import {MyRouteDto} from "../types/MyRouteDto.tsx";


type Props={
    mutateF:KeyedMutator<any>,
    onSubmit: (x:MyRouteDto)=> void;}
export default function RouteDetails(props:Props){

    const [isEditMode, setIsEditMode] = useState(false);

    const {id} = useParams();
    const {data, error} = useSWR(`/api/routes/${id}`, fetcher)
    console.log(data)
    if (error) return <div>Error loading data</div>;
    if (!data) return <div>Loading data...</div>;

    async function handleEditRoute(x:MyRouteDto) {
        console.log(id);
        const response = await fetch(`/api/routes/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: x.name, dateTime:x.dateTime}),
        });
        if (response.ok) {
            await props.mutateF();
            setIsEditMode(false);
        }
    }


    return(
        <>
            <img src={map} alt={"map"}/>
            <h2>Ort: {data.name}</h2>
            <p>Datum: {new Date(data.dateTime).toLocaleDateString()}</p>
            <div>
                {isEditMode &&(
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
                    <button type="button" >
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