import RouteForm from "./RouteForm.tsx";
import {MyRouteDto} from "../types/MyRouteDto.tsx";

type PropsNewRoute = {
    onSubmit: (route: MyRouteDto) => void;
}
export default function NewRoute(props: PropsNewRoute) {
    return (
        <>
            <RouteForm onSubmit={props.onSubmit} name={""} date={""} isEdit={false}/>
        </>
    )
}
